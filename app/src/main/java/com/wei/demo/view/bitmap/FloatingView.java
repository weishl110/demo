package com.wei.demo.view.bitmap;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.Toast;

import com.wei.demo.utils.MyEvaltor;
import com.wei.demo.bean.ColumnBean;
import com.wei.demo.bean.PointFLocal;

import java.util.ArrayList;

/**
 * Created by ${wei} on 2017/2/16.
 * 折线图
 */

public class FloatingView extends BaseChartView {
    private static final String TAG = "debug_FloatingView";
    //默认线的颜色
    private static final String LINECOLOR = "#99ff5a00";
    //背景颜色
    private static final String BGSTARTCOLOR = "#66ff5a00";
    private static final String BGENDCOLOR = "#11ff5a00";
    //负值的颜色
    private static final String BLUECOLOR = "#8800ffff";
    //正值的颜色
    private static final String REDCOLOR = "#666666";
    //点的间距
    private float mPointSpec;

    private ArrayList<ColumnBean> list;
    private String[] leftValues;
    private ArrayList<PointFLocal> pointFs;
    private Path alphaPath;
    private ObjectAnimator objectAnimator;

    public FloatingView(Context context) {
        super(context);
        init();
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private float stopX, stopY;

    public void setPointFs(PointFLocal pointF) {
        stopX = pointF.x;
        stopY = pointF.y;
        resetAlphaPath(list, list.get(getMaxValueIndex()).getValue());//重新
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (list != null && list.size() > 0) {
            mPointSpec = (float) (mWidth - STOREWIDTH) / (list.size() - 1);
            //计算点的位置
            pointFs = calculatePointLocal(list, list.get(getMaxValueIndex()).getValue());

            objectAnimator = ObjectAnimator.ofObject(this, "pointFs", new MyEvaltor(), pointFs.toArray());
            objectAnimator.setDuration(1500);
            objectAnimator.start();
        }
    }

    @Override
    protected void drawLeftText(Canvas canvas) {
        //计算左侧数值
        if (leftValues != null && leftValues.length > 0) {
            Paint paint = getTextPaint(COLOR_LEFTTEXT, textsize);
            drawLeftText(canvas, leftValues, paint);
        }
    }

    @Override
    protected void drawLong(Canvas canvas) {
        drawLongBottomDateAndDeshLine(canvas, pointFs, list, longDownX, "yyyyMMdd", "yyyy-MM-dd");
    }

    @Override
    protected void drawLine(Canvas canvas) {
        Paint paint = getLinePaint();
        paint.setColor(Color.parseColor(LINECOLOR));
        paint.setStrokeWidth(STOREWIDTH);
        paint.setStyle(Paint.Style.STROKE);
        int size = pointFs.size();
        for (int i = 0; i < size - 1; i++) {
            PointFLocal pointF = pointFs.get(i);
            PointFLocal nextPointF = pointFs.get(i + 1);
            if (nextPointF.x > stopX) {
                canvas.drawLine(pointF.x, pointF.y, stopX, stopY, paint);
                break;
            } else {
                canvas.drawLine(pointF.x, pointF.y, nextPointF.x, nextPointF.y, paint);
            }
        }


        //绘制渐变背景
        float x = pointFs.get(0).x;
        LinearGradient linearGradient = new LinearGradient(x, marginTop, 0, marginTop + mHeight,
                Color.parseColor(BGSTARTCOLOR), Color.parseColor(BGENDCOLOR), Shader.TileMode.MIRROR);
        paint.setShader(linearGradient);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(alphaPath, paint);

        //绘制底部日期
        if (!isLongPress) {
            Paint textPaint = getTextPaint(COLOR_BOTTOMDATE, dateTextSize);
            String startDate = formatDate(list.get(0).getDate(), "yyyyMMdd", "yyyy-MM-dd");
            String endDate = formatDate(list.get(list.size() - 1).getDate(), "yyyyMMdd", "yyyy-MM-dd");
            float textWidth = textPaint.measureText(startDate);
            float textX = MARGIN + STOREWIDTH;
            float y = marginTop + mHeight + verticalSpec + textsize;
            canvas.drawText(startDate, textX, y, textPaint);
            textX = MARGIN + STOREWIDTH + mWidth - textWidth;
            canvas.drawText(endDate, textX, y, textPaint);
        }
    }

    /**
     * 虚线及边框的画笔
     */
    protected Paint getLinePaint() {
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STOREWIDTH);
        mPaint.setPathEffect(new CornerPathEffect(10));
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(0);
        mPaint.setColor(Color.parseColor(COLOR_BOX));
        return mPaint;
    }


    /**
     * @param list
     */
    public void setData(ArrayList<ColumnBean> list) {
        if (list == null || list.size() == 0) {
            isSetData = false;
            postInvalidate();
            return;
        }
        this.list = list;
        isSetData = true;
        //获取最大绝对值
        float maxValue = Math.abs(list.get(getMaxValueIndex()).getValue());
        //计算左侧数值
        leftValues = calculateAvarage(maxValue, true);
        requestLayout();
//        postInvalidate();
//        animator.start();
    }


    /**
     * 获取最大值的索引 如果没有数据返回-1
     */
    private int getMaxValueIndex() {
        if (list == null || list.size() == 0) {
            Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
            return -1;
        }
        int size = list.size();
        double indexValue = Math.abs(list.get(0).getValue());
        int index = 0;
        for (int i = 1; i < size; i++) {
            double tempValue = Math.abs(list.get(i).getValue());
            if (tempValue > indexValue) {
                indexValue = tempValue;
                index = i;
            }
        }
        return index;
    }

    /**
     * 计算点的位置并存入集合中
     *
     * @param list
     */
    private ArrayList<PointFLocal> calculatePointLocal(ArrayList<ColumnBean> list, float maxValue) {
        ArrayList<PointFLocal> pointFs = new ArrayList<>();
        int size = list.size();
        alphaPath = new Path();

        for (int i = 0; i < size; i++) {
            float value = list.get(i).getValue();
            float percent = (Math.abs(maxValue) - value) / mAvarageValue;
            float y = Math.abs(mAvarageLine * percent) + marginTop;
            //左侧边距 + 线宽 + x * 间距
            float x = i * mPointSpec + MARGIN + STOREWIDTH / 2;
            PointFLocal pointF = new PointFLocal();
            pointF.x = x;
            pointF.y = y;
            pointFs.add(pointF);
//            if (i == 0) {
//                alphaPath.moveTo(x, marginTop + mHeight);
//            }
//            alphaPath.lineTo(x, y);
        }

//        int pointSize = pointFs.size();

        //关闭path
//        alphaPath.lineTo(pointFs.get(pointSize - 1).x, marginTop + mHeight);
//        alphaPath.close();
        return pointFs;
    }

    private void resetAlphaPath(ArrayList<ColumnBean> list, float maxValue) {
        int size = list.size();
        alphaPath.reset();
        alphaPath.moveTo(MARGIN + STOREWIDTH / 2, marginTop + mHeight);
        for (int i = 0; i < size; i++) {
            float value = list.get(i).getValue();
            float percent = (Math.abs(maxValue) - value) / mAvarageValue;
            float y = Math.abs(mAvarageLine * percent) + marginTop;
            //左侧边距 + 线宽 + x * 间距
            float x = i * mPointSpec + MARGIN + STOREWIDTH / 2;
            if (x > stopX) {
                alphaPath.lineTo(stopX, y);
                break;
            } else {
                alphaPath.lineTo(x, y);
            }
        }
        //关闭path
        alphaPath.lineTo(stopX, marginTop + mHeight);
        alphaPath.close();

    }
}
