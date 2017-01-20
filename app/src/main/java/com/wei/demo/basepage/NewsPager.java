package com.wei.demo.basepage;

import android.content.Context;
import android.view.View;

import com.wei.demo.R;

/**
 * Created by Administrator on 2017/1/1.
 */

public class NewsPager extends BasePager {
    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(weak.get(), R.layout.layout_newspager, null);
        return view;
    }

    @Override
    public void initData() {

    }
}
