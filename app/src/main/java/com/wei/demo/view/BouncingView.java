package com.wei.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ${wei} on 2017/2/10.
 */

public class BouncingView extends View {

    private static final String TAG = "zpy_BouncingView";
    private Path mPath;
    private int mArcHeight = 10;
    private int mMaxArcHeight = 500;
    private Paint mPaint;
    private Status mStatus = Status.STATUS_NONE;

    public BouncingView(Context context) {
        super(context);
        init();
    }

    public BouncingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BouncingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mMaxArcHeight = 600;
    }

    private enum Status {
        STATUS_NONE, STATUS_UP, STATUS_DOWN;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        int currentPointY = 0;

        switch (mStatus) {
            case STATUS_NONE:
                currentPointY = 0;
                break;
            case STATUS_DOWN:
                currentPointY = -mMaxArcHeight;
                break;
            case STATUS_UP:
                currentPointY = (int) ((getHeight() * (1 - (float) mArcHeight / mMaxArcHeight)) + mMaxArcHeight);
                break;
        }

        mPath.reset();
        mPath.moveTo(0, currentPointY);
        mPath.quadTo(getWidth() / 2, -(currentPointY + mArcHeight), getWidth(), currentPointY);
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

    public void show() {
        mStatus = Status.STATUS_UP;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mMaxArcHeight);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArcHeight = (int) animation.getAnimatedValue();
                if (mArcHeight == mMaxArcHeight) {
//                    bounce();
                }
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    public void bounce() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mMaxArcHeight, 0);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArcHeight = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }
}
