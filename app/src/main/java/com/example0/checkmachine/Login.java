package com.example0.checkmachine;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;

import com.baidu.idl.main.facesdk.registerlibrary.user.utils.KeyboardsUtils;
import com.example0.BaseActivity;
import com.example0.checkmachine.R;
import com.example0.retrofit.ApiService;
import com.example0.retrofit.ResponseData;
import com.example0.retrofit.Teacher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends BaseActivity {

    private EditText et_username,et_password;
    private TextView btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();

    }

    private void initUi() {
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入
                String username = et_username.getText().toString().trim();
                String pwd = et_password.getText().toString().trim();
                //判断用户名或者密码是否为空
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(Login.this,"账号或密码为空",Toast.LENGTH_LONG).show();
                }else {
                   // Log.e(TAG, "onClick: username"+username+"pwd:"+pwd );
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://kg.ykwell.cn/api/device/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ApiService apiService = retrofit.create(ApiService.class);
                    Call<ResponseData<Teacher>> call = apiService.login(username, pwd);
                    call.enqueue(new Callback<ResponseData<Teacher>>() {
                        @Override
                        public void onResponse(Call<ResponseData<Teacher>> call, Response<ResponseData<Teacher>> response) {
                            Log.e(TAG, "onResponse: " + response.body().getStatus());
                            if(response.body().getStatus().equals("20001")){
                                Toast.makeText(Login.this,"该账号不存在！",Toast.LENGTH_LONG).show();
                            }else if(response.body().getStatus().equals("20002")){
                                Toast.makeText(Login.this,"密码错误，请重新输入！",Toast.LENGTH_LONG).show();
                            }else if(response.body().getStatus().equals("0")){
                                Toast.makeText(Login.this,"登录成功",Toast.LENGTH_LONG).show();
                                Log.e(TAG, "onResponse: " + response.body().getData().getToken());
                                Log.e(TAG, "onResponse: " + response.body().getData().getKindergarten().getName());

                                startActivity(new Intent(Login.this,ActivityFaceManage.class));
                                finish();
                            }



                        }

                        @Override
                        public void onFailure(Call<ResponseData<Teacher>> call, Throwable t) {
                            Log.e(TAG, "onResponse: " + t);
                            Toast.makeText(Login.this,"遇到错误，请稍后再试",Toast.LENGTH_LONG).show();
                        }

                    });

                }
            }
        });

    }

    /**
     * 点击非编辑区域收起键盘
     * 获取点击事件
     */
    @CallSuper
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (KeyboardsUtils.isShouldHideKeyBord(view, ev)) {
                KeyboardsUtils.hintKeyBoards(view);
            }
        }
        return super.dispatchTouchEvent(ev);
    }


}
