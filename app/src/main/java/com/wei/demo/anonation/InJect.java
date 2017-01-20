package com.wei.demo.anonation;

import android.app.Activity;

/**
 * Created by ${wei} on 2017/1/4.
 */

public class InJect {

    private Activity contentView;

    public static void inject(Activity activity) {
        setContentView(activity);
    }

    public static void setContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        ContentView annotation = clazz.getAnnotation(ContentView.class);
        int value = annotation.value();
        activity.setContentView(value);
    }
}
