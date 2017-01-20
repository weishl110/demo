package com.wei.demo.basepage;

import android.content.Context;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/1/1.
 */

public abstract class BasePager implements View.OnClickListener {

    protected WeakReference<Context> weak;
    public View rootView;

    public BasePager(Context context) {
        weak = new WeakReference<Context>(context);
        rootView = initView();
    }


    public abstract View initView();

    public abstract void initData();

    @Override
    public void onClick(View v) {

    }
}
