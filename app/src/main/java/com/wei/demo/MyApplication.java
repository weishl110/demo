package com.wei.demo;

import android.app.Application;
import android.util.Log;

/**
 * Created by ${wei} on 2017/3/14.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new MyHandler());
    }

    private static class MyHandler implements Thread.UncaughtExceptionHandler{
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            Log.e(TAG, "uncaughtException:  = " + e.getMessage());
        }
    }


}
