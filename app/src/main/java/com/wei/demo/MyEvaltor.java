package com.wei.demo;

import android.animation.TypeEvaluator;
import android.util.Log;

import com.wei.demo.bean.PointFLocal;

/**
 * Created by ${wei} on 2017/3/14.
 */

public class MyEvaltor implements TypeEvaluator<PointFLocal> {

    private static final String TAG = "MyEvaltor";

    @Override
    public PointFLocal evaluate(float fraction, PointFLocal startValue, PointFLocal endValue) {
        PointFLocal pointF = new PointFLocal();
        Log.e(TAG, "evaluate: 25 行  percent = " + fraction);
        pointF.x = startValue.x + (endValue.x - startValue.x) * fraction;
        pointF.y = startValue.y + (endValue.y - startValue.y) * fraction;

        Log.e(TAG, "evaluate: 19 行  endX = " + endValue.x + "   endY = " + endValue.y);
        Log.e(TAG, "evaluate: 20 行  x = " + pointF.x + "  y = " + pointF.y + "  fraction = " + fraction);
        return endValue;
    }
}
