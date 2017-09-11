package com.wei.demo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wei.demo.R;

/**
 * Created by ${wei} on 2017/8/28.
 */

public class CancelService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = intent.getIntExtra("startId", -1);
        if (id != -1) {
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
            Notification.Builder builder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("猴子服务")
                    .setContentTitle("title")
                    .setContentText("防止被杀")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notification = builder.build();
            } else {
                notification = builder.getNotification();
            }
            startForeground(id, notification);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(id);
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }


}
