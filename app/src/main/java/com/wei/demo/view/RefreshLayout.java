package com.wei.demo.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ${wei} on 2017/10/26.
 */

public class RefreshLayout extends SwipeRefreshLayout {

    private static final String TAG = "debug_RefreshLayout";
    private RecyclerView recyclerView;

    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setChildView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager){
            ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }else if(layoutManager instanceof GridLayoutManager){
            ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        android.util.Log.e(TAG, "onInterceptTouchEvent:  = ");


        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        android.util.Log.e(TAG, "onTouchEvent:  = ");
        return super.onTouchEvent(ev);
    }
}
