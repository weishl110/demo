package com.wei.demo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wei.demo.receiver.MyReceiver;

/**
 * Created by ${wei} on 2017/8/30.
 */

public class ScreenBroadcastListener {

    private Context mContext;
    private ScreenStateListener mListener;


    public ScreenBroadcastListener(Context context) {
        this.mContext = context;
    }

    public interface ScreenStateListener {
        void onScreenOn();

        void onScreenOff();
    }

    public void regiserListener(ScreenStateListener listener){
        this.mListener = listener;
        registerListener();
    }

    private void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_APP_ERROR);
        filter.addAction(Intent.ACTION_CALL);
        filter.addAction(Intent.ACTION_INPUT_METHOD_CHANGED);
        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        filter.addAction(Intent.ACTION_SENDTO);//发短信
        filter.addAction(Intent.ACTION_MANAGE_NETWORK_USAGE);
        filter.addAction(Intent.ACTION_CAMERA_BUTTON);
        MyReceiver receiver = new MyReceiver();
        filter.setPriority(1000);
        receiver.setListener(mListener);
        mContext.registerReceiver(receiver, filter);

    }

}
