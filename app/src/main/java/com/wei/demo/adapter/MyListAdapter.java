package com.wei.demo.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/31.
 */

public class MyListAdapter extends BaseAdapter {
    private final ArrayList<String> list;

    public MyListAdapter() {
        list = new ArrayList<>();
        list.add("left_item1");
        list.add("left_item2");
        list.add("left_item3");
        list.add("left_item4");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(parent.getContext(), android.R.layout.simple_list_item_1, null);
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(25);
        tv.setText(getItem(position));
        return view;
    }
}
