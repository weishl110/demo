package com.wei.demo;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wei.demo.adapter.MyRecyclerAdapter;
import com.wei.demo.view.BouncingMenu;

import java.util.ArrayList;

/**
 * Created by ${wei} on 2017/2/9.
 */

public class SencondActivity extends Activity {
    private static final String TAG = "zpy_SencondActivity";

    private BouncingMenu bouncingMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sencond_layout);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        TextView tv = (TextView) findViewById(R.id.tv2);
        tv.setOnClickListener(View->{
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
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
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
}
