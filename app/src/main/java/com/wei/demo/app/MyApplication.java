package com.wei.demo.app;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by ${wei} on 2017/3/14.
 */

public class MyApplication extends Application {
    private static final String TAG = "debug_MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

//        getProcessName(this, android.os.Process.myPid());
//        startService(new Intent(this,StickyLocaService.class));
//        startService(new Intent(this, MyJobService.class));
//        Thread.setDefaultUncaughtExceptionHandler(new MyHandler());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e(TAG, "22行...onTrimMemory: level = " + level);
//        startService(new Intent(this,StickyLocaService.class));
//        startService(new Intent(this, MyJobService.class));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "27行...onLowMemory:  = ");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e(TAG, "32行...onTerminate:  = ");
    }

    public static String getProcessName(Context cxt, int pid) {
        Log.e(TAG, "45行...getProcessName: pid = " + pid);
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            Log.e(TAG, "49行...getProcessName: prcessName = " + procInfo.processName+"   pid : "+procInfo.pid);
        }
        return null;
    }

    private static class MyHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            Log.e(TAG, "uncaughtException:  = " + e.getMessage());
        }
    }


}
