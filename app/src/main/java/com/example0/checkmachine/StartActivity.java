package com.example0.checkmachine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.idl.main.facesdk.attendancelibrary.listener.SdkInitListener;
import com.baidu.idl.main.facesdk.attendancelibrary.utils.AttendanceConfigUtils;
import com.baidu.idl.main.facesdk.registerlibrary.user.utils.RegisterConfigUtils;
import com.example0.BaseActivity;
import com.example0.FaceSDKManager;
import com.example0.checkmachine.R;
import com.example0.checkmachine.start.ActivitionActivity;

import java.util.Timer;
import java.util.TimerTask;


public class StartActivity extends BaseActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mContext = this;

        boolean isAttendanceConfigExit = AttendanceConfigUtils.isConfigExit(this);
        boolean isAttendanceInitConfig = AttendanceConfigUtils.initConfig();
        boolean isRegisterConfigExit = RegisterConfigUtils.isConfigExit(this);
        boolean isRegisterInitConfig = RegisterConfigUtils.initConfig();

        if (isAttendanceInitConfig && isAttendanceConfigExit
                && isRegisterInitConfig && isRegisterConfigExit) {
            Toast.makeText(StartActivity.this, "初始配置加载成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(StartActivity.this, "初始配置失败,将重置文件内容为默认配置", Toast.LENGTH_SHORT).show();
            AttendanceConfigUtils.modityJson();
            RegisterConfigUtils.modityJson();

        }

        initLicense();
    }
    private void initLicense() {
        FaceSDKManager.getInstance().init(mContext, new SdkInitListener() {
            @Override
            public void initStart() {

            }

            public void initLicenseSuccess() {

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        /**
                         *要执行的操作
                         */
                        startActivity(new Intent(mContext, HomeActivity.class));
                        finish();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 2000);
            }

            @Override
            public void initLicenseFail(int errorCode, String msg) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        /**
                         *要执行的操作
                         */
                        startActivity(new Intent(mContext, ActivitionActivity.class));
                        finish();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 2000);
            }

            @Override
            public void initModelSuccess() {
            }

            @Override
            public void initModelFail(int errorCode, String msg) {

            }
        });
    }
}
