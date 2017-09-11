package com.wei.demo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.wei.demo.R;

public class NotifiActivity extends AppCompatActivity {
    private static final String TAG = "debug_NotifiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifi);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 1;
        params.height = 1;
        params.x = 0;
        params.y = 0;
        window.setAttributes(params);

        ScreenManager.getInstance(this).setActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "25行...onResume:  = ");
    }
}
