package com.wei.demo.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.List;

/**
 * Created by ${wei} on 2017/8/28.
 */


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    private String locaServiceName = "com.wei.demo.service.StickyLocaService";
    private String remoteServiceName = "com.wei.demo.service.RemoteService";
    private static final String TAG = "debug_MyJobService";

    private static int i = 0;

    @Override
    public boolean onStartJob(JobParameters params) {
//        Log.e(TAG, "27行...onStartJob:  = " + (i++));
        if (!isWorking(locaServiceName)) {
            startService(new Intent(this, StickyLocaService.class));
        }
        if (!isWorking(remoteServiceName)) {
            startService(new Intent(this, RemoteService.class));
        }
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "40行...onStopJob:  = ");
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "46行...onCreate:  = ");
//        threadStart();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduleJob(getSchedeleInfo(startId));
        Log.e(TAG, "52行...onStartCommand:  = ");
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e(TAG, "68行...onTrimMemory: level = " + level);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "62行...onDestroy:  = ");
    }

    private void scheduleJob(JobInfo jobInfo) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
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

    public JobInfo getSchedeleInfo(int startId) {
        JobInfo.Builder builder = new JobInfo.Builder(startId, new ComponentName(getPackageName(), MyJobService.class.getName()));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                /*.setRequiresCharging(false)*/
                .setPeriodic(200)
               /* .setMinimumLatency(200)
                .setOverrideDeadline(3000)*/
//                .setRequiresDeviceIdle(false)
                /*.setPersisted(true)*/;
        return builder.build();
    }
}
