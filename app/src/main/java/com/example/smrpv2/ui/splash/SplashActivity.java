package com.example.smrpv2.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.user_model.LoginUser;
import com.example.smrpv2.model.UserDto;
import com.example.smrpv2.model.user_model.UserInform;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.common.SharedData;
import com.example.smrpv2.ui.main.MainActivity;
import com.example.smrpv2.ui.start.StartActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

        //getHashKey();// 각 클라이언트의 디바이스 해시키를 알야하기에 이 메소드를 구현

        Log.d("TAG", "auto_login: "+auto_login);
        if(auto_login){
            String id = sharedData.getUser_id();
            String passwd = sharedData.getUser_password();
            LoginUser loginUser = new LoginUser(id,passwd);

            //로그인 시도
            Call<UserDto> call= RetrofitHelper.getRetrofitService_server().login(loginUser);
            call.enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(Call<UserDto> call, Response<UserDto> response) {

                    if(!response.body().getUserId().equals("")){ //로그인 성공
                        //MainActivity로 화면 이동

                        UserInform user = new UserInform(response.body().getUserId(),response.body().getEmail(),response.body().getName(),
                                response.body().getGender(),response.body().getBirth(),response.body().getCreatedAt());
                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        //intent.putExtra("name",response.body().getName());
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
                    Log.d("TAG", "failfail: ");

                }
            });
        }else{
            intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();//SplashActivity.this.
        }

    }
    private void show(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed(){ //뒤로가기 기능 제거

    }
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }




}