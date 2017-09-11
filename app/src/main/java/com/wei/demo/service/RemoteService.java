package com.wei.demo.service;

import android.app.ActivityManager;
import android.app.Notification;
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

import com.wei.demo.R;
import com.wei.demo.aidl.IProcessAidl;

import java.util.List;

/**
 * Created by ${wei} on 2017/8/25.
 */

public class RemoteService extends Service {
    private static final String TAG = "debug_RemoteService";
    private MyServiceConnection mConnection;
    private MyBinde myBinde;

    int id = 110;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinde;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (myBinde == null) {
            myBinde = new MyBinde();
        }
        mConnection = new MyServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, StickyLocaService.class), mConnection, Context.BIND_IMPORTANT);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentText("猴子服务启动中")
                .setContentTitle("猴子服务")
                .setAutoCancel(true)
                .setTicker("remoteservice");

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        startForeground(id, notification);
        Intent it = new Intent(this, CancelService.class);
        it.putExtra("startId", id);
        startService(it);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e(TAG, "74行...onTrimMemory: level ====================== " + level);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void startLocaService() {
        startService(new Intent(this, StickyLocaService.class));
        this.bindService(new Intent(this, StickyLocaService.class), mConnection, Context.BIND_IMPORTANT);
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "51行...onServiceConnected: 远程服务 = ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startLocaService();
        }
    }

    private class MyBinde extends IProcessAidl.Stub {

        @Override
        public String getProName() throws RemoteException {
            return "Remote猴子搬来的救兵";
        }
    }

    private boolean isWorking(String name) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(128);
        if (runningServices.size() <= 0) {
            return false;
        }
        boolean isWork = false;
        int size = runningServices.size();
        for (int i = 0; i < size; i++) {
            if (name.equals(runningServices.get(i).service.getClassName())) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}
