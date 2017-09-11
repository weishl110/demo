package com.wei.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;

/**
 * Created by ${wei} on 2017/8/30.
 */

public class ScreenManager {

    private Context mContext;

    private WeakReference<Activity> mActivityWeak;

    private static ScreenManager mInstance;

    public static ScreenManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ScreenManager(context);
        }
        return mInstance;
    }

    private ScreenManager(Context context) {
        this.mContext = context;
    }

    public void setActivity(Activity activity) {
        mActivityWeak = new WeakReference<Activity>(activity);
    }

    public void finishActivity() {
        if (mActivityWeak != null) {
            Activity activity = mActivityWeak.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }


    public void startActvity() {
//        Intent intent = new Intent(mContext, NotifiActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
    }
}
