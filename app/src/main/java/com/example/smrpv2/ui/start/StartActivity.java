package com.example.smrpv2.ui.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.smrpv2.R;
import com.example.smrpv2.ui.common.PermissionAllocate;
import com.example.smrpv2.ui.common.SharedData;
import com.example.smrpv2.ui.login.LoginActivity;
import com.example.smrpv2.ui.tos.ToSActivity;

import me.relex.circleindicator.CircleIndicator;

/*
 * StartActivity   : 처음 으로 시작하는 액티비티  LoginActivity, TosActivity  버튼 클릭시 화면 전환
 */
public class StartActivity extends AppCompatActivity {

    ViewPagerAdapter startAdapter;
    ViewPager startViewPager;
    Button login,signUp; //로그인 버튼 , 회원가입 버튼
    CircleIndicator indicator;
    AutoSlide autoSlide;
    final long DELAY_MS = 1000;
    final long PERIOD_MS = 6000;
    static StartActivity startActivity;
    private int[] images = {R.drawable.start_slide1, R.drawable.start_slide2,R.drawable.start_slide3};
    SharedData sharedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //초기화 작업
        login = findViewById(R.id.Btn_login);
        signUp = findViewById(R.id.Btn_signup);
        startViewPager = findViewById(R.id.viewpager);
        indicator = findViewById(R.id.indicator);
        startAdapter = new ViewPagerAdapter(this, images);


        startViewPager.setAdapter(startAdapter);
        indicator.setViewPager(startViewPager);
        autoSlide = new AutoSlide(startViewPager, DELAY_MS, PERIOD_MS);
        autoSlide.startSlide();
        startActivity = this;

        PermissionAllocate p = new PermissionAllocate(this);
        p.getPermission();

        //로그인 버튼 클릭 시 ..
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        //회원가입 버튼 클릭 시 ..
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ToSActivity.class);
                startActivity(intent);
            }
        });



    }
    public static StartActivity getInstance(){
        return startActivity;
    }

}