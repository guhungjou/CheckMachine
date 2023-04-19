package com.example0.checkmachine;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.baidu.idl.main.facesdk.attendancelibrary.camera.CameraPreviewManager;
import com.baidu.idl.main.facesdk.attendancelibrary.listener.SdkInitListener;
import com.baidu.idl.main.facesdk.attendancelibrary.manager.FaceSDKManager;
import com.baidu.idl.main.facesdk.attendancelibrary.utils.ToastUtils;
import com.example0.BaseActivity;
import com.example0.retrofit.ApiService;
import com.example0.retrofit.CameraData;
import com.example0.retrofit.Location;
import com.example0.retrofit.PhotoData;
import com.example0.util.GpioUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestCamera2 extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private SurfaceView svContent;
    private int mPreviewWidth;
    private int mPreviewHeight;
    private HandlerThread mInferenceThread;
    private Handler mInferenceHandler;
    private TextView hint;
    private OkHttpClient client;

    private void initListener() {
        if (FaceSDKManager.initStatus != FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
            FaceSDKManager.getInstance().initModel(this, new SdkInitListener() {
                @Override
                public void initStart() {
                }

                @Override
                public void initLicenseSuccess() {
                }

                @Override
                public void initLicenseFail(int errorCode, String msg) {
                }

                @Override
                public void initModelSuccess() {
                    FaceSDKManager.initModelSuccess = true;
                    ToastUtils.toast(TestCamera2.this, "模型加载成功，欢迎使用");
                }

                @Override
                public void initModelFail(int errorCode, String msg) {
                    FaceSDKManager.initModelSuccess = false;
                    if (errorCode != -12) {
                        ToastUtils.toast(TestCamera2.this, "模型加载失败，请尝试重启应用");
                    }
                }
            });
        }
    }

    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
    private Type.Builder yuvType;
    private Type.Builder rgbaType;
    private Allocation in;
    private Allocation out;

    private void initRender() {
        rs = RenderScript.create(this);
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
    }

    private void openlight() {
        int index = 139;
        if (GpioUtils.exportGpio(index)) {
            GpioUtils.upgradeRootPermissionForGpio(index);
            String status = GpioUtils.getGpioDirection(index);
            if ("".equals(status))
                Log.e("TAG", "无效的GPIO");
            else
                Log.e("TAG", "有效的GPIO");
            GpioUtils.setGpioDirection(index, 0);
            GpioUtils.writeGpioValue(index, "0");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        CameraPreviewManager.getInstance().stopPreview();
        super.onCreate(savedInstanceState);
//        FaceSDKManager.getInstance().initDataBases(this);
        setContentView(R.layout.testnircamera);
//        initListener();
//        initRender();

//        mInferenceThread = new HandlerThread("InferenceThread");
//        mInferenceThread.start();
//        mInferenceHandler = new Handler(mInferenceThread.getLooper());

        //界面初始化
        initUI();
        openlight();
        client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .build();

        SurfaceHolder surfaceHolder = svContent.getHolder();
        surfaceHolder.addCallback(this);
    }

    private BoundingBoxView boundingBoxView;
    private int stu_id = -1;

    private boolean isCameraInUse(int cameraId) {
        Camera camera = null;
        boolean inUse = true;
        try {
            camera = Camera.open(cameraId);
            inUse = false;
        } catch (RuntimeException e) {
            // Camera is not available (in use or does not exist)
            // inUse will remain true
        } finally {
            if (camera != null) {
                camera.release();
            }
        }
        return inUse;
    }


    private void initUI() {
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 在这里执行您的返回操作，例如结束当前Activity
                yolov5ncnn.Deinit();
                if (mCamera != null) {
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
                GpioUtils.writeGpioValue(139, "1");
                finish();
            }
        });
        svContent = findViewById(R.id.auto_camera_preview_view);
        boundingBoxView = findViewById(R.id.bounding_box_view);
        hint = findViewById(R.id.check_hint);
        SurfaceHolder holder = svContent.getHolder();

        boolean inUse = isCameraInUse(0);
        if (inUse) {
            Log.e("MainActivity", "Camera is in use");
//            SystemClock.sleep(2000);
        } else {
            Log.e("MainActivity", "Camera is not in use");
        }
//        if (holder != null && holder.getSurface() != null && holder.getSurface().isValid()) {
//            // Surface is available
//            //sleep 2 s
////            SystemClock.sleep(2000);
//        } else {
//            // Surface is not available
//        }

//        if(stu_id<0){
//            Intent intent=getIntent();
//            // Bundle bd=intent.getExtras();
//            String strID =intent.getStringExtra("id");
//            stu_id=Integer.parseInt(strID);
//        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openlight();
        boolean ret_init = yolov5ncnn.Init(getAssets());
        if (!ret_init) {
            Log.e("MainActivity", "yolov5ncnn Init failed");
        } else {
            Log.e("MainActivity", "yolov5ncnn Init success");
        }
        mHolder = holder;

        mCamera = Camera.open(1);

        // 设置预览方向
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(1, info);
        int result = (info.orientation - degrees + 360) % 360;
        mCamera.setDisplayOrientation(result);

        Camera.Parameters parameters = mCamera.getParameters();//获取各项参数
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size size : supportedPreviewSizes) {
            Log.e("Supported Preview Size", "Width: " + size.width + ", Height: " + size.height);
        }
        int desiredWidth = 1280;
        int desiredHeight = 720;
        int minDiff = Integer.MAX_VALUE;
        Camera.Size bestSize = null;

        for (Camera.Size size : supportedPreviewSizes) {
            int diff = Math.abs(size.width - desiredWidth) + Math.abs(size.height - desiredHeight);
            if (diff < minDiff) {
                minDiff = diff;
                bestSize = size;
            }
        }

        if (bestSize != null) {
            Log.e("Best Preview Size", "Width: " + bestSize.width + ", Height: " + bestSize.height);
            parameters.setPreviewSize(bestSize.width, bestSize.height);
            mCamera.setParameters(parameters);
            mPreviewWidth = bestSize.width;
            mPreviewHeight = bestSize.height;
        }

//        parameters.setPictureFormat(ImageFormat.JPEG);//设置图片格式
//        parameters.setPreviewSize(1280, 720);//设置预览大小
//        parameters.setJpegQuality(100);//设置照片质量
//        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//        mCamera.setParameters(parameters);

        // 设置预览大小


        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        boolean ret_init = yolov5ncnn.Init(getAssets());
//        if (!ret_init) {
//            Log.e("MainActivity", "yolov5ncnn Init failed");
//        } else {
//            Log.e("MainActivity", "yolov5ncnn Init success");
//        }
//        mHolder = holder;
//        int rotation = getWindowManager().getDefaultDisplay().getRotation();
//        int degrees = 0;
//        switch (rotation) {
//            case Surface.ROTATION_0:
//                degrees = 0;
//                break;
//            case Surface.ROTATION_90:
//                degrees = 90;
//                break;
//            case Surface.ROTATION_180:
//                degrees = 180;
//                break;
//            case Surface.ROTATION_270:
//                degrees = 270;
//                break;
//        }
//
//        Camera.CameraInfo info = new Camera.CameraInfo();
//        Camera.getCameraInfo(1, info);
//        int result = (info.orientation - degrees + 360) % 360;
//        mCamera.setDisplayOrientation(result);
//        mCamera = Camera.open(1);
//
//        Camera.Parameters parameters = mCamera.getParameters();//获取各项参数
//        parameters.setPictureFormat(ImageFormat.JPEG);//设置图片格式
//        parameters.setPreviewSize(1280, 720);//设置预览大小
//        // parameters.setPictureSize(1280, 720);//设置保存的图片大小
//        parameters.setJpegQuality(100);//设置照片质量
//        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//        // parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//设置自动对焦
//        mCamera.setParameters(parameters);
//        parameters.setPreviewSize(1200, 700); // 设置预览大小
//        mPreviewWidth = 1200;
//        mPreviewHeight = 700;
//        try {
//            mCamera.setPreviewDisplay(mHolder);
//            mCamera.setPreviewCallback(this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mCamera.startPreview();


//        Uri selectedImage = Uri.parse("content://media/external/images/media/712");
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        try {
//            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
//        } catch ( FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }

    // The new size we want to scale to
//        final int REQUIRED_SIZE = 640;
//
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp / 2 < REQUIRED_SIZE
//                    || height_tmp / 2 < REQUIRED_SIZE) {
//                break;
//            }
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//
//        // Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Rotate according to EXIF
//        int rotate = 0;
//        try {
//            ExifInterface exif = new ExifInterface(getContentResolver().openInputStream(selectedImage));
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate = 270;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate = 90;
//                    break;
//            }
//        } catch (IOException e) {
//            Log.e("MainActivity", "ExifInterface IOException");
//        }
//
//        Matrix matrix = new Matrix();
//        matrix.postRotate(rotate);
//        Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
////        Log.e("Info", "bitmap: " + cameraData.getWidth() + " " + cameraData.getHeight() + " " + cameraData.getByteCount());
//
//    }
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private long lastDetectionTime = 0;
    private static final int DETECTION_INTERVAL = 600;
    private static int flag = 0;

    public byte[] convertToNV21(byte[] data, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        int width = size.width;
        int height = size.height;

        byte[] nv21Data = new byte[width * height * 3 / 2];
        int ySize = width * height;
        int uvSize = ySize / 4;

        System.arraycopy(data, 0, nv21Data, 0, ySize);

        for (int i = 0; i < uvSize; i++) {
            nv21Data[ySize + i * 2] = data[ySize + i];
            nv21Data[ySize + i * 2 + 1] = data[ySize + uvSize + i];
        }

        return nv21Data;
    }


    public int[] getSecondHighestConfidenceObjectWithLabel(YoloV5Ncnn.Obj[] objects, String targetLabel) {
        Optional<YoloV5Ncnn.Obj> secondHighestConfidenceObject = Arrays.stream(objects)
                .filter(obj -> obj.label.equals(String.valueOf(targetLabel)))
                .sorted(Comparator.comparing(YoloV5Ncnn.Obj::getProb).reversed())
                .skip(1)
                .findFirst();

        if (secondHighestConfidenceObject.isPresent()) {
            YoloV5Ncnn.Obj obj = secondHighestConfidenceObject.get();
            return new int[]{(int) obj.x, (int) obj.y, (int) obj.w, (int) obj.h};
        }

        return null;
    }

    public int[] getHighestConfidenceObjectWithLabel(YoloV5Ncnn.Obj[] objects, String targetLabel) {
        Optional<YoloV5Ncnn.Obj> highestConfidenceObject = Arrays.stream(objects)
                .filter(obj -> obj.label.equals(String.valueOf(targetLabel)))
                .max(Comparator.comparing(obj -> obj.prob));

        if (highestConfidenceObject.isPresent()) {
            YoloV5Ncnn.Obj obj = highestConfidenceObject.get();
            return new int[]{(int) obj.x, (int) obj.y, (int) obj.w, (int) obj.h};
        }

        return null;
    }

    public float[] getHighestConfidenceObjectWithLabelForAPI(YoloV5Ncnn.Obj[] objects, String targetLabel) {
        Optional<YoloV5Ncnn.Obj> highestConfidenceObject = Arrays.stream(objects)
                .filter(obj -> obj.label.equals(String.valueOf(targetLabel)))
                .max(Comparator.comparing(obj -> obj.prob));

        if (highestConfidenceObject.isPresent()) {
            YoloV5Ncnn.Obj obj = highestConfidenceObject.get();
            float top = obj.y;
            float left = obj.x;
            float bottom = obj.y + obj.h;
            float right = obj.x + obj.w;
            return new float[]{top, left, bottom, right};
        }

        return null;
    }

    public float[] getSecondHighestConfidenceObjectWithLabelForAPI(YoloV5Ncnn.Obj[] objects, String targetLabel) {
        Optional<YoloV5Ncnn.Obj> secondHighestConfidenceObject = Arrays.stream(objects)
                .filter(obj -> obj.label.equals(String.valueOf(targetLabel)))
                .sorted(Comparator.comparing(YoloV5Ncnn.Obj::getProb).reversed())
                .skip(1)
                .findFirst();

        if (secondHighestConfidenceObject.isPresent()) {
            YoloV5Ncnn.Obj obj = secondHighestConfidenceObject.get();
            float top = obj.y;
            float left = obj.x;
            float bottom = obj.y + obj.h;
            float right = obj.x + obj.w;
            return new float[]{top, left, bottom, right};
        }

        return null;
    }


    private Uri bitmapToUri(Bitmap bitmap) {
        File imageTemp = new File(getExternalCacheDir(), "imageOut.jpg");
        if (imageTemp.exists()) {
            imageTemp.delete();
        }
        try {
            imageTemp.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            OutputStream os = new FileOutputStream(imageTemp);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
            Uri uri = null;
            if (Build.VERSION.SDK_INT > 24) {
                uri = FileProvider.getUriForFile(this, "com.example0.fileprovider", imageTemp);
            } else {
                uri = Uri.fromFile(imageTemp);
            }
            return uri;
        } catch (Exception ignored) {
        }
        return null;

    }

    public boolean containsSpecificLabels(YoloV5Ncnn.Obj[] objects) {
        long mouthCount = Arrays.stream(objects)
                .filter(obj -> obj.label.equals("mouth"))
                .count();

        long eyeCount = Arrays.stream(objects)
                .filter(obj -> obj.label.equals("eye"))
                .count();

        long handCount = Arrays.stream(objects)
                .filter(obj -> obj.label.equals("hand"))
                .count();

        return mouthCount == 1 && eyeCount == 1 && handCount == 2;
    }

    private boolean shouldProcessFrames = true;
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (!shouldProcessFrames) {
            return;
        }
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastDetectionTime >= DETECTION_INTERVAL) {
            lastDetectionTime = SystemClock.elapsedRealtime();
            ///////////////////////////
//            if (stu_id == -1) {
//                byte[] nv21Data = convertToNV21(data, camera);
//                FaceSDKManager.getInstance().onDetectCheck(nv21Data, null, null,
//                        mPreviewWidth, mPreviewHeight, 0, new FaceDetectCallBack() {
//                            @Override
//                            public void onFaceDetectCallback(LivenessModel livenessModel) {
//                                Log.e("TAG", livenessModel == null ? "null" : "not null");
//                                // 预览模式
//                                User user = livenessModel.getUser();
//                                if (user == null) {
////                            mUser = null;
//                                    if (livenessModel.isMultiFrame()) {
//
//                                        Log.e("TAG", "未检测到注册用户！");
//
//                                    }
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            hint.setTextColor(Color.RED);
//                                            hint.setText("未检测到注册用户！");
//                                        }
//                                    });
//                                } else {
////                            mUser = user;
////                    String absolutePath = FileUtils.getBatchImportSuccessDirectory()
////                            + "/" + user.getImageName();
////                    Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
////                    nameImage.setImageBitmap(bitmap);
////                    Toast.makeText(FaceCheck.this,FileUtils.spotString(user.getUserName()) + "成功",Toast.LENGTH_SHORT).show();
//
//                                    //获取识别成功的用户id
//                                    String idStr = FileUtils.spotString(user.getUserName());
//                                    stu_id = Integer.parseInt(idStr);
//                                    Log.e("TAG", stu_id + "欢迎您，请晨检！");
////                                                    runOnUiThread(new Runnable() {
////                                                        @Override
////                                                        public void run() {
////                                                            if (toast == null) {
////                                                                toast = Toast.makeText(FaceCheck.this, "欢迎您，请晨检！", Toast.LENGTH_SHORT);
////                                                            } else {
////                                                                toast.cancel();
////                                                                toast = Toast.makeText(FaceCheck.this, "欢迎您，请晨检！", Toast.LENGTH_SHORT);
////                                                            }
////                                                            toast.show();
////                                                        }
////                                                    });
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            hint.setTextColor(Color.YELLOW);
//                                            hint.setText("欢迎您，请晨检！");
//                                        }
//                                    });
//                                }
//
////                                        // 开发模式
////                                        checkOpenDebugResult(livenessModel);
////
////                                        if (isSaveImage){
////                                            SaveImageManager.getInstance().saveImage(livenessModel);
////                                        }
//                            }
//
//                            @Override
//                            public void onTip(int code, String msg) {
//                                Log.e("TAG", "未检测到Tip");
//                                //Toast.makeText(FaceCheck.this, msg, Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void onFaceDetectDarwCallback(LivenessModel livenessModel) {
//                                // 绘制人脸框
//                                // showFrame(livenessModel);
//                                Log.e("TAG", "未检测到Draw");
//
//                            }
//                        });
//                return;
//
//            }
            ///////////////////////////


//        if (flag == 0) {
//            flag = 1;
//        }
//        Log.e("帧率", currentTime - lastDetectionTime + " ");
            // 执行模型
            Camera.Size size = camera.getParameters().getPreviewSize();
            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
            byte[] byteArray = baos.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

//            Log.e("TestCamera", "bitmap: " + bitmap.getWidth() + " " + bitmap.getHeight() + " " + bitmap.getByteCount());
            YoloV5Ncnn.Obj[] objects = yolov5ncnn.Detect(bitmap, false);
//            Arrays.stream(objects).forEach(obj -> Log.e("LABEL","Label: " + obj.label+" Confidence: " + obj.prob));
            boundingBoxView.setPreviewSize(mPreviewWidth, mPreviewHeight);
            boundingBoxView.setObjects(objects);
            Log.e("模型执行时间（CPU）", SystemClock.elapsedRealtime() - currentTime + "");
            boolean containsAllLabels = containsSpecificLabels(objects);
            if (containsAllLabels) {
                shouldProcessFrames = false;
                for (YoloV5Ncnn.Obj obj : objects) {
                    if ("mouth".equals(obj.label)) {
                        float whRatio = obj.w / obj.h;
                        float mouthRatio = obj.h / mPreviewHeight;
                        if (whRatio >= 1.1) {
                            containsAllLabels = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hint.setTextColor(Color.YELLOW);
                                    hint.setText("请张大嘴！");
                                }
                            });
                            shouldProcessFrames = true;
                            return;
                        }
                        if (mouthRatio < 0.23) {
                            containsAllLabels = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hint.setTextColor(Color.GREEN);
                                    hint.setText("请靠近一点！");
                                }
                            });
                            shouldProcessFrames = true;
                            return;
                        }

                        Log.e("INFO", "W/H ratio for mouth: " + whRatio);
                        Log.e("INFO", "Mouth ratio: " + obj.h / mPreviewHeight);
                    }
                }

/////////////////////////////////////

                camera.stopPreview();
//            boolean containsAllLabels = false;
//                camera.stopPreview();
                Toast.makeText(TestCamera2.this, "识别成功,请放下并稍等！", Toast.LENGTH_SHORT).show();
                boundingBoxView.clearBoundingBoxes();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        hint.setTextColor(Color.BLUE);

                        ViewGroup.LayoutParams originalLayoutParams = hint.getLayoutParams();
                        ViewGroup.LayoutParams layoutParams = null;

                        // Create the appropriate layout parameters based on the parent layout type
                        if (originalLayoutParams instanceof FrameLayout.LayoutParams) {
                            layoutParams = new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.WRAP_CONTENT,
                                    FrameLayout.LayoutParams.WRAP_CONTENT,
                                    Gravity.CENTER
                            );
                        }
                        hint.setLayoutParams(layoutParams);
                        hint.setText("识别成功,请放下并稍等！");
                        // 2. Set the font size to 3 times the original size
                        float originalTextSize = hint.getTextSize();
                        hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSize * 2);

                        // 3. Make the text color blink between yellow and red
                        int yellowColor = Color.YELLOW;
                        int redColor = Color.RED;
                        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), yellowColor, redColor);
                        colorAnimator.setDuration(200); // The duration of the color transition
                        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
                        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
                        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                hint.setTextColor((Integer) animation.getAnimatedValue());
                            }
                        });

                        colorAnimator.start();

                    }
                });



                Location location1, location2, location3, location4;
                location1 = new Location(getHighestConfidenceObjectWithLabel(objects, "hand"));
                location2 = new Location(getSecondHighestConfidenceObjectWithLabel(objects, "hand"));
                location3 = new Location(getHighestConfidenceObjectWithLabel(objects, "eye"));
                location4 = new Location(getHighestConfidenceObjectWithLabel(objects, "mouth"));

                String str_base64 = bitmapToBase64(bitmap);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://47.101.206.135:8082/")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                float[][] loc = new float[4][4];
                loc[0] = getHighestConfidenceObjectWithLabelForAPI(objects, "hand");
                loc[1] = getSecondHighestConfidenceObjectWithLabelForAPI(objects, "hand");
                loc[2] = getHighestConfidenceObjectWithLabelForAPI(objects, "eye");
                loc[3] = getHighestConfidenceObjectWithLabelForAPI(objects, "mouth");
                ApiService apiService = retrofit.create(ApiService.class);
                Call<CameraData> call = apiService.cameraup(new PhotoData(str_base64, loc));

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        // Log.e("TAG111", "onGetCameraData:base64="+str_base64);
//                                call.enqueue(new Callback<CameraData>() {
//
//                                    @Override
//                                    public void onResponse(Call<CameraData> call, Response<CameraData> response) {
                        Response<CameraData> response = null;
                        try {
                            response = call.execute();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        //保存bitmap
                        try {
                            //将bitmap保存本地并输出路径
                            Uri imageUri = bitmapToUri(bitmap);
                            Log.e("TAG111", "uri: " + imageUri);
                            //手口眼疾病检测结果
                            int hand1 = response.body().getHand1();
                            int hand2 = response.body().getHand2();
                            int eye1 = response.body().getEye1();
                            int eye2 = response.body().getEye2();
                            int mouth = response.body().getMouth();
                            Log.e("TAG222", "hand1: " + hand1 + ",hand2:" + hand2 + ",eye1:" + eye1
                                    + ",eye2:" + eye2 + ",mouth:" + mouth);
                            int hand, eye;
                            if (hand1 == 1 && hand2 == 1) {
                                hand = 1;
                            } else {
                                hand = 0;
                            }
                            if (eye1 == 1 && eye2 == 1) {
                                eye = 1;
                            } else {
                                eye = 0;
                            }


                            //跳转和传值
                            Intent intent = new Intent(TestCamera2.this, ActivityMorning.class);
                            intent.putExtra("id", "87");
//                        Log.e("TAG000", "id: " + stu_id);
                            intent.putExtra("uri", imageUri.toString());
                            intent.putExtra("hand", hand);
                            intent.putExtra("eye", eye);
                            intent.putExtra("mouth", mouth);
                            intent.putExtra("location1", location1);
                            intent.putExtra("location2", location2);
                            intent.putExtra("location3", location3);
                            intent.putExtra("location4", location4);

                            startActivity(intent);
//                        if (toast != null)
//                            toast.cancel();
                            finish();
                        } catch (
                                Exception e) {
                            Log.e("Error", e.toString());
                        }
                    }
                }).start();
            }
            ///////////////////////////////////////////////////////////
            else {
                        if(hint.getText().toString().equals("请将手口眼置于框内！"))
                            return;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hint.setTextColor(Color.parseColor("#303F9F"));
                                hint.setText("请将手口眼置于框内！");
                            }
                        });

            }
//            boundingBoxView.drawBoundingBoxes();
//        Canvas canvas = mHolder.lockCanvas();
//        boundingBoxView.draw(canvas);
//        boundingBoxView.invalidate();
//        boundingBoxView.


//            flag = 0;
        }
    }
//    }


//    private void showObjects(YoloV5Ncnn.Obj[] objects, Bitmap bitmap)
//    {
////        if (objects == null)
////        {
////            imageView.setImageBitmap(bitmap);
////            return;
////        }
//
//        // draw objects on bitmap
//        Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//        final int[] colors = new int[] {
//                Color.rgb( 54,  67, 244),
//                Color.rgb( 99,  30, 233),
//                Color.rgb(176,  39, 156),
//                Color.rgb(183,  58, 103),
//                Color.rgb(181,  81,  63),
//                Color.rgb(243, 150,  33),
//                Color.rgb(244, 169,   3),
//                Color.rgb(212, 188,   0),
//                Color.rgb(136, 150,   0),
//                Color.rgb( 80, 175,  76),
//                Color.rgb( 74, 195, 139),
//                Color.rgb( 57, 220, 205),
//                Color.rgb( 59, 235, 255),
//                Color.rgb(  7, 193, 255),
//                Color.rgb(  0, 152, 255),
//                Color.rgb( 34,  87, 255),
//                Color.rgb( 72,  85, 121),
//                Color.rgb(158, 158, 158),
//                Color.rgb(139, 125,  96)
//        };
//
//        Canvas canvas = new Canvas(rgba);
//
////        Paint paint = new Paint();
////        paint.setStyle(Paint.Style.STROKE);
////        paint.setStrokeWidth(4);
////
////        Paint textbgpaint = new Paint();
////        textbgpaint.setColor(Color.WHITE);
////        textbgpaint.setStyle(Paint.Style.FILL);
////
////        Paint textpaint = new Paint();
////        textpaint.setColor(Color.BLACK);
////        textpaint.setTextSize(26);
////        textpaint.setTextAlign(Paint.Align.LEFT);
//
//        for (int i = 0; i < objects.length; i++)
//        {
//            paint.setColor(colors[i % 19]);
//
//            Log.e( "Result",Float.toString(objects[i].x)+ Float.toString(objects[i].y)+   Float.toString(objects[i].prob));
//            Log.e( "Result",objects[i].label);
//            // draw filled text inside image
////            {
////                String text = objects[i].label + " = " + String.format("%.1f", objects[i].prob * 100) + "%";
////
////                float text_width = textpaint.measureText(text);
////                float text_height = - textpaint.ascent() + textpaint.descent();
////
////                float x = objects[i].x;
////                float y = objects[i].y - text_height;
////                if (y < 0)
////                    y = 0;
////                if (x + text_width > rgba.getWidth())
////                    x = rgba.getWidth() - text_width;
////
////                canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint);
////
////                canvas.drawText(text, x, y - textpaint.ascent(), textpaint);
////            }
//        }
//
////        imageView.setImageBitmap(rgba);
//    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        mCamera.setPreviewCallback(this);
        mCamera.startPreview();
//        Log.e("相机--》surfaceview", "width--" + width);
//        Log.e("相机--》surfaceview", "height--" + height);
//        Log.e("相机--》mCamera", "width--" + mCamera.getParameters().getPreviewSize().width);
//        Log.e("相机--》mCamera", "height--" +  mCamera.getParameters().getPreviewSize().height);

    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mCamera != null) {
//            mCamera.setPreviewCallback(null);
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera = null;
//        }
//        mInferenceHandler.removeCallbacksAndMessages(null);
//        mInferenceThread.quitSafely();
//        GpioUtils.writeGpioValue(139, "1");
//        CameraPreviewManager.getInstance().stopPreview();
//        try {
//            mInferenceThread.join();
//            mInferenceThread = null;
//            mInferenceHandler = null;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
//        mInferenceHandler.removeCallbacksAndMessages(null);
//        mInferenceThread.quitSafely();
        GpioUtils.writeGpioValue(139, "1");
//        try {
//            mInferenceThread.join();
//            mInferenceThread = null;
//            mInferenceHandler = null;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        yolov5ncnn.Deinit();
        yolov5ncnn = null;
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        GpioUtils.writeGpioValue(139, "1");
        Log.e("TAG", "onDestroy: ..");
//        GpioUtils.writeGpioValue(index,"1");
//        CameraPreviewManager.getInstance().stopPreview();

    }

}
