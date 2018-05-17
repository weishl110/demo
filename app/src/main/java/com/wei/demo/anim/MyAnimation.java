package com.wei.demo.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * Created by ${wei} on 2017/12/26.
 */

public class MyAnimation extends Animation {

    private final Camera mCamera;
    private int centerX, centerY;
    private int startAngle;

    public MyAnimation(int angle) {
        mCamera = new Camera();
        this.startAngle = angle;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width / 2;
        centerY = height / 2;
        setDuration(1000);
        setInterpolator(new DecelerateInterpolator());
    }

    private static final String TAG = "debug_MyAnimation";

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Matrix matrix = t.getMatrix();
        mCamera.save();
        float angle = startAngle * interpolatedTime;
        mCamera.rotateZ(angle);
//        mCamera.rotateX(angle);
        mCamera.rotateY(angle);
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        mCamera.restore();
    }
}
