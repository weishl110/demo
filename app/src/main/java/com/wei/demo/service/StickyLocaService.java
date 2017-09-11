package com.wei.demo.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.wei.demo.R;
import com.wei.demo.aidl.IProcessAidl;
import com.wei.demo.ui.NotifiActivity;
import com.wei.demo.ui.ScreenBroadcastListener;
import com.wei.demo.ui.ScreenManager;

/**
 * Created by ${wei} on 2017/8/25.
 */

public class StickyLocaService extends Service {
    private static final String TAG = "debug_locaService";
    private MyServiceConnection myServiceConnection;
    private LocaBinder locaBinder;

    int id = 110;
    private ScreenManager screenManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return locaBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (locaBinder == null) {
            locaBinder = new LocaBinder();
        }
        myServiceConnection = new MyServiceConnection();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, RemoteService.class), myServiceConnection, Context.BIND_IMPORTANT);
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

        screenManager = ScreenManager.getInstance(this);
        Intent it = new Intent(this, CancelService.class);
        it.putExtra("startId", id);
        startService(it);
        ScreenBroadcastListener listener = new ScreenBroadcastListener(this);
        listener.regiserListener(new ScreenBroadcastListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                screenManager.finishActivity();

            }

            @Override
            public void onScreenOff() {
                screenManager.startActvity();
                Log.e(TAG, "81行...onScreenOff:  -----------------------= ");
            }
        });
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e(TAG, "77行...onTrimMemory: level ================== " + level);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void startRemoteService() {
        startService(new Intent(this, RemoteService.class));
        this.bindService(new Intent(this, RemoteService.class), myServiceConnection, Context.BIND_IMPORTANT);
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "51行...onServiceConnected: 本地服务 = ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(StickyLocaService.this, "远程服务Remote被干掉了", Toast.LENGTH_SHORT).show();
            startRemoteService();
        }
    }

    private class LocaBinder extends IProcessAidl.Stub {
        @Override
        public String getProName() throws RemoteException {
            return "locaService搬来的救兵";
        }
    }

}
