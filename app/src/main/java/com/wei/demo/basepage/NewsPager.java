package com.wei.demo.basepage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wei.demo.R;
import com.wei.demo.bean.ColumnBean;
import com.wei.demo.utils.StringUtil;
import com.wei.demo.view.FloatingView;
import com.wei.demo.view.bitmap.AssetsMovementsView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/1.
 */

public class NewsPager extends BasePager {
    private static final String TAG = "zpy_NewsPager";
    private FloatingView floatingview;
    private AssetsMovementsView assetsMovementsView;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(weak.get(), R.layout.layout_newspager, null);
        TextView tv = (TextView) view.findViewById(R.id.btn);
        floatingview = (FloatingView) view.findViewById(R.id.floatingview);
        assetsMovementsView = (AssetsMovementsView) view.findViewById(R.id.assets_movementview);
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
        floatingview.setData(list);
        ArrayList<ColumnBean> assetData = getAssetData();
        assetsMovementsView.setData(assetData);
    }

    //创建假数据
    public ArrayList<ColumnBean> getData() {
        ArrayList<ColumnBean> list = new ArrayList<ColumnBean>();
        int tempIndex = 0;
        for (int i = 0; i < 10; i++) {
            ColumnBean columnBean = new ColumnBean();
            float value = (float) (10000 - ((Math.random() * 20000)));
            value = StringUtil.getDecimal(value);
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

    public ArrayList<ColumnBean> getAssetData() {
        ArrayList<ColumnBean> list = new ArrayList<ColumnBean>();
        int tempIndex = 0;
        for (int i = 0; i < 10; i++) {
            ColumnBean columnBean = new ColumnBean();
            float value = (float) (((Math.random() * 100000)));
            value = StringUtil.getDecimal(value);
            float hasValue = (float) (((Math.random() * 100000)));
            hasValue = StringUtil.getDecimal(hasValue);
            float totalValue = (float) (((Math.random() * 100000)));
            totalValue = StringUtil.getDecimal(totalValue);

            columnBean.setValue(value);
            columnBean.setHasValue(hasValue);
            columnBean.setTotalValue(totalValue);
            if (tempIndex + 1 < 10) {
                columnBean.setDate("2016110" + (tempIndex += 1));
            } else {
                columnBean.setDate("201611" + (tempIndex += 1));
            }
            list.add(columnBean);
        }
        return list;
    }

}
