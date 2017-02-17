package com.wei.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.wei.demo.view.bitmap.BaseChartView;

/**
 * Created by ${wei} on 2017/2/16.
 */

public class FloatingView extends BaseChartView {
    private int mWidth,mHeight;
    private float mAvarageLine;
    //默认线的颜色
    private static final String LINECOLOR = "#666666";
    //负值的颜色
    private static final String BLUECOLOR = "#8800ffff";
    //正值的颜色
    private static final String REDCOLOR = "#666666";
    //边距
    private final int MARGIN = 0;
    private final int MARGINTOP = 24;
    private final int MARGINBOTTOM = 24;
    public FloatingView(Context context) {
        super(context);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    @Override
    protected void drawLeftText(Canvas canvas) {

    }

    @Override
    protected void drawLong(Canvas canvas) {

    }

    @Override
    protected void drawLine(Canvas canvas) {

    }

    /**
     * 虚线及边框的画笔
     */
    protected Paint getLinePaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STOREWIDTH);
        paint.setAntiAlias(true);
        paint.setAlpha(0);
        paint.setColor(Color.parseColor(COLOR_BOX));
        return paint;
    }
}
