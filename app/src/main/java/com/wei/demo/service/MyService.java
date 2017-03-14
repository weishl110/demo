package com.wei.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ${wei} on 2017/3/4.
 */

public class MyService extends Service {
    private static final String TAG = "MyService";
    private Thread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind = ");
//        thread = new Thread(runnable);
//        thread.start();
        th.start();
        return new MyBind();
    }

    public class MyBind extends Binder {

        public MyBind() {
            super();
            Log.e(TAG, "MyBind:  = ");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate:  = ");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e(TAG, "onStart:  = ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand:  = ");

//        thread = new Thread(runnable);
//        thread.start();
        th.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public Runnable runnable = new Runnable() {
        public int i = 0;

        @Override
        public void run() {
            while (i < 30) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "run:  = " + (i++));
                if (i == 30) {
                    thread.interrupt();
                }
            }
        }

        ;
    };


    int i = 0;
    public Thread th = new Thread() {
        @Override
        public void run() {
            super.run();
            while (i < 30) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "run:  = " + (i++));
                if (i == 30) {
                    th.interrupt();
                }
            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        try {
            th.stop();
            th.interrupt();
            Log.e(TAG, "onUnbind:  = " );
        }catch (Exception e){

        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy:  = " + Thread.interrupted());
        th.interrupt();
        boolean interrupted = Thread.interrupted();
        Log.e(TAG, "onDestroy: interrupted = " + interrupted);
    }
}
