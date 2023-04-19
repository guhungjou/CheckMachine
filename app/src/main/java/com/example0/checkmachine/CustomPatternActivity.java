package com.example0.checkmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example0.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CustomPatternActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pattern);

        TextView textView = findViewById(R.id.center_text);
textView.setOnClickListener(v -> {

    Intent intent = new Intent(CustomPatternActivity.this, TestCamera.class);
    startActivity(intent);
    finish();

});


    }
}
