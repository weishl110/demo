package com.wei.demo.view.bitmap;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.wei.demo.bean.ColumnBean;

import java.util.ArrayList;

/**
 * Created by ${wei} on 2017/2/21.
 */

public class AssetsMovementsView extends BaseChartView {
    public AssetsMovementsView(Context context) {
        super(context);
    }

    public AssetsMovementsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AssetsMovementsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawLeftText(Canvas canvas) {

    }

    @Override
    protected void drawLine(Canvas canvas) {

    }

    @Override
    protected void drawLong(Canvas canvas) {

    }


    public void setData(ArrayList<ColumnBean> list){}
}
