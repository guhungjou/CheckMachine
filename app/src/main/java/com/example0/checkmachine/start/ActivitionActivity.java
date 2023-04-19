package com.example0.checkmachine.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.idl.main.facesdk.FaceAuth;
import com.baidu.idl.main.facesdk.attendancelibrary.utils.NetUtil;
import com.baidu.idl.main.facesdk.callback.Callback;
import com.example0.BaseActivity;
import com.example0.checkmachine.HomeActivity;
import com.example0.checkmachine.R;

public class ActivitionActivity extends BaseActivity implements View.OnClickListener{
    public static Context mContext;
    private FaceAuth faceAuth;
    private TextView accredit_deviceTv;
    private Button accredit_offBtn;
    private TextView accredit_hintTv;
    private TextView accredit_offTv;
    private ImageView accredit_offView;
    private TextView accredit_onTv;
    private ImageView accredit_onView;
    private TextView accredit_useTv;
    private ImageView accredit_useView;
    private TextView accredit_offhiteTv;
    private TextView accredit_onhintTv;
    private Button accredit_onBtn;
    private TextView accredit_onhiteTv;
    private PopupWindow popupWindow;
    RelativeLayout rel;
    boolean isFlag = false;
    boolean isTrue = true;
    private View view1;
    int count = 0;
    private LinearLayout accreditOnRl;


    private EditText activity_et_one;
    private EditText activity_et_two;
    private EditText activity_et_three;
    private EditText activity_et_four;

    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 1000; // 快速点击间隔
    private Vibrator vibrator;
    // 拼接后的激活码
    private String end;
    private View activity_one_view;
    private View activity_two_view;
    private View activity_three_view;
    private View activity_four_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activition);
        mContext = this;
        initView();
        // 点击激活按钮3次无响应弹出popupwindow
        initPopupWindow();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void initPopupWindow() {
        SharedPreferences sharedPreferences = getSharedPreferences("ws", MODE_PRIVATE);
        boolean accredit = sharedPreferences.getBoolean("accredit", false);
        if (accredit == false) {
            // 以view将view_layout中的布局和activity_main布局进行桥接
            view1 = View.inflate(this, R.layout.layout_popup_hint, null);
            popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // 点击框外可以使得popupwindow消失
            popupWindow.setFocusable(false);
            popupWindow.setTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(false);

            TextView hintHelpTv = view1.findViewById(R.id.hint_helpTv);
            hintHelpTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bangListener();
                }
            });
        }
    }

    private void initHandler() {
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // 实现页面跳转
                popupWindow.dismiss();
                accredit_offhiteTv.setTextColor(Color.parseColor("#999999"));
                accredit_onhiteTv.setTextColor(Color.parseColor("#999999"));
                return false;

            }
        }).sendEmptyMessageDelayed(0, 3000);
    }


    private void initView() {

        // 复制按钮
        faceAuth = new FaceAuth();
        // 在线激活
        accredit_onhintTv = findViewById(R.id.accredit_onhintTv);
        accredit_onBtn = findViewById(R.id.accredit_onBtn);
        accredit_onBtn.setOnClickListener(this);
        accredit_onhiteTv = findViewById(R.id.accredit_onhiteTv);
        accredit_onhiteTv.setOnClickListener(this);
        accreditOnRl = findViewById(R.id.accredit_onRl);

        // 输入序列码
        activity_et_one = findViewById(R.id.activity_et_one);
        activity_et_two = findViewById(R.id.activity_et_two);
        activity_et_three = findViewById(R.id.activity_et_three);
        activity_et_four = findViewById(R.id.activity_et_four);
        activity_et_two.setFocusable(false);
        activity_et_two.setFocusableInTouchMode(false);
        activity_et_two.requestFocus();

        activity_et_three.setFocusable(false);
        activity_et_three.setFocusableInTouchMode(false);
        activity_et_three.requestFocus();

        activity_et_four.setFocusable(false);
        activity_et_four.setFocusableInTouchMode(false);
        activity_et_four.requestFocus();

        activity_one_view = findViewById(R.id.activity_one_view);
        activity_two_view = findViewById(R.id.activity_two_view);
        activity_three_view = findViewById(R.id.activity_three_view);
        activity_four_view = findViewById(R.id.activity_four_view);

        initActivation();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 在线激活
            // 在线激活按钮
            case R.id.accredit_onBtn:
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                    count++;
                }
                lastClickTime = System.currentTimeMillis();
                if (count == 3) {
                    popupWindow.showAsDropDown(accredit_onhiteTv, -15, 10);
                    count = 0;
                    initHandler();
                    accredit_onhiteTv.setTextColor(Color.parseColor("#00BAF2"));
                }
                if (activity_et_one.getText().toString().trim().length() == 4 &&
                        activity_et_two.getText().toString().trim().length() == 4
                        && activity_et_three.getText().toString().trim().length() == 4
                        && activity_et_four.getText().toString().trim().length() == 4) {
                    String et_one = activity_et_one.getText().toString().trim();
                    String et_two = activity_et_two.getText().toString().trim();
                    String et_three = activity_et_three.getText().toString().trim();
                    String et_four = activity_et_four.getText().toString().trim();
                    end = et_one + "-" + et_two + "-" + et_three + "-" + et_four;


                }
                boolean onNetworkConnected = NetUtil.isNetworkConnected(mContext);
                if (onNetworkConnected) {
                    faceAuth.initLicenseOnLine(this, end, new Callback() {
                        @Override
                        public void onResponse(final int code, final String response) {
                            if (code == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        isFlag = true;
                                        accredit_onhintTv.setText("");
                                        startActivity(new Intent(mContext, HomeActivity.class));
                                        finish();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.equals("key invalid")) {
                                            accredit_onhintTv.setText("序列号有误，请重新输入");
                                        } else if (response.equals("license has actived on other device")) {
                                            accredit_onhintTv.setText("激活失败，该序列号已在其它设备激活，请使用其它有效序列号");
                                        } else if (code == 14) {
                                            accredit_onhintTv.setText("激活失败，该序列号不在有效期范围内");
                                        } else if (response.equals("在线激活失败")) {
                                            accredit_onhintTv.setText("激活失败，该序列号不在有效期范围内");
                                        } else if (response.equals("auth expired time")) {
                                            accredit_onhintTv.setText("激活失败，该序列号不在有效期范围内");
                                        } else {
                                            accredit_onhintTv.setText(response);
                                        }
                                        initShake();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    accredit_onhintTv.setText("激活失败，请保证设备网络通畅");

                    initShake();
                }
                break;

            // 在线激活遇到问题
            case R.id.accredit_onhiteTv:
                bangListener();
                break;

        }
    }

    // 为组件设置一个抖动效果
    private void initShake() {
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shake);
        activity_et_one.startAnimation(shake);
        activity_et_two.startAnimation(shake);
        activity_et_three.startAnimation(shake);
        activity_et_four.startAnimation(shake);
        // 改变view的颜色
        activity_one_view.setBackgroundColor(Color.parseColor("#F34B56"));
        activity_two_view.setBackgroundColor(Color.parseColor("#FF0033"));
        activity_three_view.setBackgroundColor(Color.parseColor("#FF0033"));
        activity_four_view.setBackgroundColor(Color.parseColor("#FF0033"));

    }

    private void bangListener() {
        Intent intent = new Intent(mContext, StartSettingActivity.class);
        startActivity(intent);
    }

    /**
     * 点击空白区域隐藏键盘.
     *
     * @param event the event
     * @return true, if successful
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (ActivitionActivity.this.getCurrentFocus() != null) {
                if (ActivitionActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(ActivitionActivity.this.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    // 激活
    private void initActivation() {
        activity_et_one.setTransformationMethod(new AllCapTransformationMethod(true));
        activity_et_two.setTransformationMethod(new AllCapTransformationMethod(true));
        activity_et_three.setTransformationMethod(new AllCapTransformationMethod(true));
        activity_et_four.setTransformationMethod(new AllCapTransformationMethod(true));
        activity_et_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activity_et_one.length() == 0) {
                    activity_one_view.setBackgroundColor(Color.parseColor("#666666"));
                } else if (activity_et_one.length() == 4) {
                    activity_one_view.setBackgroundColor(Color.parseColor("#666666"));
                    activity_et_two.setFocusable(true);
                    activity_et_two.setFocusableInTouchMode(true);
                    activity_et_two.requestFocus();
                    activity_et_two.setText(activity_et_two.getText().toString().trim() + " ");
                    activity_et_two.setSelection(activity_et_two.getText().length());
                } else if (activity_et_one.length() < 4) {
                    activity_one_view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
        activity_et_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activity_et_two.length() == 0) {
                    activity_et_one.setFocusable(true);
                    activity_et_one.setFocusableInTouchMode(true);
                    activity_et_one.requestFocus();
                    activity_two_view.setBackgroundColor(Color.parseColor("#666666"));
                } else if (activity_et_two.getText().toString().trim().length() == 4) {
                    activity_two_view.setBackgroundColor(Color.parseColor("#666666"));
                    activity_et_three.setFocusable(true);
                    activity_et_three.setFocusableInTouchMode(true);
                    activity_et_three.requestFocus();
                    activity_et_three.setText(activity_et_three.getText().toString().trim() + " ");
                    activity_et_three.setSelection(activity_et_three.getText().length());
                } else if (activity_et_two.getText().toString().trim().length() < 4) {
                    activity_two_view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }


            }
        });
        activity_et_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activity_et_three.length() == 0) {
                    activity_et_two.setFocusable(true);
                    activity_et_two.setFocusableInTouchMode(true);
                    activity_et_two.requestFocus();
                    activity_three_view.setBackgroundColor(Color.parseColor("#666666"));

                } else if (activity_et_three.getText().toString().trim().length() == 4) {
                    activity_three_view.setBackgroundColor(Color.parseColor("#666666"));
                    activity_et_four.setFocusable(true);
                    activity_et_four.setFocusableInTouchMode(true);
                    activity_et_four.requestFocus();
                    activity_et_four.setText(activity_et_four.getText().toString().trim() + " ");
                    activity_et_four.setSelection(activity_et_four.getText().length());
                } else if (activity_et_three.getText().toString().trim().length() < 4) {
                    activity_three_view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
        activity_et_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activity_et_four.length() == 0) {
                    activity_et_three.setFocusable(true);
                    activity_et_three.setFocusableInTouchMode(true);
                    activity_et_three.requestFocus();
                    activity_four_view.setBackgroundColor(Color.parseColor("#666666"));
                } else if (activity_et_four.getText().toString().trim().length() == 4) {
                    activity_four_view.setBackgroundColor(Color.parseColor("#666666"));
                    accredit_onBtn.setEnabled(true);
                    accredit_onBtn.setBackground(getResources().getDrawable(R.mipmap.btn_main_normal));
                    accredit_onBtn.setTextColor(Color.parseColor("#FFFFFF"));
                } else if (activity_et_four.getText().toString().trim().length() < 4) {
                    activity_four_view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    accredit_onBtn.setBackground(getResources().getDrawable(R.mipmap.btn_all_d));
                    accredit_onBtn.setEnabled(false);
                    accredit_onBtn.setTextColor(Color.parseColor("#666666"));
                }
            }
        });
    }

    // 转大写
    public static class AllCapTransformationMethod extends ReplacementTransformationMethod {

        private char[] lower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        private char[] upper = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        private boolean allUpper = false;

        public AllCapTransformationMethod(boolean needUpper) {
            this.allUpper = needUpper;
        }

        @Override
        protected char[] getOriginal() {
            if (allUpper) {
                return lower;
            } else {
                return upper;
            }
        }

        @Override
        protected char[] getReplacement() {
            if (allUpper) {
                return upper;
            } else {
                return lower;
            }
        }
    }
}
