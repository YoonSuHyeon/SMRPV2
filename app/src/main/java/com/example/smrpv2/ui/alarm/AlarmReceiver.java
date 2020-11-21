package com.example.smrpv2.ui.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.smrpv2.R;
import com.example.smrpv2.ui.login.LoginActivity;
import com.example.smrpv2.ui.main.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    static  final String TAG  = "AlarmReceiver";


    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received intent : $intent");
        notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        String content = intent.getStringExtra("content");
        int privateId = intent.getIntExtra("privateId", 0);
        createNotificationChannel(String.valueOf(privateId));
        deliverNotification(context,content,privateId);
    }

    private void deliverNotification(Context context,String content,int privateId) {
        Intent contentIntent = new Intent(context, LoginActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                context,
                privateId,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, String.valueOf(privateId))
                        .setSmallIcon(R.drawable.ic_alarm_set)//알람 이미지
                        .setContentTitle("약드실 시간입니다.")
                        .setContentText(content)
                        .setContentIntent(contentPendingIntent)
                        .setColor(Color.argb(50,100,10,10))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL);

        notificationManager.notify(privateId, builder.build());
    }

    private void createNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId,
                    "Stand up notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("AlarmManager Tests");
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }
    }
}
