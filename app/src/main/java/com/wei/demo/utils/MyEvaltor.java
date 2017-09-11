package com.wei.demo.utils;

import android.animation.TypeEvaluator;
import android.util.Log;

import com.wei.demo.bean.PointFLocal;

/**
 * Created by ${wei} on 2017/3/14.
 * 曲线动画估值器
 */

public class MyEvaltor implements TypeEvaluator<PointFLocal> {
    @Override
    public PointFLocal evaluate(float fraction, PointFLocal startValue, PointFLocal endValue) {
        PointFLocal pointF = new PointFLocal();
        pointF.x = startValue.x + (endValue.x - startValue.x) * fraction;
        pointF.y = startValue.y + (endValue.y - startValue.y) * fraction;
        return endValue;
    }
}
