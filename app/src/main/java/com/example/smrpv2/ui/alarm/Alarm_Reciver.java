package com.example.smrpv2.ui.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import com.example.smrpv2.ui.login.LoginActivity;
import com.example.smrpv2.R;

import static com.example.smrpv2.ui.alarm.AlarmSetActivity.NOTIFICATION_CHANNEL_ID;

public class Alarm_Reciver {/*extends BroadcastReceiver {

    Context context;
  //  String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;


       // String getString = intent.getExtras().getString("state");


        NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context.getApplicationContext(), LoginActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.alarm_medicine2))
                .setSmallIcon(R.drawable.location_icon)
                .setContentTitle("얄 알람 서비스")
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setContentText("약을 드실 시간입니다!!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            CharSequence channelName ="테스트 채널";
            String description = "오레오 이상의 버전을 위한 것";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,channelName,importance);
            channel.setDescription(description);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }else builder.setSmallIcon(R.mipmap.ic_launcher); //오레오 이하 에서는 밉맵사용해야만 함

        assert  notificationManager != null;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK  |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "My:Tag");
        wakeLock.acquire(5000);
        notificationManager.notify(1234,builder.build());

    }*/
}
