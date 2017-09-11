package com.wei.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.Serializable;

public class ResultAnimation extends View implements
        ValueAnimator.AnimatorUpdateListener{
//    private static final String TAG = "debug_ResultAnimation";
    private Context mContext;
    private Paint mPaint;
    /**
     * Path和对应的空Path用来填充
     */

    private Path mPathRight;
    private Path mPathRightDst;
    /**
     * Path管理
     */
    private PathMeasure mPathMeasure;
    /**
     * 动画
     */
    private ValueAnimator mRightAnimator;
    /**
     * 当前绘制进度占总Path长度百分比
     */
    private float mRightPercent;
    /**
     * 线宽
     */
    private int mLineWidth;
    /**
     * 正确动画 错误动画
     */
    public static final int RESULT_RIGHT = 1;
    /**
     * 当前结果类型
     */
    private int mResultType = RESULT_RIGHT;
    private float temp1;
    private float temp2;
    private float temp3;

    private void init() {
        mLineWidth = dp2px(3);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        initPath();
    }

    private void initPath() {
        mPathRight = new Path();
        mPathRightDst = new Path();

        mPathMeasure = new PathMeasure();

        //实例化对象
        //设置时长为1000ms
        //开始动画
        //设置动画监听

        mRightAnimator = ValueAnimator.ofFloat(0, 1);
        mRightAnimator.setDuration(1000);
        mRightAnimator.addUpdateListener(this);
        mRightAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRightPercent == 1) {
//            canvas.drawColor(Color.parseColor("#000000"));
//            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            mPathRight.reset();
//            mPathRightDst.reset();
//            mRightAnimator.start();
        }
        drawRight(canvas);
    }

    private void drawRight(Canvas canvas) {
        //画对勾
        mPathRight.moveTo(temp1, temp2);
        mPathRight.lineTo(temp2, temp3);
        mPathRight.lineTo(temp3, temp1);
//            Log.e(TAG, "onDraw: mRightPercent == " + mRightPercent);
        mPathMeasure.nextContour();
        mPathMeasure.setPath(mPathRight, false);
        mPathMeasure.getSegment(0, mRightPercent * mPathMeasure.getLength(), mPathRightDst, true);
        canvas.drawPath(mPathRightDst, mPaint);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mRightPercent = (float) animation.getAnimatedValue();
//        Log.e(TAG, "onAnimationUpdate: mRightPercent == " + mRightPercent);
        invalidate();
//        if (mRightPercent == 1.0) {
//            Log.e(TAG, "onAnimationUpdate: ---------------------");
//            mRightAnimator.start();
//        }
    }

    public void setmResultType(int mResultType) {
        this.mResultType = mResultType;
        invalidate();
    }

    /**
     * ----------------------------------------------------------------------------------------------------------------------
     */
    /**
     * 固定写死了宽高，可重新手动调配
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(dp2px(50), dp2px(50));
    }

    public ResultAnimation(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ResultAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ResultAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        temp1 = getWidth() / 4;
        temp2 = getWidth() / 2;
        temp3 = temp1 * 3;
//        mPathRight.lineTo(getWidth() / 2, getWidth() / 4 * 3);
//        mPathRight.lineTo(getWidth() / 4 * 3, getWidth() / 4);
    }

    private int dp2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }
}