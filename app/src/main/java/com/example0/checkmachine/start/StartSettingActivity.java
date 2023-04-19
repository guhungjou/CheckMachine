package com.example0.checkmachine.start;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example0.BaseActivity;
import com.example0.checkmachine.R;

public class StartSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_setting);
        initView();
    }

    private void initView() {

        ImageView startSettingBack = findViewById(R.id.start_setting_back);
        startSettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
