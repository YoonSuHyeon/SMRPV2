package com.example.smrpv2.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.user_model.LoginUser;
import com.example.smrpv2.model.UserDto;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.common.SharedData;
import com.example.smrpv2.ui.main.MainActivity;
import com.example.smrpv2.ui.start.StartActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * 스플래쉬 화면
 * */
public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    SharedData sharedData;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedData = new SharedData(this);
        final boolean auto_login = sharedData.isAuto_login();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(auto_login){
                    String id = sharedData.getUser_id();
                    String passwd = sharedData.getUser_password();
                    LoginUser loginUser = new LoginUser(id,passwd);

                    Log.d("TAG", "login_id: "+id);
                    Log.d("TAG", "login_passwd: "+passwd);
                    //로그인 시도
                    Call<UserDto> call= RetrofitHelper.getRetrofitService_server().login(loginUser);
                    call.enqueue(new Callback<UserDto>() {
                        @Override
                        public void onResponse(Call<UserDto> call, Response<UserDto> response) {

                            Log.d("login",response.toString());
                            Log.d("ddd", response.body().getUserId());

                            if(!response.body().getUserId().equals("")){ //로그인 성공
                                //MainActivity로 화면 이동
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                intent.putExtra("name",response.body().getName());
                                startActivity(intent);
                                finish();
                            }else{ //로그인 실패
                                show("자동로그인 실패하였습니다.");
                                intent = new Intent(SplashActivity.this, StartActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<UserDto> call, Throwable t) {

                        }
                    });
                }else{
                    intent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(intent);
                    finish();//SplashActivity.this.
                }

            }
        },2000);

    }
    private void show(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed(){ //뒤로가기 기능 제거

    }
}