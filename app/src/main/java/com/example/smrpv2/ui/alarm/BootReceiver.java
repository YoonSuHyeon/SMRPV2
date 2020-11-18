package com.example.smrpv2.ui.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            Intent bootIntent = new Intent(context,BootReceiver.class);
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                context.startForegroundService(bootIntent);
            }else{
                context.startService(bootIntent);
            }

        }


    }
}
