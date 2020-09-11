package com.example.smrpv2.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.smrpv2.R;
import com.example.smrpv2.ui.start.StartActivity;

/*
 * 스플래쉬 화면
 * */
public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },2000);

    }
    @Override
    public void onBackPressed(){ //뒤로가기 기능 제거

    }
}