package com.example.smrpv2.ui.tos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.smrpv2.R;
import com.example.smrpv2.ui.signup.SignUpActivity;

public class ToSActivity extends AppCompatActivity {
    Context context; //this 객체
    Button agree; //동의하기 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_s);

        //초기화 작업
        agree= findViewById(R.id.btn_agree);
        context=this;

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}