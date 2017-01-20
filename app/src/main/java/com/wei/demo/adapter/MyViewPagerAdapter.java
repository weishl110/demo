package com.wei.demo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wei.demo.basepage.BasePager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/31.
 */

public class MyViewPagerAdapter extends PagerAdapter {

    private ArrayList<BasePager> pagerList;
    private Context context;


    public MyViewPagerAdapter(Context context, ArrayList<BasePager> pagerList) {
        this.context = context;
        this.pagerList = pagerList;
    }

    @Override
    public int getCount() {
        return pagerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager basePager = pagerList.get(position);
        container.addView(basePager.rootView);
        return basePager.rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
