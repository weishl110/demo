package com.wei.demo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.wei.demo.bean.CircleBean;
import com.wei.demo.bean.CircleValueBean;

import java.util.ArrayList;

/**
 * Created by ${wei} on 2017/3/11.
 * <p>
 * 饼图
 */

public class CircleView extends View {

    private int mValue;
    //画笔的宽度
    private int mPaintWidth;

    private float margin = 13, textSize = 13;
    private float blockSize = 14, verticalSrec = 30;
    private RectF rectF;
    private Paint mPaint;
    //圆的半径
    private float mRadius, mCircleCenter, mPhase = 0.0f;

    //颜色值
    private final static String BLUE = "#00bb00", RED = "#ff0000", BLACK = "#000000", GREEN = "#0000ff", PURPLE = "#da5a5a", YELLOW = "#9932cc";

    private final static String[] colors = {BLUE, RED, BLACK, GREEN, PURPLE, YELLOW};

    private ArrayList<CircleValueBean> list;
    private ArrayList<CircleBean> arcList;
    private ObjectAnimator animator;

    private boolean isAnimatorEnd = false;
    private int mWidth, mHeight, originalHeight;
    private double centerY, centerX;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundResource(android.R.color.white);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextAlign(Paint.Align.RIGHT);

        margin = dp2px(margin);
        blockSize = dp2px(blockSize);
        textSize = dp2px(textSize);
        //创建矩形
        rectF = new RectF();

        animator = ObjectAnimator.ofFloat(this, "phase", 0.0f, 1.0f);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimatorEnd = true;
            }
        });
    }

    /**
     * 动画使用
     */
    public void setPhase(float mPhase) {
        this.mPhase = mPhase;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //宽和高的最小值
        mValue = Math.min(w, h);
        //画笔的宽度
        mPaintWidth = mValue / 9;
        mPaint.setStrokeWidth(mPaintWidth);
        originalHeight = h;

        mWidth = (int) (w - getPaddingLeft() - getPaddingRight() - mPaintWidth * 2f);
        mHeight = (int) (h - getPaddingBottom() - getPaddingTop() - mPaintWidth * 2f);
        mRadius = (float) Math.min(mWidth, mHeight) / 2;
        //圆心
        mCircleCenter = mValue / 2;
        centerX = (double) w / 2;
        centerY = (double) h / 2;


        //固定矩形的位置
        rectF.left = (float) mWidth / 2 - mRadius + mPaintWidth * 2f / 2;
        rectF.top = (float) h / 2 - mRadius + getPaddingTop();
        rectF.right = rectF.left + mRadius * 2;
        rectF.bottom = rectF.top + mRadius * 2;
    }

    RectF tempRectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (arcList == null || arcList.size() == 0) return;

        int size = arcList.size();
        float startAngle = 0.0f, sweepAngle = 0.0f;
        boolean userCenter = false;
        for (int i = 0; i < size; i++) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mPaintWidth);
//            mPaint.setStyle(Paint.Style.STROKE);
            CircleBean circleBean = arcList.get(i);
            startAngle = circleBean.getStartAngle() * mPhase;
            sweepAngle = circleBean.getSweepAngle() * mPhase;
            int color = Color.parseColor(circleBean.getColor());
            mPaint.setColor(color);
            if (i == 4) {
                double angleValue = ((startAngle + sweepAngle / 2)) * Math.PI / 180;
                float stopX = (float) (centerX + (30 * Math.cos(angleValue)));
                float stopY = (float) (centerY + (30 * Math.sin(angleValue)));
                tempRectF.set(stopX - mRadius, stopY - mRadius, stopX + mRadius, stopY + mRadius);
                drawArc(canvas, tempRectF, startAngle, sweepAngle, userCenter, mPaint);
            } else {
                drawArc(canvas, rectF, startAngle, sweepAngle, userCenter, mPaint);
            }
            //根据角度获取圆的坐标点，并连接线
//            CircleBean circleBean = arcList.get(i);
            double angleValue = (startAngle + sweepAngle) * Math.PI / 180;
            float stopX = (float) (centerX + (mRadius * Math.cos(angleValue)));
            float stopY = (float) (centerY + (mRadius * Math.sin(angleValue)));
            mPaint.setStrokeWidth(3);
            canvas.drawLine((float) centerX, (float) centerY, stopX, stopY, mPaint);
        }
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle((float) centerX, (float) centerY, 30, mPaint);//绘制白色空心圆
        if (isAnimatorEnd) {
            RectF rectF1 = new RectF();
            float top = originalHeight / 2 - blockSize * 3 - verticalSrec * 2;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(textSize);
            paint.setStrokeWidth(2);
            for (int i = 0; i < size; i++) {
                rectF1.left = margin * 2;
                rectF1.top = top + i * (blockSize + verticalSrec);
                rectF1.right = rectF1.left + blockSize;
                rectF1.bottom = rectF1.top + blockSize;
                paint.setColor(Color.parseColor(arcList.get(i).getColor()));
                canvas.drawRect(rectF1, paint);
                canvas.drawText(list.get(i).getName(), rectF1.left + verticalSrec * 2, rectF1.bottom - 5, paint);
            }
            isAnimatorEnd = false;
            paint.setColor(Color.BLACK);
        }
    }


    /**
     * 绘制圆弧
     *
     * @param canvas     画笔
     * @param rectF      矩形
     * @param startAngle 起始角度
     * @param sweepAngle 绘制的角度
     * @param useCenter  是否包括圆心
     * @param paint      画笔
     */
    private void drawArc(Canvas canvas, RectF rectF, float startAngle, float sweepAngle, boolean useCenter, Paint paint) {
        canvas.drawArc(rectF, startAngle, sweepAngle, useCenter, paint);
    }


    public void setData(ArrayList<CircleValueBean> list) {
        if (list == null || list.size() == 0) return;
        this.list = list;
        calculateAngle(list);
        animator.start();
    }

    /**
     * 计算角度值
     *
     * @param list
     */
    private void calculateAngle(ArrayList<CircleValueBean> list) {

        arcList = new ArrayList<>();
        int size = list.size();
        float totalValue = 0;
        int i = 0;
        for (; i < size; i++) {
            CircleValueBean circleValueBean = list.get(i);
            float value = circleValueBean.getValue();
            totalValue += value;
        }
        //每一个角度的值
        float angleValue = 360 / totalValue;

        //计算每一个的值，并给每个角度的颜色
        float preAngle = 270;
        for (i = 0; i < size; i++) {
            CircleValueBean circleValueBean = list.get(i);
            float value = circleValueBean.getValue();
            float angle = value * angleValue;
            CircleBean circleBean = new CircleBean();
            circleBean.setStartAngle(preAngle);
            circleBean.setSweepAngle(angle);
            circleBean.setColor(colors[i]);
            arcList.add(circleBean);
            preAngle += angle;
        }
    }

    private float dp2px(float dpValue) {
        float density = getResources().getDisplayMetrics().density;
        return dpValue * density + 0.5f;

    }
}
