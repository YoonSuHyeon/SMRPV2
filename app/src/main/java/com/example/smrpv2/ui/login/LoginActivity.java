package com.example.smrpv2.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.user.LoginUser;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.findid.FindIdActivity;

import com.example.smrpv2.ui.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * 로그인 액티비티
 */
public class LoginActivity extends AppCompatActivity {
    Context context; //this 객체
    TextView findId; //아이디 비밀 번호 찾기 TextView
    EditText Txt_id,Txt_password;
    Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //초기화 작업
        context=this;
        initView();


        //아이디 비밀번호 찾기 클릭 시 ...
        findId.setOnClickListener(new View.OnClickListener() { // 아이디 / 비밀번호 찾기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FindIdActivity.class);
                startActivity(intent);

            }
        });



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**임시로 Intent로 선언 테스트용**/
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                //login();
            }
        });
    }

    private void login(){
        //입력된 아이디 비밀번호 가져오기
        String id =Txt_id.getText().toString();
        String passWord = Txt_password.getText().toString();

        LoginUser loginUser = new LoginUser(id,passWord);

        //로그인 시도
        Call<Message> call=RetrofitHelper.getRetrofitService_server().login(loginUser);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                Log.d("login",response.toString());
                Log.d("ddd", response.body().getResultCode());

                if(response.body().getResultCode().equals("PASS")){ //로그인 성공

                    //MainActivity로 화면 이동
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                }else{ //로그인 실패
                    Toast.makeText(context, "로그인 오류", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });



    }
    private void initView() { //init
        findId=findViewById(R.id.tv_findId);
        login_btn = findViewById(R.id.Btn_Innerlogin);
        Txt_id=findViewById(R.id.Txt_id);
        Txt_password = findViewById(R.id.Txt_password);

    }
}