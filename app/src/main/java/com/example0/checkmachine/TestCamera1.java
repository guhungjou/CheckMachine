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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
import com.example0.DashedRectSurfaceView;
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

public class TestCamera1 extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private SurfaceView svContent;
    private int mPreviewWidth;
    private int mPreviewHeight;
    private TextView hint;
    private OkHttpClient client;
    private TestCamera1 mContent;

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

    //    SoundPool soundPool1 = new SoundPool.Builder().build();
//    SoundPool soundPool2 = new SoundPool.Builder().build();
    int soundId2;
    View im1;

    View im2;
    private DashedRectSurfaceView dashedRectSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        CameraPreviewManager.getInstance().stopPreview();
        super.onCreate(savedInstanceState);
//        FaceSDKManager.getInstance().initDataBases(this);
        setContentView(R.layout.testnircamera);
        //界面初始化
        initUI();
        openlight();
        mContent = this;
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(2).build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        int soundId = soundPool.load(mContent, R.raw.e_h, 1);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    int streamId = soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);

                    // 播放完成后释放资源
                    int duration = 3000; // 替换为音频文件的实际持续时间（毫秒）
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            soundPool.stop(streamId);
                            soundPool.release();
                        }
                    }, duration);
                }
            }
        });
        dashedRectSurfaceView = findViewById(R.id.dashedRectSurfaceView);

        im1 = findViewById(R.id.im1);
        im2 = findViewById(R.id.im2);
        im2.setVisibility(View.INVISIBLE);
//        dashedRectSurfaceView.setVisibility(View.VISIBLE);
        dashedRectSurfaceView.setVisibility(View.INVISIBLE);

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

    MediaPlayer mediaPlayer1;
    MediaPlayer mediaPlayer2;

    private void initUI() {
        ImageButton backButton = findViewById(R.id.back_button);

//        dashedRectSurfaceView.setVisibility(View.VISIBLE);
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
//        mediaPlayer1= MediaPlayer.create(this, R.raw.e_h);
//        mediaPlayer2= MediaPlayer.create(this, R.raw.m);

        hint = findViewById(R.id.check_hint);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hint.setTextColor(Color.GREEN);
                hint.setText("检测手、眼！");
//                mediaPlayer1.start();
            }
        });

        SurfaceHolder holder = svContent.getHolder();

        boolean inUse = isCameraInUse(0);
        if (inUse) {
            Log.e("MainActivity", "Camera is in use");
//            SystemClock.sleep(2000);
        } else {
            Log.e("MainActivity", "Camera is not in use");
        }
    }

    Intent intent = null;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        intent = new Intent(TestCamera1.this, ActivityMorning.class);
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

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();

    }

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


    private Uri bitmapToUri(Bitmap bitmap, int flag) {
        File imageTemp = new File(getExternalCacheDir(), "imageOut" + flag + ".jpg");
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

        return mouthCount >= 1 && eyeCount >= 1 && handCount >= 2;
    }

    public boolean containsMouthLabels(YoloV5Ncnn.Obj[] objects) {
        long mouthCount = Arrays.stream(objects)
                .filter(obj -> obj.label.equals("mouth"))
                .count();

        return mouthCount >= 1;
    }

    Boolean first = false;
    private boolean shouldProcessFrames = true;

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
//        dashedRectSurfaceView.drawDashedRect();
        Log.d("TAG", "onPreviewFrame called");
        if (!shouldProcessFrames) {
            return;
        }
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastDetectionTime >= DETECTION_INTERVAL) {
            lastDetectionTime = SystemClock.elapsedRealtime();

            Camera.Size size = camera.getParameters().getPreviewSize();
            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
            byte[] byteArray = baos.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

//            Log.e("TestCamera", "bitmap: " + bitmap.getWidth() + " " + bitmap.getHeight() + " " + bitmap.getByteCount());
            YoloV5Ncnn.Obj[] objects = yolov5ncnn.Detect(bitmap, false);
            YoloV5Ncnn.Obj[] mObjects;
            if (!first) {
                mObjects = Arrays.stream(objects).filter(obj -> !obj.label.equals("mouth")).toArray(YoloV5Ncnn.Obj[]::new);
            } else {
                mObjects = Arrays.stream(objects)
                        .filter(obj -> !obj.label.equals("hand") && !obj.label.equals("eye"))
                        .toArray(YoloV5Ncnn.Obj[]::new);
            }
//            Arrays.stream(objects).forEach(obj -> Log.e("LABEL","Label: " + obj.label+" Confidence: " + obj.prob));
            boundingBoxView.setPreviewSize(mPreviewWidth, mPreviewHeight);
            boundingBoxView.setObjects(mObjects);
            Log.e("模型执行时间（CPU）", SystemClock.elapsedRealtime() - currentTime + "");
            boolean containsAllLabels = containsSpecificLabels(objects);
            if (!first && containsAllLabels) {
                shouldProcessFrames = false;
                for (YoloV5Ncnn.Obj obj : objects) {
                    if ("mouth".equals(obj.label)) {
                        float whRatio = obj.w / obj.h;
                        float mouthRatio = obj.h / mPreviewHeight;
//                        if (whRatio >= 1.1) {
//                            containsAllLabels = false;
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    hint.setTextColor(Color.YELLOW);
//                                    hint.setText("请张大嘴！");
//                                }
//                            });
//                            shouldProcessFrames = true;
//                            return;
//                        }
                        if (mouthRatio < 0.1) {
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

//                        Log.e("INFO", "W/H ratio for mouth: " + whRatio);
//                        Log.e("INFO", "Mouth ratio: " + obj.h / mPreviewHeight);
                    }
                }

/////////////////////////////////////

                camera.stopPreview();
//            boolean containsAllLabels = false;
//                camera.stopPreview();
                Toast.makeText(TestCamera1.this, "识别成功,请检查口部！", Toast.LENGTH_SHORT).show();
                boundingBoxView.clearBoundingBoxes();
                dashedRectSurfaceView.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams originalLayoutParams = hint.getLayoutParams();
                float originalTextSize = hint.getTextSize();
                int yellowColor = Color.YELLOW;
                int redColor = Color.RED;
                im1.setVisibility(View.INVISIBLE);
                im2.setVisibility(View.VISIBLE);
                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), yellowColor, redColor);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        hint.setTextColor(Color.BLUE);


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
                        hint.setText("识别成功,请检查口部！");
                        // 2. Set the font size to 3 times the original size

                        hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSize * 2);

                        // 3. Make the text color blink between yellow and red

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

//                mediaPlayer2.start();
//                soundPool2.play(soundId2, 1.0f, 1.0f, 0, 0, 1.0f);
                SoundPool soundPool;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    soundPool = new SoundPool.Builder().setMaxStreams(2).build();
                } else {
                    soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
                }

                int soundId = soundPool.load(mContent, R.raw.m, 1);

                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        if (status == 0) {
                            int streamId = soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);

                            // 播放完成后释放资源
                            int duration = 3500; // 替换为音频文件的实际持续时间（毫秒）
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    soundPool.stop(streamId);
                                    soundPool.release();
                                }
                            }, duration);
                        }
                    }
                });

                Location location1, location2, location3, location4;
                location1 = new Location(getHighestConfidenceObjectWithLabel(objects, "hand"));
                location2 = new Location(getSecondHighestConfidenceObjectWithLabel(objects, "hand"));
                location3 = new Location(getHighestConfidenceObjectWithLabel(objects, "eye"));
//                location4 = new Location(getHighestConfidenceObjectWithLabel(objects, "mouth"));

                String str_base64 = bitmapToBase64(bitmap);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://47.101.206.135:8078/")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                float[][] loc = new float[3][4];
                loc[0] = getHighestConfidenceObjectWithLabelForAPI(objects, "hand");
                loc[1] = getSecondHighestConfidenceObjectWithLabelForAPI(objects, "hand");
                loc[2] = getHighestConfidenceObjectWithLabelForAPI(objects, "eye");
//                loc[3] = getHighestConfidenceObjectWithLabelForAPI(objects, "mouth");
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
                            Uri imageUri = bitmapToUri(bitmap, 1);
                            Log.e("TAG111", "uri: " + imageUri);
                            //手口眼疾病检测结果
                            int hand1 = response.body().getHand1();
                            int hand2 = response.body().getHand2();
                            int eye1 = response.body().getEye1();
                            int eye2 = response.body().getEye2();
//                            int mouth = response.body().getMouth();
                            Log.e("TAG222", "hand1: " + hand1 + ",hand2:" + hand2 + ",eye1:" + eye1
                                    + ",eye2:" + eye2);
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

                            intent.putExtra("id", "87");
//                        Log.e("TAG000", "id: " + stu_id);
                            intent.putExtra("uri1", imageUri.toString());
                            intent.putExtra("hand", hand);
                            intent.putExtra("eye", eye);
//                            intent.putExtra("mouth", mouth);
                            intent.putExtra("location1", location1);
                            intent.putExtra("location2", location2);
                            intent.putExtra("location3", location3);
//                            intent.putExtra("location4", location4);
                            first = true;
//                            startActivity(intent);
//                        if (toast != null)
//                            toast.cancel();
//                            finish();
//                            camera.release();
                            mCamera.release();
                            mCamera = null;
                            mCamera = Camera.open(0); // 切换到ID为0的摄像头
                            Camera.Parameters parameters = mCamera.getParameters();
// 配置预览参数
                            shouldProcessFrames = true;
                            parameters.setPreviewSize(mPreviewWidth, mPreviewHeight);
                            mCamera.setParameters(parameters);
                            try {
                                mCamera.setPreviewDisplay(mHolder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            mCamera.setPreviewDisplay(mHolder);
//                            mCamera.setPreviewCallback(this);
// 开始新摄像头的预览
                            mCamera.setPreviewCallback(mContent);
                            mCamera.startPreview();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hint.setLayoutParams(originalLayoutParams);
                                    hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSize);
                                    colorAnimator.cancel();
                                }
                            });


                        } catch (
                                Exception e) {
                            Log.e("Error", e.toString());
                        }
                    }
                }).start();
            }
            ///////////////////////////////////////////////////////////
            else if (first && containsMouthLabels(objects)) {
                shouldProcessFrames = false;
                for (YoloV5Ncnn.Obj obj : objects) {
                    if ("mouth".equals(obj.label)) {
                        float whRatio = obj.w / obj.h;
                        float mouthRatio = obj.h / mPreviewHeight;
                        Log.e("INFO", "W/H ratio for mouth: " + whRatio);
                        Log.e("INFO", "Mouth ratio: " + obj.h / mPreviewHeight);
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

////
                    }
                }

/////////////////////////////////////

                camera.stopPreview();
//            boolean containsAllLabels = false;
//                camera.stopPreview();
                Toast.makeText(TestCamera1.this, "识别成功,流程结束！", Toast.LENGTH_SHORT).show();
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
                        hint.setText("识别成功,流程结束！");
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


                Location location4;
                location4 = new Location(getHighestConfidenceObjectWithLabel(objects, "mouth"));

                String str_base64 = bitmapToBase64(bitmap);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://47.101.206.135:8079/")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                float[][] loc = new float[1][4];
//                loc[0] = getHighestConfidenceObjectWithLabelForAPI(objects, "hand");
//                loc[1] = getSecondHighestConfidenceObjectWithLabelForAPI(objects, "hand");
//                loc[2] = getHighestConfidenceObjectWithLabelForAPI(objects, "eye");
                loc[0] = getHighestConfidenceObjectWithLabelForAPI(objects, "mouth");
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
                            Uri imageUri2 = bitmapToUri(bitmap, 2);
                            Log.e("TAG111", "uri: " + imageUri2);
                            //手口眼疾病检测结果
//                            int hand1 = response.body().getHand1();
//                            int hand2 = response.body().getHand2();
//                            int eye1 = response.body().getEye1();
//                            int eye2 = response.body().getEye2();
                            int mouth = response.body().getMouth();
//                            Log.e("TAG222", "hand1: " + hand1 + ",hand2:" + hand2 + ",eye1:" + eye1
//                                    + ",eye2:" + eye2);
//                            int hand, eye;
//                            if (hand1 == 1 && hand2 == 1) {
//                                hand = 1;
//                            } else {
//                                hand = 0;
//                            }
//                            if (eye1 == 1 && eye2 == 1) {
//                                eye = 1;
//                            } else {
//                                eye = 0;
//                            }


                            //跳转和传值
//                            Intent intent = new Intent(TestCamera1.this, ActivityMorning.class);
//                            intent.putExtra("id", "87");
//                        Log.e("TAG000", "id: " + stu_id);
                            intent.putExtra("uri2", imageUri2.toString());
//                            intent.putExtra("hand", hand);
//                            intent.putExtra("eye", eye);
//                            intent.putExtra("mouth", mouth);
//                            intent.putExtra("location1", location1);
//                            intent.putExtra("location2", location2);
//                            intent.putExtra("location3", location3);
                            intent.putExtra("location4", location4);
//                            first = true;
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
            } else {
//                if (hint.getText().toString().equals("请将手口眼置于框内！"))
//                    return;
                if (first == false) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hint.setTextColor(Color.parseColor("#303F9F"));
                            hint.setText("检测手、眼！");
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hint.setTextColor(Color.parseColor("#303F9F"));
                            hint.setText("检测口部！");
                        }
                    });
                }

            }
        }
    }

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
