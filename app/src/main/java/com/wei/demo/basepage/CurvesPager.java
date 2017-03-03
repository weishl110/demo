package com.wei.demo.basepage;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.demo.bean.ColumnBean;
import com.wei.demo.R;
import com.wei.demo.view.OSPicker;
import com.wei.demo.view.TimeSharingView;
import com.weigan.loopview.LoopView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2017/1/1.
 */
public class CurvesPager extends BasePager {

    private static final String TAG = "zpy_CurvesPager";
    private TimeSharingView timesharing_view;
    private LoopView loopView;

    public CurvesPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(weak.get(), R.layout.layout_curvesview, null);
        timesharing_view = (TimeSharingView) view.findViewById(R.id.timesharing_view);
        TextView tv_get = (TextView) view.findViewById(R.id.tv_get);
        loopView = (LoopView) view.findViewById(R.id.loop_view);
        tv_get.setOnClickListener(this);

        return view;
    }

    @Override
    public void initData() {
        //创建假数据
        h = 9;
        m = 25;
        ArrayList<ColumnBean> list = getData();
        timesharing_view.setData(list);

        ArrayList<String> items = new ArrayList<String>();
//        Collections.addAll(items, "鸡蛋炒饭","鸡排盖浇饭","西红柿鸡蛋汤","鱼香茄子盖饭","牛肉面","刀削面","干拌面","炒河粉");
        for (int i = 0; i < 12; i++) {
            items.add("2016年" + (i + 1) + "月");
        }
        loopView.setNotLoop();
        loopView.setItems(items);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        initData();
    }

    //创建假数据
    int h = 9;
    int m = 25;

    public ArrayList<ColumnBean> getData() {
        ArrayList<ColumnBean> list = new ArrayList<ColumnBean>();
        for (int i = 0; i < 49; i++) {
            ColumnBean columnBean = new ColumnBean();
            float value = (float) (Math.random() * 10000);
            float netValue = (float) (Math.random() * 2);
            value = getDecimal(value);
            columnBean.setValue(value);
            columnBean.setNetValue(get4Decimal(netValue));
            m += 5;
            String mm = m >= 60 ? "00" : String.valueOf(m < 10 ? ("0" + m) : m);
            if (m >= 60) {
                h++;
                m = 0;
            }
            if (h == 11 && m > 30) {
                h = 13;
                m = 5;
                mm = "05";
            }
            columnBean.setDate((h < 10 ? ("0" + h) : h) + ":" + mm);
            list.add(columnBean);
        }
        for (int i = 0; i < list.size(); i++) {
            Log.e(TAG, "getData: value = " + list.get(i).toString());
        }
        return list;

    }

    private float getDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return Float.parseFloat(decimalFormat.format(value));
    }

    private float get4Decimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.0000");
        return Float.parseFloat(decimalFormat.format(value));
    }

    //快速排序
    public void sort(int arr[], int low, int high) {
        int l = low;
        int h = high;
        int povit = arr[low];

        while (l < h) {
            while (l < h && arr[h] >= povit)
                h--;
            if (l < h) {
                int temp = arr[h];
                arr[h] = arr[l];
                arr[l] = temp;
                l++;
            }

            while (l < h && arr[l] <= povit)
                l++;

            if (l < h) {
                int temp = arr[h];
                arr[h] = arr[l];
                arr[l] = temp;
                h--;
            }
        }
        if (l > low) sort(arr, low, l - 1);
        if (h < high) sort(arr, l + 1, high);
    }
}
