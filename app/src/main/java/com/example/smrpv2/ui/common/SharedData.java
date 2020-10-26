package com.example.smrpv2.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class SharedData {

    private Activity activity;
    SharedPreferences loginInformtaion;
    SharedPreferences.Editor editor;
    public SharedData(Activity activity){
        this.activity = activity;
    }


    public void setUserId(Activity activity, String user_id,boolean store_id){ //아이디 저장 메소드
        loginInformtaion = activity.getSharedPreferences("user_inform",Context.MODE_PRIVATE);
        editor = loginInformtaion.edit();
        editor.putString("user_id",user_id);
        editor.putBoolean("stored_id",store_id);
        editor.apply();
    }

    public void setUserAuto(Activity activity,String id, String password,boolean auto_login){ //자동로그인 메소드

        /*auto_login = true;
        this.user_id = id;
        this.user_password = password;*/

        Log.d("TAG", "setUserAuto: id"+id);
        Log.d("TAG", "setUserAuto: password"+password);
        Log.d("TAG", "setUserAuto: auto_login"+auto_login);

        loginInformtaion = activity.getSharedPreferences("user_inform", Context.MODE_PRIVATE);
        editor= loginInformtaion.edit();
        editor.putString("user_id",id);
        editor.putString("user_password",password);
        editor.putBoolean("auto_login",auto_login);
        editor.apply();


    }
    public String getUser_id() {
        loginInformtaion = activity.getSharedPreferences("user_inform",Context.MODE_PRIVATE);
        return loginInformtaion.getString("user_id","");

    }

    public String getUser_password() {
        loginInformtaion = activity.getSharedPreferences("user_inform",Context.MODE_PRIVATE);
        return loginInformtaion.getString("user_password","");
    }

    public boolean isStroe_id() {
        loginInformtaion = activity.getSharedPreferences("user_inform",Context.MODE_PRIVATE);
        return loginInformtaion.getBoolean("stored_id",false);

    }

    public boolean isAuto_login(){
        loginInformtaion = activity.getSharedPreferences("user_inform",Context.MODE_PRIVATE);
        return loginInformtaion.getBoolean("auto_login",false);

    }
    public void reset(){
        loginInformtaion = activity.getSharedPreferences("user_inform", Context.MODE_PRIVATE);
        editor= loginInformtaion.edit();
        editor.clear().apply();
    }

}
