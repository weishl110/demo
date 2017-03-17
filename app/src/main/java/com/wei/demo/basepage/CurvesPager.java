package com.wei.demo.basepage;

import android.content.Context;
import android.view.View;

import com.wei.demo.R;
import com.wei.demo.adapter.MyRecyclerAdapter;
import com.wei.demo.bean.ColumnBean;
import com.wei.demo.factory.DialogFactory;
import com.wei.demo.view.BouncingMenu;
import com.wei.demo.view.TimeSharingView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/1.
 */
public class CurvesPager extends BasePager {

    private static final String TAG = "zpy_CurvesPager";
    private TimeSharingView timesharing_view;
    private BouncingMenu bouncingMenu;

    public CurvesPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(weak.get(), R.layout.layout_curvesview, null);
        timesharing_view = (TimeSharingView) view.findViewById(R.id.timesharing_view);
        view.findViewById(R.id.tv_get).setOnClickListener(this);
        view.findViewById(R.id.tv_dialog).setOnClickListener(this);
        view.findViewById(R.id.tv_jump).setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        //创建假数据
        h = 9;
        m = 25;
        ArrayList<ColumnBean> list = getData();
        timesharing_view.setData(list);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_get:
                initData();
                break;
            case R.id.tv_dialog:
                ArrayList<String> items = new ArrayList<String>();
                for (int i = 0; i < 12; i++) {
                    items.add("2016年" + (i + 1) + "月");
                }
                if (DialogFactory.isShow()) {
                    DialogFactory.dismiss();
                }
                DialogFactory.createDialog(weak.get(), items, false);
                DialogFactory.show();
                break;
            case R.id.tv_jump:
                if (bouncingMenu != null && bouncingMenu.isShow()) {
                    bouncingMenu.dismiss();
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < 40; i++) {
                        list.add("item=====" + i);
                    }
                    MyRecyclerAdapter adapter = new MyRecyclerAdapter(list);
                    bouncingMenu = BouncingMenu.makeMenu(timesharing_view, R.layout.layout_ru_sweet, adapter).show();
                }
                break;
        }
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
//        for (int i = 0; i < list.size(); i++) {
//            Log.e(TAG, "getData: value = " + list.get(i).toString());
//        }
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
            //从后往前排
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
