package com.wei.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.wei.demo.R;

/**
 * Created by ${wei} on 2017/7/9.
 */

public class WaveView extends View {

    private static final String TAG = "debug_WaveView";

    private int mWidth, mHeight;
    private Paint mPaint;
    private Path mPath;

    private float dx;//偏移量
    private boolean isStart;

    private float mWaveLength = 600;
    private float mWaveHeight = 150;
    private Region mRegion;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private float halfWidth;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ff0099cc"));
        mPaint.setStyle(Paint.Style.FILL);

        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);

        mPath = new Path();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPathData();
        canvas.clipRect(0, 0, mWidth, mHeight);
        canvas.drawPath(mPath, mPaint);
        Rect rect = mRegion.getBounds();
        int bitmapHalfWidth = mBitmap.getWidth() / 2;
        if (rect.top < mWaveHeight) {
            canvas.drawBitmap(mBitmap, mWidth / 2 - bitmapHalfWidth, rect.top - mBitmap.getHeight(), mBitmapPaint);
        } else {
            canvas.drawBitmap(mBitmap, mWidth / 2 - bitmapHalfWidth, rect.bottom - mBitmap.getHeight(), mBitmapPaint);
        }
        if (rect.right == 0) {
            canvas.drawBitmap(mBitmap, mWidth / 2 - bitmapHalfWidth, mWaveHeight - mBitmap.getHeight(), mBitmapPaint);
        }
    }

    private void setPathData() {
        mPath.reset();
        halfWidth = mWaveLength / 2;
        mPath.reset();
        mPath.moveTo(-mWaveLength + dx, mWaveHeight);//移动至起始点
        //绘制弧度
        for (float i = -mWaveLength; i < mWidth + mWaveLength; i += mWaveLength) {
            mPath.rQuadTo(halfWidth / 2, -mWaveHeight, halfWidth, 0);
            mPath.rQuadTo(halfWidth / 2, mWaveHeight, halfWidth, 0);
        }

        float xCenter = mWidth / 2;
        mRegion = new Region();
        Region clip = new Region((int) (xCenter - 0.1), 0, (int) xCenter, mHeight);
        mRegion.setPath(mPath, clip);


        mPath.lineTo(mWidth, mHeight);//移动至起始点，形成封闭空间
        mPath.lineTo(0, mHeight);
        mPath.close();
        if (!isStart) {
            isStart = true;
            start();
        }
    }

    private void start() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(800);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                dx = mWaveLength * fraction;
                invalidate();
            }
        });
        animator.start();
    }
}
