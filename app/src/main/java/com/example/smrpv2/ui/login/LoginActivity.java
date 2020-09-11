package com.example.smrpv2.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

import com.example.smrpv2.R;
import com.example.smrpv2.ui.findid.FindIdActivity;

import com.example.smrpv2.ui.main.MainActivity;

/*
 * 로그인 액티비티
 */
public class LoginActivity extends AppCompatActivity {
    Context context; //this 객체
    TextView findId; //아이디 비밀 번호 찾기 TextView

    Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //초기화 작업
        findId=findViewById(R.id.tv_findId);
        context=this;

        //아이디 비밀번호 찾기 클릭 시 ...
        findId.setOnClickListener(new View.OnClickListener() { // 아이디 / 비밀번호 찾기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FindIdActivity.class);
                startActivity(intent);

            }
        });


        login_btn = findViewById(R.id.Btn_Innerlogin);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}