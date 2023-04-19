package com.example0.checkmachine;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Base64;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.FileProvider;

import com.baidu.idl.main.facesdk.FaceInfo;
import com.baidu.idl.main.facesdk.attendancelibrary.callback.CameraDataCallback;
import com.baidu.idl.main.facesdk.attendancelibrary.callback.FaceDetectCallBack;
import com.baidu.idl.main.facesdk.attendancelibrary.camera.AutoTexturePreviewView;
import com.baidu.idl.main.facesdk.attendancelibrary.listener.SdkInitListener;
import com.baidu.idl.main.facesdk.attendancelibrary.manager.FaceSDKManager;
import com.baidu.idl.main.facesdk.attendancelibrary.model.LivenessModel;
import com.baidu.idl.main.facesdk.attendancelibrary.model.SingleBaseConfig;
import com.baidu.idl.main.facesdk.attendancelibrary.utils.FaceOnDrawTexturViewUtil;
import com.baidu.idl.main.facesdk.attendancelibrary.utils.FileUtils;
import com.baidu.idl.main.facesdk.attendancelibrary.utils.ToastUtils;
import com.baidu.idl.main.facesdk.attendancelibrary.camera.CameraPreviewManager;
import com.example0.BaseActivity;
import com.example0.datalibrary.model.User;
import com.example0.retrofit.Location;
import com.example0.util.GpioUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class FaceCheck extends BaseActivity {


    private String stu_id = "-1";
    private TextureView mDrawDetectFaceView;
    private AutoTexturePreviewView mAutoCameraPreviewView;
    private RectF rectF;
    private Paint paint;
    private Paint paintBg;
    private User mUser;
    private ImageView imageView;

//    private YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();

    // 图片越大，性能消耗越大，也可以选择640*480， 1280*720
//    private static final int PREFER_WIDTH = SingleBaseConfig.getBaseConfig().getRgbAndNirWidth();
//    private static final int PERFER_HEIGH = SingleBaseConfig.getBaseConfig().getRgbAndNirHeight();
//    private static final int PREFER_WIDTH = 640;
//    private static final int PERFER_HEIGH = 480;
    private static final int PREFER_WIDTH = 1280;
    private static final int PERFER_HEIGH = 720;
    private Context mContext;
    private Uri imageUri;

    public OkHttpClient client;

    //相机预览数据byte[]转bitmap耗时优化方案对象
    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
    private Type.Builder yuvType;
    private Type.Builder rgbaType;
    private Allocation in;
    private Allocation out;

    // private  boolean retrofit_flag=false;
    private Object location = null;
    private Bitmap bit;
    private int hand = 0;
    private TextView hint;
    private int eye = 0;
    private int mouth = 0;
    //创建4个坐标对象
    Location location1, location2, location3, location4;
    private long time;
    private int index = 139;
    private YoloV5Ncnn yolov5ncnn ;
    boolean ret_init= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        SoundPool soundPool;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            soundPool = new SoundPool.Builder().setMaxStreams(2).build();
//        } else {
//            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
//        }

//        int soundId = soundPool.load(this, R.raw.rlsb, 1);
//
//        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                if (status == 0) {
//                    int streamId = soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
//
//                    // 播放完成后释放资源
//                    int duration = 1000; // 替换为音频文件的实际持续时间（毫秒）
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            soundPool.stop(streamId);
//                            soundPool.release();
//                        }
//                    }, duration);
//                }
//            }
//        });
        initListener();
        FaceSDKManager.getInstance().initDataBases(this);
        setContentView(R.layout.activity_facecheck);
    yolov5ncnn=new YoloV5Ncnn();

//    ret_init = yolov5ncnn.Init(getAssets());

        openlight();
        //界面初始化
        initUI();
        initRender();
        client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .build();

        time = System.currentTimeMillis();
//        startTestOpenDebugRegisterFunction();
    }

    private void openlight() {
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


    private void initRender() {
        rs = RenderScript.create(this);
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
    }

    private void initUI() {
        // 画人脸框
        rectF = new RectF();
        paint = new Paint();
        paintBg = new Paint();
        hint = findViewById(R.id.check_hint);
        mDrawDetectFaceView = findViewById(R.id.draw_detect_face_view);
        mDrawDetectFaceView.setOpaque(false);
        mDrawDetectFaceView.setKeepScreenOn(true);
        if (SingleBaseConfig.getBaseConfig().getRgbRevert()) {
            mDrawDetectFaceView.setRotationY(180);
        }
        // 单目摄像头RGB 图像预览
        mAutoCameraPreviewView = findViewById(R.id.auto_camera_preview_view);

        //测试获取图
//        imageView = findViewById(R.id.iv_testphoto);
    }


    @Override
    protected void onResume() {
        super.onResume();

        startTestOpenDebugRegisterFunction();
        // mAutoCameraPreviewView.getTextureView().setSurfaceTextureListener(this);
    }

    //    private Bitmap convertBitmap(byte[] data) {
//        try {
//            YuvImage img = new YuvImage(data, ImageFormat.NV21, PREFER_WIDTH, PERFER_HEIGH, null);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
//            img.compressToJpeg(new Rect(0, 0, PREFER_WIDTH, PERFER_HEIGH), 100, baos);
//            byte[] bytes = baos.toByteArray();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            baos.close();
//            return bitmap ;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
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

    private Bitmap convertBitmap4RenderScript(byte[] data) {
        if (yuvType == null) {
            yuvType = new Type.Builder(rs, Element.U8(rs)).setX(data.length);
            in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);
        }

        if (rgbaType == null) {
            rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(PREFER_WIDTH).setY(PERFER_HEIGH);
            out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
        }

        in.copyFrom(data);
        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);

        Bitmap bitmap = Bitmap.createBitmap(PREFER_WIDTH, PERFER_HEIGH, Bitmap.Config.ARGB_8888);
        out.copyTo(bitmap);

        return bitmap;
    }

    private static Toast toast;

    private boolean isAppInstalled(Context context, String packagename)
    {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        }catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo ==null){
            //没有安装
            return false;
        }else{
            //已经安装
            return true;
        }
    }


    private void startTestOpenDebugRegisterFunction() {

        Log.e("cameraId", SingleBaseConfig.getBaseConfig().getRBGCameraId() + "");
        //  CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_USB);
        if (SingleBaseConfig.getBaseConfig().getRBGCameraId() != -1) {
            CameraPreviewManager.getInstance().setCameraFacing(SingleBaseConfig.getBaseConfig().getRBGCameraId());
//            Log.e("cameraId", SingleBaseConfig.getBaseConfig().getRBGCameraId() + "");
        } else {
            CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_FACING_FRONT);
        }


//        CameraPreviewManager.getInstance().setDisplayOrientation();
//         CameraPreviewManager.getInstance().setCameraFacing(0);
//        Log.e("11111111111111","1111");
//        mAutoCameraPreviewView.setRotationX(180);
//        mDrawDetectFaceView.setRotation(90);
        CameraPreviewManager.getInstance().startPreview(mContext, mAutoCameraPreviewView,
                PREFER_WIDTH, PERFER_HEIGH, new CameraDataCallback() {
                    @Override
                    public void onStopPreview()  {
                    }

                    @Override
                    public void onGetCameraData(byte[] data, Camera camera, int width, int height) throws IOException {

                        if (Objects.equals(stu_id, "-1"))
                            FaceSDKManager.getInstance().onDetectCheck(data, null, null,
                                    height, width, 0, new FaceDetectCallBack() {
                                        @Override
                                        public void onFaceDetectCallback(LivenessModel livenessModel) {
                                            // 预览模式
                                            User user = livenessModel.getUser();
                                            if (user == null) {
                                                mUser = null;
                                                if (livenessModel.isMultiFrame()) {

                                                    Log.e("TAG", "未检测到注册用户！");

                                                }
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hint.setTextColor(Color.RED);
                                                        hint.setText("未检测到注册用户！");
                                                    }
                                                });
                                            } else {
                                                mUser = user;
//                    String absolutePath = FileUtils.getBatchImportSuccessDirectory()
//                            + "/" + user.getImageName();
//                    Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
//                    nameImage.setImageBitmap(bitmap);
//                    Toast.makeText(FaceCheck.this,FileUtils.spotString(user.getUserName()) + "成功",Toast.LENGTH_SHORT).show();

                                                //获取识别成功的用户id
                                                String idStr = FileUtils.spotString(user.getUserName());
                                                stu_id = idStr;
                                                Log.e("TAG", stu_id + "欢迎您，请晨检！");
//                                                    runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            if (toast == null) {
//                                                                toast = Toast.makeText(FaceCheck.this, "欢迎您，请晨检！", Toast.LENGTH_SHORT);
//                                                            } else {
//                                                                toast.cancel();
//                                                                toast = Toast.makeText(FaceCheck.this, "欢迎您，请晨检！", Toast.LENGTH_SHORT);
//                                                            }
//                                                            toast.show();
//                                                        }
//                                                    });
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hint.setTextColor(Color.YELLOW);
                                                        hint.setText("欢迎您，请晨检！");
                                                    }
                                                });
                                            }

//                                        // 开发模式
//                                        checkOpenDebugResult(livenessModel);
//
//                                        if (isSaveImage){
//                                            SaveImageManager.getInstance().saveImage(livenessModel);
//                                        }
                                        }

                                        @Override
                                        public void onTip(int code, String msg) {

                                        }

                                        @Override
                                        public void onFaceDetectDarwCallback(LivenessModel livenessModel) {
                                            // 绘制人脸框
                                            // showFrame(livenessModel);


                                        }
                                    });

                        if (!Objects.equals(stu_id, "-1")) {
//                            CameraPreviewManager.getInstance().stopPreview();
////                            CameraPreviewManager.getInstance().
//                            Intent intent = new Intent(mContext, TestCamera.class);
//                            intent.putExtra("id", stu_id);
//                            startActivity(intent);
//                            finish();
                            CameraPreviewManager.getInstance().stopPreview();
//                            Intent intent = new Intent(mContext, CustomPatternActivity.class);
////                                intent.putExtra("id", stu_id);
//                            startActivity(intent);
//                            CameraPreviewManager.releaseInstance();
//                            SingleBaseConfig.release();
                            if(isAppInstalled(mContext, "com.tencent.yolov5ncnn")){
                                Intent i = new Intent("com.tencent.yolov5ncnn.action.TEST_CAMERA");
                                ComponentName cn = new ComponentName("com.tencent.yolov5ncnn",
                                        "com.tencent.yolov5ncnn.TestCamera1");
                                i.setComponent(cn);
                                i.putExtra("id", stu_id);
                                startActivityForResult(i, RESULT_OK);
                            }
//                            finish();
//                            return;
//                            runOnUiThread(
//                                    new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            CameraPreviewManager.getInstance().stopPreview();
//                                            Intent intent = new Intent(mContext, TestCamera.class);
//                                            intent.putExtra("id", stu_id);
//                                            startActivity(intent);
//                                        }
//                                    }
//                            );

//                            finish();
//                            time = System.currentTimeMillis();
//                                if (location != null) {

                            // 摄像头预览数据进行人脸检测
//                                FaceSDKManager.getInstance().onDetectCheck(data, null, null,
//                                        height, width, 0, new FaceDetectCallBack() {
//                                            @Override
//                                            public void onFaceDetectCallback(LivenessModel livenessModel) {
//                                                // 预览模式
//                                                checkCloseDebugResult(livenessModel);
//
////                                        // 开发模式
////                                        checkOpenDebugResult(livenessModel);
////
////                                        if (isSaveImage){
////                                            SaveImageManager.getInstance().saveImage(livenessModel);
////                                        }
//                                            }
//
//                                            @Override
//                                            public void onTip(int code, String msg) {
//
//                                            }
//
//                                            @Override
//                                            public void onFaceDetectDarwCallback(LivenessModel livenessModel) {
//                                                // 绘制人脸框
//                                                // showFrame(livenessModel);
//
//
//                                            }
//                                        });
//                            }

//                            if (location == null) {

                            //Bitmap cameraData =  convertBitmap(data);
//                                //  Log.e("TAG", "onGetCameraData:base64="+str_base64);
//                                // PhotoData photoData =new PhotoData("1222222222222222222222222222222");
//                                Bitmap cameraData = convertBitmap4RenderScript(data);
//                                runOnUiThread(() -> {

//                                    // The new size we want to scale to
//                                    final int REQUIRED_SIZE = 640;
//
//                                    // Find the correct scale value. It should be the power of 2.
//                                    int width_tmp = PREFER_WIDTH, height_tmp = PERFER_HEIGH;
//                                    int scale = 1;
//                                    while (true) {
//                                        if (width_tmp / 2 < REQUIRED_SIZE
//                                                || height_tmp / 2 < REQUIRED_SIZE) {
//                                            break;
//                                        }
//                                        width_tmp /= 2;
//                                        height_tmp /= 2;
//                                        scale *= 2;
//                                    }
//
//                                    // Decode with inSampleSize
//                                    BitmapFactory.Options o2 = new BitmapFactory.Options();
//                                    o2.inSampleSize = scale;
//                                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
//
//                                    // Rotate according to EXIF
//                                    int rotate = 0;
//                                    Matrix matrix = new Matrix();
////                                    matrix.postRotate(rotate);
//                                    Bitmap b=Bitmap.createBitmap(cameraData, 0, 0, 640, 640, null, true);
//                                    Log.e("Info", "bitmap: " + b.getWidth() + " " + b.getHeight() + " " + b.getByteCount());
//                                    Uri selectedImage= Uri.parse("content://media/external/images/media/712");
//                                    BitmapFactory.Options o = new BitmapFactory.Options();
//                                    o.inJustDecodeBounds = true;
//                                    try {
//                                        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
//                                    } catch (FileNotFoundException e) {
//                                        throw new RuntimeException(e);
//                                    }
//
//                                    // The new size we want to scale to
//                                    final int REQUIRED_SIZE = 640;
//
//                                    // Find the correct scale value. It should be the power of 2.
//                                    int width_tmp = o.outWidth, height_tmp = o.outHeight;
//                                    int scale = 1;
//                                    while (true) {
//                                        if (width_tmp / 2 < REQUIRED_SIZE
//                                                || height_tmp / 2 < REQUIRED_SIZE) {
//                                            break;
//                                        }
//                                        width_tmp /= 2;
//                                        height_tmp /= 2;
//                                        scale *= 2;
//                                    }
//
//                                    // Decode with inSampleSize
//                                    BitmapFactory.Options o2 = new BitmapFactory.Options();
//                                    o2.inSampleSize = scale;
//                                    Bitmap bitmap= null;
//                                    try {
//                                         bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
//                                    } catch (FileNotFoundException e) {
//                                        throw new RuntimeException(e);
//                                    }
//
//                                    // Rotate according to EXIF
//                                    int rotate = 0;
//                                    try
//                                    {
//                                        ExifInterface exif = new ExifInterface(getContentResolver().openInputStream(selectedImage));
//                                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                                        switch (orientation) {
//                                            case ExifInterface.ORIENTATION_ROTATE_270:
//                                                rotate = 270;
//                                                break;
//                                            case ExifInterface.ORIENTATION_ROTATE_180:
//                                                rotate = 180;
//                                                break;
//                                            case ExifInterface.ORIENTATION_ROTATE_90:
//                                                rotate = 90;
//                                                break;
//                                        }
//                                    }
//                                    catch (IOException e)
//                                    {
//                                        Log.e("MainActivity", "ExifInterface IOException");
//                                    }
//
//                                    Matrix matrix = new Matrix();
//                                    matrix.postRotate(rotate);
//                                    Bitmap b=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                                    Log.e("Info", "bitmap: " + cameraData.getWidth() + " " + cameraData.getHeight() + " " + cameraData.getByteCount());
//                                    try{
//                                        YoloV5Ncnn.Obj[] objects = yolov5ncnn.Detect(cameraData, false);
//                                        Log.e("result", Arrays.toString(objects));
//                                    }catch (Exception e){
//                                        e.printStackTrace();
//                                    }


//
//                                showObjects(objects, cameraData);
//                                });

//                                String str_base64 = bitmapToBase64(cameraData);
//                                Retrofit retrofit = new Retrofit.Builder()
//                                        .baseUrl("http://47.101.206.135:8080/")
//                                        .client(client)
//                                        .addConverterFactory(GsonConverterFactory.create())
//                                        .build();
//
//                                ApiService apiService = retrofit.create(ApiService.class);
//                                Call<CameraData> call = apiService.cameraup(new PhotoData(str_base64));
//
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//
//                                        // Log.e("TAG111", "onGetCameraData:base64="+str_base64);
////                                call.enqueue(new Callback<CameraData>() {
////
////                                    @Override
////                                    public void onResponse(Call<CameraData> call, Response<CameraData> response) {
//                                        Response<CameraData> response = null;
//                                        try {
//                                            response = call.execute();
//                                            Log.e("TAG111", "onResponse-body: " + response.body().getLocation());
//
//                                            location = response.body().getLocation();
//                                            if (location == null) {
//                                                int flag = response.body().getHand1();
//                                                if (flag == -2) {
//                                                    runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            hint.setTextColor(Color.RED );
//                                                            hint.setText("请靠近一点！");
//                                                        }
//                                                    });
//                                                }
//                                                if (flag == -3) {
//                                                    runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            hint.setTextColor(Color.RED);
//                                                            hint.setText("请张大嘴巴！");
//                                                        }
//                                                    });
//                                                }
//                                                if (!hint.getText().equals("请将手口眼置于框内！")&&(flag!=-2&&flag!=-3)) {
//                                                    Timer timer = new Timer();
//                                                    timer.schedule(new TimerTask() {
//                                                        @Override
//                                                        public void run() {
//                                                            runOnUiThread(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    hint.setTextColor(Color.parseColor("#303F9F"));
//                                                                    hint.setText("请将手口眼置于框内！");
//                                                                }
//                                                            });
//                                                        }
//                                                    }, 3000);
//                                                }
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                            Log.e("TAG", "onResponse: " + toast);
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    if (toast == null) {
//                                                        toast = Toast.makeText(FaceCheck.this, "网络或者服务器错误", Toast.LENGTH_SHORT);
//                                                    } else {
//                                                        toast.cancel();
//                                                        toast = Toast.makeText(FaceCheck.this, "网络或者服务器错误", Toast.LENGTH_SHORT);
//                                                    }
//                                                    toast.show();
//                                                }
//                                            });
//
////            Toast.makeText(FaceCheck.this, "网络或者服务器错误", Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                        //保存bitmap
//                                        try {
//                                            if (location != null) {
//                                                bit = cameraData;
//                                                //将bitmap保存本地并输出路径
//                                                imageUri = bitmapToUri(bit);
//                                                Log.e("TAG111", "uri: " + imageUri);
//                                                //手口眼疾病检测结果
//                                                int hand1 = response.body().getHand1();
//                                                int hand2 = response.body().getHand2();
//                                                int eye1 = response.body().getEye1();
//                                                int eye2 = response.body().getEye2();
//                                                mouth = response.body().getMouth();
//                                                Log.e("TAG222", "hand1: " + hand1 + ",hand2:" + hand2 + ",eye1:" + eye1
//                                                        + ",eye2:" + eye2 + ",mouth:" + mouth);
//                                                if (hand1 == 1 && hand2 == 1) {
//                                                    hand = 1;
//                                                } else {
//                                                    hand = 0;
//                                                }
//                                                if (eye1 == 1 && eye2 == 1) {
//                                                    eye = 1;
//                                                } else {
//                                                    eye = 0;
//                                                }
//                                                //手口眼分割坐标
//                                                // location = response.body().getLocation();
//                                                int[][] arr = (int[][]) location;
//
//                                                location1 = new Location(arr[0][1], arr[0][0], (arr[0][3] - arr[0][1]), (arr[0][2] - arr[0][0]));
//                                                location2 = new Location(arr[1][1], arr[1][0], (arr[1][3] - arr[1][1]), (arr[1][2] - arr[1][0]));
//                                                location3 = new Location(arr[2][1], arr[2][0], (arr[2][3] - arr[2][1]), (arr[2][2] - arr[2][0]));
//                                                location4 = new Location(arr[3][1], arr[3][0], (arr[3][3] - arr[3][1]), (arr[3][2] - arr[3][0]));
//
//
//                            runOnUiThread(() -> {
                            //跳转和传值
//                            CameraPreviewManager.getInstance().stopPreview();
//                            Intent intent = new Intent(mContext, TestCamera.class);
//                            intent.putExtra("id", stu_id);
//                            startActivity(intent);
//                            finish();
//                            });


                            //沉睡2秒
//                                                try {
//                                                    Thread.sleep(2000);
//                                                } catch (InterruptedException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                                //跳转和传值
//                                                Intent intent = new Intent(FaceCheck.this, TestCamera.class);
//                                                intent.putExtra("id", stu_id);
//                            Log.e("TAG000", "id: " + stu_id);
//                                                intent.putExtra("uri", imageUri.toString());
//                                                intent.putExtra("hand", hand);
//                                                intent.putExtra("eye", eye);
//                                                intent.putExtra("mouth", mouth);
//                                                intent.putExtra("location1", location1);
//                                                intent.putExtra("location2", location2);
//                                                intent.putExtra("location3", location3);
//                                                intent.putExtra("location4", location4);

//                                                startActivity(intent);
//                                                if (toast != null)
//                                                    toast.cancel();
//                                                finish();
//
//                                            }
//                                        }catch (Exception e){
//                                            Log.e("Error",e.toString());
//                                        }
//                                    }
//                                }).start();

//                            }

//                                    @Override
//                                    public void onFailure(Call<CameraData> call, Throwable t) {
//                                        Log.e("TAG", "onResponse: " + t);
//                                        Toast.makeText(FaceCheck.this, "网络或者服务器错误", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }


                        }
                    }


                });

        Log.e("TAG", "onResponse");
    }

    private void checkCloseDebugResult(final LivenessModel livenessModel) {
        // 当未检测到人脸UI显示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (livenessModel == null) {

                    return;
                }
                User user = livenessModel.getUser();
                if (user == null) {
                    mUser = null;
                    if (livenessModel.isMultiFrame()) {

                        Log.e("TAG", "失败");

                    }
                } else {
                    mUser = user;
//                    String absolutePath = FileUtils.getBatchImportSuccessDirectory()
//                            + "/" + user.getImageName();
//                    Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
//                    nameImage.setImageBitmap(bitmap);
//                    Toast.makeText(FaceCheck.this,FileUtils.spotString(user.getUserName()) + "成功",Toast.LENGTH_SHORT).show();

                    //获取识别成功的用户id
                    String idStr = FileUtils.spotString(user.getUserName());
                    stu_id = idStr;
                    Log.e("TAG", stu_id + "成功");

//                    //获取送检照片
//                    Bitmap bitmap =null;
//                    BDFaceImageInstance image = livenessModel.getBdFaceImageInstance();
//                    if (image != null) {
//                        bitmap= BitmapUtils.getInstaceBmp(image);
//                        image.destory();
//
//                       //将bitmap保存本地并输出路径
//                       imageUri= bitmapToUri(bitmap);
//                      // Log.e("111", "uri: "+imageUri );
//                        try{
//                            InputStream inputStream=getContentResolver().openInputStream(imageUri);
//                            Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
//                           // imageView.setImageBitmap(bitmap2);
//                        }catch (FileNotFoundException e){
//                            e.printStackTrace();
//                        }
//                    }

                    CameraPreviewManager.getInstance().stopPreview();

                    //跳转和传值
                    Intent intent = new Intent(FaceCheck.this, ActivityMorning.class);
                    intent.putExtra("id", stu_id);
                    intent.putExtra("uri", imageUri.toString());
                    intent.putExtra("location1", location1);
                    intent.putExtra("location2", location2);
                    intent.putExtra("location3", location3);
                    intent.putExtra("location4", location4);
                    startActivity(intent);
                    finish();

                }

            }
        });
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
//                    ToastUtils.toast(FaceCheck.this, "模型加载成功，欢迎使用");
                }

                @Override
                public void initModelFail(int errorCode, String msg) {
                    FaceSDKManager.initModelSuccess = false;
                    if (errorCode != -12) {
                        ToastUtils.toast(FaceCheck.this, "模型加载失败，请尝试重启应用");
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (toast != null)
            toast.cancel();
        CameraPreviewManager.getInstance().stopPreview();
        finish();
    }

    /**
     * 绘制人脸框
     */
//    private void showFrame(final LivenessModel model) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Canvas canvas = mDrawDetectFaceView.lockCanvas();
//                if (canvas == null) {
//                    mDrawDetectFaceView.unlockCanvasAndPost(canvas);
//                    return;
//                }
//                if (model == null) {
//                    // 清空canvas
//                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                    mDrawDetectFaceView.unlockCanvasAndPost(canvas);
//                    return;
//                }
//                FaceInfo[] faceInfos = model.getTrackFaceInfo();
//                if (faceInfos == null || faceInfos.length == 0) {
//                    // 清空canvas
//                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                    mDrawDetectFaceView.unlockCanvasAndPost(canvas);
//                    return;
//                }
//                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                FaceInfo faceInfo = faceInfos[0];
//
//                rectF.set(FaceOnDrawTexturViewUtil.getFaceRectTwo(faceInfo));
//                // 检测图片的坐标和显示的坐标不一样，需要转换。
//                FaceOnDrawTexturViewUtil.mapFromOriginalRect(rectF,
//                        mAutoCameraPreviewView, model.getBdFaceImageInstance());
//                // 人脸框颜色
//                FaceOnDrawTexturViewUtil.drawFaceColor(mUser, paint, paintBg, model);
//                // 绘制人脸框
//                FaceOnDrawTexturViewUtil.drawRect(canvas,
//                        rectF, paint, 5f, 50f, 25f);
//                // 清空canvas
//                mDrawDetectFaceView.unlockCanvasAndPost(canvas);
//            }
//        });
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy: ..");
        GpioUtils.writeGpioValue(index,"1");
        yolov5ncnn=null;
        CameraPreviewManager.getInstance().stopPreview();


    }


}
