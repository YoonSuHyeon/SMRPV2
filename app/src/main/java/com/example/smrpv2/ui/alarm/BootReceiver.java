package com.example.smrpv2.ui.alarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.smrpv2.ui.common.SharedData;

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "onReceive: "+intent.getAction());
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            Toast.makeText(context, "bootCompleted", Toast.LENGTH_SHORT).show();
            Intent bootIntent = new Intent(context,AlarmAllAdd.class);
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                context.startForegroundService(bootIntent);
            }else{
                context.startService(bootIntent);
            }

        }


    }

}
