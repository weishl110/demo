package com.wei.demo.basepage;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.demo.R;
import com.wei.demo.view.RefreshView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/1.
 */

public class TestPager extends BasePager {

    private TextView tv, tv_get;
    private ListView lv;
    private RefreshView refresh_view;
    private ArrayList<String> list;

    private Handler mHandler = new Handler();

    public TestPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(weak.get(), R.layout.layout_testpaer, null);
        tv = (TextView) view.findViewById(R.id.tv);
        tv_get = (TextView) view.findViewById(R.id.tv_get);
        lv = (ListView) view.findViewById(R.id.list_view);
        tv.setOnClickListener(this);
        tv_get.setOnClickListener(this);

        refresh_view = (RefreshView) view.findViewById(R.id.refresh_view);
        refresh_view.setOnRefreshListener(new RefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh_view.refreshFinish();
                    }
                }, 5000);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("item-----" + i);
        }
        lv.setAdapter(new ArrayAdapter<String>(weak.get(), android.R.layout.simple_list_item_1, list) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(25);
                tv.setText(list.get(position));
                return tv;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(weak.get(), "position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv:
                refresh_view.refreshFinish();
                break;
            case R.id.tv_get:
                break;
        }
    }

}
