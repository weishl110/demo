
package com.wei.demo;

import android.animation.TypeEvaluator;

import com.wei.demo.bean.PointFLocal;

/**
 * Created by ${wei} on 2017/3/15.
 */

public class LineEvaltor implements TypeEvaluator<PointFLocal> {
    private static final String TAG = "LineEvaltor";

    @Override
    public PointFLocal evaluate(float fraction, PointFLocal startValue, PointFLocal endValue) {
        PointFLocal pointF = new PointFLocal();
        pointF.y = startValue.y + (endValue.y - startValue.y) * fraction;
        pointF.x = startValue.x + (endValue.x - startValue.x) * fraction;
        return pointF;
    }
}
