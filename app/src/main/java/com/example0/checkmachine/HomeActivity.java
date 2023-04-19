package com.example0.checkmachine;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.idl.main.facesdk.attendancelibrary.utils.AttendanceConfigUtils;
import com.baidu.idl.main.facesdk.attendancelibrary.utils.RegisterConfigUtils;
import com.baidu.idl.main.facesdk.attendancelibrary.view.PreviewTexture;
import com.baidu.idl.main.facesdk.registerlibrary.user.model.SingleBaseConfig;
import com.baidu.idl.main.facesdk.registerlibrary.user.utils.ToastUtils;

import com.baidu.idl.main.facesdk.utils.StreamUtil;
import com.example0.BaseActivity;
import com.example0.checkmachine.R;
import com.example0.datalibrary.api.FaceApi;
import com.example0.datalibrary.listener.DBLoadListener;
import com.example0.datalibrary.model.User;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private LinearLayout ll_morningcheck,ll_install;
    private Handler mHandler = new Handler();
    private int mLiveType=0;
    private boolean isCheck = false;
    private static final int PREFER_WIDTH = 640;
    private static final int PREFER_HEIGHT = 480;
    private PreviewTexture[] previewTextures;
    private Camera[] mCamera;
    private TextureView checkRBGTexture;
    private TextureView checkNIRTexture;
    private ProgressBar progressBar;
    private TextView progressText;
    private View progressGroup;
    private boolean isDBLoad;
    private Future future;
    private Button btn_test;

  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        initUI();

//        initRGBCheck();
        initDBApi();
    }


    private void initUI() {
        ll_morningcheck=findViewById(R.id.ll_morningcheck);
        ll_morningcheck.setOnClickListener(this);

        ll_install=findViewById(R.id.ll_install);
        ll_install.setOnClickListener(this);

        checkRBGTexture = findViewById(R.id.check_rgb_texture);
        checkNIRTexture = findViewById(R.id.check_nir_texture);
        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);
        progressGroup = findViewById(R.id.progress_group);

        //测试按钮
        Button btn_test=findViewById(R.id.btn_test);
        btn_test.setVisibility(View.INVISIBLE);
        btn_test.setOnClickListener(this);

    }

    private void initDBApi(){
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        isDBLoad = false;
        future = Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                FaceApi.getInstance().init(new DBLoadListener() {

                    @Override
                    public void onStart(int successCount) {
                        if (successCount < 5000 && successCount != 0){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadProgress(10);
                                }
                            });
                        }
                    }

                    @Override
                    public void onLoad(final int finishCount, final int successCount, final float progress) {
                        if (successCount > 5000 || successCount == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress((int) (progress * 100));
                                    progressText.setText(((int) (progress * 100)) + "%");
                                }
                            });
                        }
                    }

                    @Override
                    public void onComplete(final List<User> users , final int successCount) {
//                        FileUtils.saveDBList(HomeActivity.this, users);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FaceApi.getInstance().setUsers(users);
                                if (successCount > 5000 || successCount == 0) {
                                    progressGroup.setVisibility(View.GONE);
                                    isDBLoad = true;
                                }
                            }
                        });
                    }

                    @Override
                    public void onFail(final int finishCount, final int successCount, final List<User> users) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FaceApi.getInstance().setUsers(users);
                                progressGroup.setVisibility(View.GONE);
                                ToastUtils.toast(HomeActivity.this,
                                        "人脸库加载失败,共" + successCount + "条数据, 已加载" + finishCount + "条数据");
                                isDBLoad = true;
                            }
                        });
                    }
                }, mContext);
            }
        });
    }

    private void loadProgress(float i){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress((int) ((i / 5000f) * 100));
                progressText.setText(((int) ((i / 5000f) * 100)) + "%");
                if (i < 5000){
                    loadProgress(i + 100);
                }else {
                    progressGroup.setVisibility(View.GONE);
                    isDBLoad = true;
                }
            }
        },10);
    }

    private void initRGBCheck(){
        Log.e("camera数量",Camera.getNumberOfCameras()+"   "+isSetCameraId());
        if (isSetCameraId()){
            return;
        }
        int mCameraNum = Camera.getNumberOfCameras();

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            System.out.println("getting");
        }
        if (mCameraNum > 1){
            try {
                mCamera = new Camera[mCameraNum];
                previewTextures = new PreviewTexture[mCameraNum];
                mCamera[0] = Camera.open(0);
                previewTextures[0] = new PreviewTexture(this, checkRBGTexture);
                previewTextures[0].setCamera(mCamera[0], PREFER_WIDTH, PREFER_HEIGHT);
                mCamera[0].setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        int check = StreamUtil.checkNirRgb(data, PREFER_WIDTH, PREFER_HEIGHT);
                        if (check == 1){
                            setRgbCameraId(0);
                        }
                        release(0);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
//            try {
//                mCamera[1] = Camera.open(1);
//                previewTextures[1] = new PreviewTexture(this, checkNIRTexture);
//                previewTextures[1].setCamera(mCamera[1], PREFER_WIDTH, PREFER_HEIGHT);
//                mCamera[1].setPreviewCallback(new Camera.PreviewCallback() {
//                    @Override
//                    public void onPreviewFrame(byte[] data, Camera camera) {
//                        int check = StreamUtil.checkNirRgb(data, PREFER_WIDTH, PREFER_HEIGHT);
//                        if (check == 1){
//                            setRgbCameraId(1);
//                        }
//                        release(1);
//                    }
//                });
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        } else {
            setRgbCameraId(0);
        }
    }

    private void setRgbCameraId(int index){
        SingleBaseConfig.getBaseConfig().setRBGCameraId(index);
        com.baidu.idl.main.facesdk.attendancelibrary.model.SingleBaseConfig.getBaseConfig().setRBGCameraId(index);
        com.baidu.idl.main.facesdk.registerlibrary.user.model.SingleBaseConfig.getBaseConfig().setRBGCameraId(index);
        Log.e("setting",SingleBaseConfig.getBaseConfig().getRBGCameraId()+"");
        AttendanceConfigUtils.modityJson();
        RegisterConfigUtils.modityJson();

    }

    private boolean isSetCameraId(){
        if (SingleBaseConfig.getBaseConfig().getRBGCameraId() == -1 ||
                com.baidu.idl.main.facesdk.attendancelibrary.
                        model.SingleBaseConfig.getBaseConfig().getRBGCameraId() == -1 ||
                com.baidu.idl.main.facesdk.registerlibrary.user.model.
                        SingleBaseConfig.getBaseConfig().getRBGCameraId() == -1){
            return false;
        }else {
            return true;
        }
    }
    private void release(int id){
        if (mCamera != null && mCamera[id] != null) {
            if (mCamera[id] != null) {
                mCamera[id].setPreviewCallback(null);
                mCamera[id].stopPreview();
                previewTextures[id].release();
                mCamera[id].release();
                mCamera[id] = null;
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (!isDBLoad){
            return;
        }
        switch (v.getId()){
            case R.id.ll_morningcheck:
//                Intent intent_morning=new Intent(this, TestCamera1.class);
                Intent intent_morning=new Intent(this, FaceCheck.class);
                startActivity(intent_morning);
                break;

            case R.id.ll_install:
              // Intent intent_login=new Intent(this, Login.class);
                Intent intent_login=new Intent(this, Login.class);
               startActivity(intent_login);
               break;

//            case R.id.btn_test:
//                Intent intent=new Intent(this, TestCamera1.class);
//                startActivity(intent);
//                // 考勤模块
////                startActivity(new Intent(this, TestCamera.class));
//                break;
        }
    }
    private void judgeLiveType(int type, Class<?> rgbCls, Class<?> nirCls, Class<?> depthCls, Class<?> rndCls) {
        switch (type) {
            case 0: { // 不使用活体
                startActivity(new Intent(HomeActivity.this, rgbCls));
                break;
            }

            case 1: { // RGB活体
                startActivity(new Intent(HomeActivity.this, rgbCls));
                break;
            }

            case 2: { // NIR活体
                startActivity(new Intent(HomeActivity.this, nirCls));
                break;
            }

            case 3: { // 深度活体
                int cameraType = SingleBaseConfig.getBaseConfig().getCameraType();
                judgeCameraType(cameraType, depthCls);
                break;
            }

            case 4: { // rgb+nir+depth活体
                int cameraType = SingleBaseConfig.getBaseConfig().getCameraType();
                judgeCameraType(cameraType, rndCls);
            }
        }
    }

    private void judgeCameraType(int cameraType, Class<?> depthCls) {
        switch (cameraType) {
            case 1: { // pro
                startActivity(new Intent(HomeActivity.this, depthCls));
                break;
            }

            case 2: { // atlas
                startActivity(new Intent(HomeActivity.this, depthCls));
                break;
            }

            case 6: { // Pico
                //  startActivity(new Intent(HomeActivity.this,
                // PicoFaceDepthLivenessActivity.class));
                break;
            }

            default:
                startActivity(new Intent(HomeActivity.this, depthCls));
                break;
        }
    }

}