package com.wei.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wei.demo.service.RemoteService;
import com.wei.demo.service.StickyLocaService;
import com.wei.demo.ui.ScreenBroadcastListener;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "debug_MyReceiver";
    private ScreenBroadcastListener.ScreenStateListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
//        context.startService(new Intent(context, StickyLocaService.class));
//        context.startService(new Intent(context, RemoteService.class));
        String action = intent.getAction();
        Log.e(TAG, "16行...onReceive: 无序广播------------ = " + action);
        Bundle resultExtras = getResultExtras(true);
        if (resultExtras != null) {
            int bundle = resultExtras.getInt("bundle");
            Log.e(TAG, "27行...onReceive:  = " + bundle);
            resultExtras.putInt("bundle", 1000);
        }
//        setResultExtras(resultExtras);
//
//        if (listener == null) return;
//        if (action.equals(Intent.ACTION_SCREEN_OFF)) {
//            listener.onScreenOff();
//        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
//            listener.onScreenOn();
//        }
    }

    public void setListener(ScreenBroadcastListener.ScreenStateListener listener) {
        this.listener = listener;
    }
}
