package com.wei.demo.view.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ${wei} on 2017/5/16.
 */

public class LineProgressView extends View {

    private Paint mNomalPaint;
    private Paint mProgressPaint;
    private final String COLOR_BLUE = "#429AE6";
    private final String COLOR_GRAY = "#999999";

    private float nomalPadding = 6;
    private float mRadius = 10;

    /**
     * 进度条的高度
     */
    private float mLineHeight;
    private Context mContext;
    private RectF mNomalRectF;
    private RectF mProgressRectF;
    private int mWidth;
    private int mHeight;
    private float mLineWidth;

    public LineProgressView(Context context) {
        this(context, null);
    }

    public LineProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mNomalPaint = new Paint();
        mNomalPaint.setAntiAlias(true);
        mNomalPaint.setColor(Color.parseColor(COLOR_GRAY));

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.parseColor(COLOR_BLUE));

        mRadius = dp2px(mRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - getPaddingLeft() - getPaddingRight();
        mHeight = h - getPaddingTop() - getPaddingBottom();
        mLineHeight = (float) mHeight / 4;
        nomalPadding = dp2px(nomalPadding);
        //进度条的矩形
        mLineWidth = mWidth - nomalPadding * 2;
        mNomalRectF = new RectF();
        mNomalRectF.left = nomalPadding;
        mNomalRectF.top = mLineHeight * 1.5f;
        mNomalRectF.right = mWidth - nomalPadding;
        mNomalRectF.bottom = mNomalRectF.top + mLineHeight;
        //设置画笔宽度
        mNomalPaint.setStrokeWidth(mLineHeight);
        mProgressPaint.setStrokeWidth(mLineHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //进度条的默认状态
        canvas.drawRoundRect(mNomalRectF, mRadius, mRadius, mNomalPaint);
        //绘制进度状态 不断的改变右边的值
        if (mProgressRectF != null) {
            canvas.drawRoundRect(mProgressRectF, mRadius, mRadius, mProgressPaint);
        }
    }

    /**
     * 0~1
     *
     * @param currProgress
     */
    public void setPercent(float currProgress) {
        if (mProgressRectF == null) {
            mProgressRectF = new RectF();
            mProgressRectF.left = mNomalRectF.left;
            mProgressRectF.top = mNomalRectF.top;
            mProgressRectF.bottom = mNomalRectF.bottom;
        }
        if (currProgress > 1) {
            currProgress = 1;
        }
        float right = mProgressRectF.left + currProgress * mLineWidth;
        mProgressRectF.right = right;
        postInvalidate();
    }

    private float dp2px(float dpValue) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return dpValue * density + 0.5f;
    }
}
