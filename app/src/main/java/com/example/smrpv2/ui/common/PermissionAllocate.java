package com.example.smrpv2.ui.common;

import android.Manifest;
import android.app.Activity;
import android.os.Build;

import androidx.core.app.ActivityCompat;

public class PermissionAllocate {
    final int PERMISSION = 1;
    Activity activity;
    public PermissionAllocate(Activity activity){
        this.activity = activity;
    }
    public void getPermission(){
        if (Build.VERSION.SDK_INT >= 23) {      //퍼미션 권한 부여
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,Manifest.permission.FOREGROUND_SERVICE}, PERMISSION);
        }//퍼미션접근 권한
    }

}
