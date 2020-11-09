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
    static  final int NOTIFICATION_ID   = 2;
    static  final String PRIMARY_CHANNEL_ID = "alarm_channel";

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received intent : $intent");
        notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        createNotificationChannel();
        deliverNotification(context);
    }

    private void deliverNotification(Context context) {
        Intent contentIntent = new Intent(context, LoginActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                context,
                NOTIFICATION_ID,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_alarm_set)//알람 이미지
                        .setContentTitle("Alert")
                        .setContentText("This is alarm")
                        .setContentIntent(contentPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID,
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
