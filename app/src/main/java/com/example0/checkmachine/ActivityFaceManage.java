package com.example0.checkmachine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.idl.main.facesdk.registerlibrary.user.activity.UserManagerActivity;
import com.baidu.idl.main.facesdk.registerlibrary.user.model.SingleBaseConfig;
import com.baidu.idl.main.facesdk.registerlibrary.user.register.FaceRegisterNewActivity;
import com.baidu.idl.main.facesdk.registerlibrary.user.register.FaceRegisterNewDepthActivity;
import com.baidu.idl.main.facesdk.registerlibrary.user.register.FaceRegisterNewNIRActivity;
import com.baidu.idl.main.facesdk.registerlibrary.user.register.FaceRegisterNewRgbNirDepthActivity;
import com.example0.BaseActivity;
import com.example0.checkmachine.R;

public class ActivityFaceManage extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_face_manager,ll_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_index);
        initView();
    }

    private void initView() {


        ll_register=findViewById(R.id.ll_register);
        ll_register.setOnClickListener(this);

        ll_face_manager=findViewById(R.id.ll_manager);
        ll_face_manager.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_register:
                //跳转人脸注册页面
                judgeLiveType(0,
                        FaceRegisterNewActivity.class,
                        FaceRegisterNewNIRActivity.class,
                        FaceRegisterNewDepthActivity.class,
                        FaceRegisterNewRgbNirDepthActivity.class);
                break;

            case R.id.ll_manager:
                //  跳转人脸库管理
                startActivity(new Intent(ActivityFaceManage.this, UserManagerActivity.class));
                break;


        }
    }

    private void judgeLiveType(int type, Class<?> rgbCls, Class<?> nirCls, Class<?> depthCls, Class<?> rndCls) {
        switch (type) {
            case 0: { // 不使用活体
                startActivity(new Intent(ActivityFaceManage.this, rgbCls));
                break;
            }

            case 1: { // RGB活体
                startActivity(new Intent(ActivityFaceManage.this, rgbCls));
                break;
            }

            case 2: { // NIR活体
                startActivity(new Intent(ActivityFaceManage.this, nirCls));
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
                startActivity(new Intent(ActivityFaceManage.this, depthCls));
                break;
            }

            case 2: { // atlas
                startActivity(new Intent(ActivityFaceManage.this, depthCls));
                break;
            }

            case 6: { // Pico
                //  startActivity(new Intent(HomeActivity.this,
                // PicoFaceDepthLivenessActivity.class));
                break;
            }

            default:
                startActivity(new Intent(ActivityFaceManage.this, depthCls));
                break;
        }
    }

}
