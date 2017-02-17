package com.wei.demo.basepage;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wei.demo.ColumnBean;
import com.wei.demo.R;
import com.wei.demo.view.bitmap.ColumnView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/1.
 */

public class NewsPager extends BasePager {
    private static final String TAG = "zpy_NewsPager";
    private ColumnView columnView;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(weak.get(), R.layout.layout_newspager, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
        columnView = (ColumnView) view.findViewById(R.id.columview);
        tv.setOnClickListener(View -> {
            initData();
        });
        TextView btn = (TextView) view.findViewById(R.id.btn);
        btn.setSelected(false);
        btn.setOnClickListener(View -> {
            initData();
            btn.setSelected(!btn.isSelected());
        });


        return view;
    }

    @Override
    public void initData() {
        ArrayList<ColumnBean> list = getData();
        columnView.setData(list);
    }

    //创建假数据
    public ArrayList<ColumnBean> getData() {
        ArrayList<ColumnBean> list = new ArrayList<ColumnBean>();
        int tempIndex = 0;
        for (int i = 0; i < 30; i++) {
            ColumnBean columnBean = new ColumnBean();
            double value = (10000 - ((Math.random() * 20000)));
            value = getDecimal(value);
            columnBean.setValue(value);
            if (tempIndex + 1 < 10) {
                columnBean.setDate("2016110" + (tempIndex += 1));
            } else {
                columnBean.setDate("201611" + (tempIndex += 1));
            }
            list.add(columnBean);
        }
        return list;
    }

    private double getDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return Double.parseDouble(decimalFormat.format(value));
    }
}
