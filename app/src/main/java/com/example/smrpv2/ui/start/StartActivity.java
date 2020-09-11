package com.example.smrpv2.ui.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.smrpv2.R;
import com.example.smrpv2.ui.login.LoginActivity;
import com.example.smrpv2.ui.tos.ToSActivity;

/*
 * StartActivity   : 처음 으로 시작하는 액티비티  LoginActivity, TosActivity  버튼 클릭시 화면 전환
 */
public class StartActivity extends AppCompatActivity {

    Button login,signUp; //로그인 버튼 , 회원가입 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //초기화 작업
        login= findViewById(R.id.Btn_login);
        signUp=findViewById(R.id.Btn_signup);

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






}