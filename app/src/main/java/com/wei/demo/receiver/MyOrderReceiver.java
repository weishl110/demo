package com.wei.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ${wei} on 2017/9/4.
 */

public class MyOrderReceiver extends BroadcastReceiver {
    private static final String TAG = "debug_MyOrderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "15行...onReceive: 有序广播 ========== " + intent.getAction());
        Bundle resultExtras = getResultExtras(true);
        if (resultExtras != null) {
            int bundle = resultExtras.getInt("bundle");
            String test = resultExtras.getString("test");
            Log.e(TAG, "21行...onReceive: bundle = " + bundle + "  test : " + test);
            resultExtras.putInt("bundle", 110);
        }
        setResultExtras(resultExtras);
    }
}
