package com.wei.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.demo.R;
import com.wei.demo.adapter.ChannelAdapter;
import com.wei.demo.bean.ChannelTab;
import com.wei.demo.listener.ItemDragHelperCallback;
import com.wei.demo.utils.ConstansValue;

import java.util.List;

/**
 * Created by ${wei} on 2017/2/9.
 */

public class SencondActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "debug_SencondActivity";
    RecyclerView recyclerView_top;
    RecyclerView recyclerView_bottom;
    private ChannelAdapter bottomAdapter, topAdapter;
    private List<ChannelTab> tabsTop, tabsBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sencond_layout);

        recyclerView_top = (RecyclerView) findViewById(R.id.recyclerview_top);
        recyclerView_bottom = (RecyclerView) findViewById(R.id.recyclerview_bottom);
        initDate();
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

    @Override
    public void onClick(View v) {

    }

    protected void initDate() {
        tabsTop = toList(ConstansValue.json2);
        tabsBottom = toList(ConstansValue.json);

        topAdapter = new ChannelAdapter(this, R.layout.layout_item_news, tabsTop);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        recyclerView_top.setItemAnimator(new DefaultItemAnimator());
        recyclerView_top.setLayoutManager(layoutManager);
        recyclerView_top.setAdapter(topAdapter);

        bottomAdapter = new ChannelAdapter(this, R.layout.layout_item_news, tabsBottom);
        recyclerView_bottom.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        recyclerView_bottom.setLayoutManager(layoutManager);
        recyclerView_bottom.setAdapter(bottomAdapter);

        topAdapter.setOnItemClickListener(new ChannelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ChannelTab channelTab = tabsTop.get(position);
                bottomAdapter.add(channelTab);
                topAdapter.removeAt(position);
                topAdapter.notifyDataSetChanged();
                bottomAdapter.notifyDataSetChanged();
            }
        });

        bottomAdapter.setOnItemClickListener(new ChannelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ChannelTab channelTab = tabsBottom.get(position);
                topAdapter.add(channelTab);
                bottomAdapter.removeAt(position);
                topAdapter.notifyDataSetChanged();
                bottomAdapter.notifyDataSetChanged();
            }
        });

        ItemDragHelperCallback topDragHelper = new ItemDragHelperCallback(topAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(topDragHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView_top);
        topAdapter.setItemDragHelperCallback(topDragHelper);

        ItemDragHelperCallback bottomDragHelper = new ItemDragHelperCallback(bottomAdapter);
        itemTouchHelper = new ItemTouchHelper(bottomDragHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView_bottom);
        bottomAdapter.setItemDragHelperCallback(bottomDragHelper);
    }

    private <T> T toList(String json) {
        Gson gson = new Gson();
        List<ChannelTab> datas = gson.fromJson(json, new TypeToken<List<ChannelTab>>() {
        }.getType());
        return (T) datas;
    }
}
