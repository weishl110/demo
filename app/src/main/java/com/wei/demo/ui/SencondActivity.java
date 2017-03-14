package com.wei.demo.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wei.demo.R;
import com.wei.demo.adapter.MyRecyclerAdapter;
import com.wei.demo.service.MyService;
import com.wei.demo.view.BouncingMenu;

import java.util.ArrayList;

/**
 * Created by ${wei} on 2017/2/9.
 */

public class SencondActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "zpy_SencondActivity";

    private BouncingMenu bouncingMenu;
    private LinearLayout ll;
    private MyConnection myConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sencond_layout);
        ll = (LinearLayout) findViewById(R.id.ll);
        TextView start = (TextView) findViewById(R.id.start);
        TextView unbind = (TextView) findViewById(R.id.unbind);
        TextView bind = (TextView) findViewById(R.id.bind);
        TextView stop = (TextView) findViewById(R.id.stop);

        start.setOnClickListener(this);
        unbind.setOnClickListener(this);
        bind.setOnClickListener(this);
        stop.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState: " + (outState == null));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState: " + (savedInstanceState == null));
    }

    Intent intent;

    @Override
    public void onClick(View v) {
        if (intent == null)
            intent = new Intent(getApplication(), MyService.class);
        switch (v.getId()) {
            case R.id.stop:
                intent = new Intent(getApplication(), MyService.class);
                stopService(intent);
                break;
            case R.id.start:
                intent = new Intent(getApplication(), MyService.class);
                startService(intent);

                Log.e(TAG, "onClick:  = " + (10 / 3.2));
                Log.e(TAG, "onClick:  = " + (double) (10 / 3));
                Log.e(TAG, "onClick:  = " + ((double) 10 / 3));

                if (bouncingMenu != null && bouncingMenu.isShow()) {
                    bouncingMenu.dismiss();
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < 40; i++) {
                        list.add("item=====" + i);
                    }
                    MyRecyclerAdapter adapter = new MyRecyclerAdapter(list);
                    bouncingMenu = BouncingMenu.makeMenu(ll, R.layout.layout_ru_sweet, adapter).show();
                }
                break;

            case R.id.bind:
                myConnection = new MyConnection();
                bindService(intent, myConnection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                if (myConnection != null) {
                    unbindService(myConnection);
                }
                break;
        }
    }


    public static class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected:  = ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected:  = ");
        }
    }
}
