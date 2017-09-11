package com.wei.demo.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by ${wei} on 2017/8/30.
 */

public class MyAccessibiliService extends AccessibilityService {
    private static final String TAG = "debug_MyAccessibili";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e(TAG, "18行...onAccessibilityEvent:  = ");
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG, "25行...onInterrupt:  = ");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "30行...onCreate:  = ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "35行...onStartCommand:  = ");
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "44行...onDestroy:  = ");
    }
}
