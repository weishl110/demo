package com.wei.demo.basepage;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineHeightSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wei.demo.view.ChartView;
import com.wei.demo.ColumnBean;
import com.wei.demo.R;
import com.wei.demo.view.bitmap.ColumnView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/1.
 */

public class ColumViewPager extends BasePager {

    private static final String TAG = "zpy_ColumViewPager";

    private ChartView chartview;
    private TextView btn_get;
    private ColumnView columnView;

    public ColumViewPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(weak.get(), R.layout.layout_columview, null);
        btn_get = (TextView) view.findViewById(R.id.get);
        chartview = (ChartView) view.findViewById(R.id.chartview);
        columnView = (ColumnView) view.findViewById(R.id.columview);
        btn_get.setOnClickListener(this);
        btn_get.setOnClickListener(View -> {
            initData();
        });
        return view;
    }

    /**
     * @param value
     * @param negativeColor
     * @param isColor
     * @param isSymbol
     * @param isPercentType
     * @return
     */
    public static SpannableString converTextColor(double value, int negativeColor, int isColor, boolean isSymbol, boolean isPercentType) {
        String text = "";
        text = isPercentType ? String.format("%s%s", value, "%") : String.valueOf(value);
        if (value < 0) {
            return setTextColor(text, negativeColor, 0, text.length());
        } else {
            if (value > 0) {
                text = String.format("%s%s", "+", text);
                return setTextColor(text, isColor, 0, text.length());
            }
            return setTextColor(text, isColor, 0, text.length());
        }
    }

    public static SpannableString setTextColor(String text, int finalColor, int startIndex, int endIndex) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new ForegroundColorSpan(finalColor), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    public static SpannableString setTextColorAndSize(String text, int finalColor, int textsize, int startIndex, int endIndex, boolean isSetSize) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new ForegroundColorSpan(finalColor), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        if (isSetSize) {
            spanString.setSpan(new AbsoluteSizeSpan(textsize, true), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanString;
    }

    @Override
    public void initData() {
        ArrayList<ColumnBean> list = getData();
        columnView.setData(list);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.get:
                columnView.setData(getData());
                chartview.setData(getData());
                String text = "10432.42%";
                SpannableString spannableString = setTextColorAndSize(text, Color.RED, 10, text.indexOf("%"), text.length(), true);
                Log.e(TAG, "initData: spannable = " + spannableString);
                DisplayMetrics metrics = weak.get().getResources().getDisplayMetrics();
                float density = metrics.density;
                int densityDpi = metrics.densityDpi;
                float scaledDensity = metrics.scaledDensity;
                DisplayMetrics displayMetrics = weak.get().getResources().getDisplayMetrics();
                int widthPixels = displayMetrics.widthPixels;
                int heightPixels = displayMetrics.heightPixels;
                int width = btn_get.getWidth();
                Log.e(TAG, "onClick:  px2dp = " + px2dp(width));
                double screenSize = getScreenSizeOfDevice();
                double v1 = Math.sqrt(widthPixels * widthPixels + heightPixels * heightPixels) / screenSize;
                Log.e(TAG, "onClick: sprt = " + v1);
                Log.e(TAG, "onClick: density = " + density + "   densityDpi = " + densityDpi + "   scaleDensity = " + scaledDensity + "  width = " + btn_get.getWidth());
                break;
            default:
                break;
        }
    }

    private float dp2px(int dpValue) {
        float density = weak.get().getResources().getDisplayMetrics().density;
        return dpValue * density + 0.5f;
    }

    private float px2dp(int pxValue) {
        float density = weak.get().getResources().getDisplayMetrics().density;
        return pxValue / density + 0.5f;
    }

    private double getScreenSizeOfDevice() {
        DisplayMetrics dm = weak.get().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double x = Math.pow(width, 2);
        double y = Math.pow(height, 2);
        double diagonal = Math.sqrt(x + y);

        int dens = dm.densityDpi;
        double screenInches = diagonal / (double) dens;
        Log.d(TAG, "The screenInches " + screenInches);
        return screenInches;
    }


    //创建假数据
    public ArrayList<ColumnBean> getData() {
        ArrayList<ColumnBean> list = new ArrayList<ColumnBean>();
        int tempIndex = 10;
        for (int i = 0; i < 30; i++) {
            ColumnBean columnBean = new ColumnBean();
            double value = (10000 - ((Math.random() * 20000)));
            value = getDecimal(value);
            columnBean.setValue(value);
            if (tempIndex + 1 < 10) {
                columnBean.setDate("2016-11-0" + (tempIndex +=1));
            } else {
                columnBean.setDate("2016-11-" + (tempIndex += 1));
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
