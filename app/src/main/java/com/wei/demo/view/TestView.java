package com.wei.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ${wei} on 2017/2/9.
 */

public class TestView extends View{
    private int mWidth,mHeight;
    private static final String TAG = "zpy_TestView";

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.mWidth = w;
        this.mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(12);
        paint.setStyle(Paint.Style.STROKE);
        float left = 20;
        float top = 10;
        float right = mHeight-10;
        float bottom = mHeight-10;
        paint.setTextAlign(Paint.Align.LEFT);
        RectF rect = new RectF(left,top,right,bottom);
        paint.setColor(Color.parseColor("#550000ff"));
        canvas.drawArc(rect,0.0f,30.0f,false,paint);
        paint.setColor(Color.parseColor("#00ffff"));
        canvas.drawArc(rect,30.0f,90.0f,false,paint);
        paint.setColor(Color.parseColor("#00ff00"));
        canvas.drawArc(rect,120.0f,180.0f,false,paint);

        Path path = new Path();
        path.moveTo(800,100);
        path.quadTo(50,300,100,100);
        canvas.drawPath(path,paint);
    }
}
