package com.wei.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${wei} on 2017/5/12.
 * 圆点loading
 */

public class LoadingView extends View {

    private int mHeight, mWidth;
    private int totalPoint = 14;//点的数量
    private float mFixedSpec = 2.5f;

    private final String COLOR_WHITE = "#ffffff";

    private List<PointInfo> mPoints = new ArrayList<>();
    private float mCenterY = 0.0f, mCenterX = 0.0f;
    private Paint mPaint;
    /**
     * 外圆最大半径
     */
    private float mMaxRadius;//大圆半径

    /**
     * 旋转控制点
     */
    private float animatorAngle = 1.0f;
    /**
     * 平均角度
     */
    private double mAngle = 0.0;
    /**
     * 两点之间的距离
     */
    private double mPointValue = 0.0;
    /**
     * 是否开始旋转
     */
    private boolean isStart = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(COLOR_WHITE));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = ((float) w - getPaddingLeft() - getPaddingRight()) / 2.0f;
        mCenterY = ((float) h - getPaddingTop() - getPaddingBottom()) / 2.0f;
        mMaxRadius = Math.min(mCenterX, mCenterY);
        mFixedSpec = dp2px(mFixedSpec);

        //每个点之间的角度值 一共显示14个点
        mAngle = (double) 360 / totalPoint;
        //计算原点的最大半径
        float pointMaxRadius = calcuatePointSpec();
        //将外圆半径减小最大圆的半径，需重新计算外圆半径
        mMaxRadius = mMaxRadius - pointMaxRadius / 1.3f;
        pointMaxRadius = calcuatePointSpec();
        //半径的平均点
        mPointValue = (pointMaxRadius / (float) totalPoint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calcuateLoca();//计算点的位置
        int size = mPoints.size();
        for (int i = 0; i < size; i++) {
            PointInfo pointInfo = mPoints.get(i);
            canvas.drawCircle(pointInfo.getCx(), pointInfo.getCy(), pointInfo.getRadius(), mPaint);
        }
        if (isStart) {
            animatorAngle += 7.0f;
            if (animatorAngle >= 360) {
                animatorAngle = 1.0f;
            }
            postInvalidate();
        }
    }

    //计算点的位置
    private void calcuateLoca() {
        mPoints.clear();
        for (int i = 0; i < totalPoint; i++) {
            float[] cxCy = getCxCy(mAngle, i, mMaxRadius);
            float radius = (float) ((i + 1) * mPointValue);
            PointInfo pi = new PointInfo(cxCy[0], cxCy[1], radius);
            mPoints.add(pi);
        }
    }

    private float calcuatePointSpec() {
        //计算两点间的距离
        double pointSpec = 2 * mMaxRadius * Math.sin(((double) 180) / (double) totalPoint);
        return (float) ((pointSpec - mFixedSpec) / 2);
    }

    /**
     * 根据角度和索引计算x y点的位置
     *
     * @param angle 角度
     * @param index 索引
     * @return
     */
    private float[] getCxCy(double angle, int index, float maxRadius) {
        double angleValue = (index * angle + angle + animatorAngle) * Math.PI / 180;//转换角度
        float cx = (float) (mCenterX + maxRadius * Math.cos(angleValue));
        float cy = (float) (mCenterY + maxRadius * Math.sin(angleValue));
        return new float[]{cx, cy};
    }

    private float dp2px(float dpValue) {
        float density = getResources().getDisplayMetrics().density;
        return dpValue * density + 0.5f;
    }

    public void startAnimator(boolean isStart) {
        this.isStart = isStart;
        if (isStart) {
            postInvalidate();
        } else {
            mPoints.clear();
        }
    }

    public boolean isStart() {
        return isStart;
    }

    private class PointInfo {
        private float cx;
        private float cy;
        private float radius;

        public PointInfo(float cx, float cy, float radius) {
            this.cx = cx;
            this.cy = cy;
            this.radius = radius;
        }

        public float getCx() {
            return cx;
        }

        public void setCx(float cx) {
            this.cx = cx;
        }

        public float getCy() {
            return cy;
        }

        public void setCy(float cy) {
            this.cy = cy;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }
    }
}

