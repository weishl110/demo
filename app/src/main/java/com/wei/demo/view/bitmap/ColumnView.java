package com.wei.demo.view.bitmap;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.wei.demo.bean.ColumnBean;
import com.wei.demo.bean.PointFLocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by ${wei} on 2017/2/16.
 */

public class ColumnView extends BaseChartView {

    private static final String TAG = "zpy_ColumnView";

    private ArrayList<ColumnBean> list;
    private ArrayList<PointFLocal> pointFs = new ArrayList<>();//存放每个柱的左右侧位置

    private int dateIndex = 0;
    private float mColumnWidth;
    private float daysOfMonth = 30.0f;
    //负值的颜色
    private static final String BLUECOLOR = "#88049e63";
    //正值的颜色
    private static final String REDCOLOR = "#88f80d0d";
    private String[] leftValues;
    private float heightPercent;
    private ObjectAnimator animator;

    public ColumnView(Context context) {
        super(context);
        init();
    }

    public ColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColumnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        animator = ObjectAnimator.ofFloat(this, "heightPercent", 0.0f, 1.0f);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);
    }

    public void setHeightPercent(float heightPercent) {
        this.heightPercent = heightPercent;
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //每个柱状图的宽度 整体宽度-左右边距 - 左右线的宽度 - 间隔
        if (list != null && list.size() > 0) {
            daysOfMonth = getDaysOfMonth(list.get(0).getDate(), "yyyyMMdd");
        }
        mColumnWidth = (mWidth - STOREWIDTH * 2 - verticalSpec * (daysOfMonth - 1)) / daysOfMonth;
    }

    @Override
    protected void drawLeftText(Canvas canvas) {
        Paint paint = getTextPaint(COLOR_LEFTTEXT, textsize);
        if (leftValues == null || leftValues.length == 0) {
            return;
        }
        drawLeftText(canvas, leftValues, paint);
    }

    @Override
    protected void drawLong(Canvas canvas) {

        int size = pointFs.size();
        PointFLocal onePointF = pointFs.get(0);
        PointFLocal lastPointF = pointFs.get(size - 1);
        //是否第一个点范围内
        if (longDownX <= onePointF.x || (longDownX > onePointF.x && longDownX < onePointF.endX + verticalSpec / 2)) {
            drawDeshLine(canvas, onePointF.x, onePointF.endX, 0);
        } else if (longDownX >= lastPointF.endX || (longDownX > (lastPointF.x - verticalSpec / 2) && longDownX < lastPointF.endX)) {
            //是否在最后一个点范围内
            drawDeshLine(canvas, lastPointF.x, lastPointF.endX, size - 1);
        } else {
            //在中间
            PointFLocal prePointF, nextPoint;
            for (int i = 1; i < size - 1; i++) {
                PointFLocal pointF = pointFs.get(i);
                prePointF = pointFs.get(i - 1);
                nextPoint = pointFs.get(i + 1);

                float leftX = pointF.x - (pointF.x - prePointF.endX) / 2;
                float rightX = pointF.endX + (nextPoint.x - pointF.endX) / 2;
                if (longDownX > leftX && longDownX < rightX) {
                    drawDeshLine(canvas, leftX, rightX, i);
                    break;
                }
            }
        }
    }

    /**
     * 绘制长按虚线
     *
     * @param canvas
     * @param leftX
     * @param rightX
     */
    private void drawDeshLine(Canvas canvas, float leftX, float rightX, int index) {
        //绘制虚线
        Paint paint = getLinePaint();
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));
        paint.setStrokeWidth(STOREWIDTH);
        paint.setColor(Color.parseColor(COLOR_DOTTED));
        float startX = leftX + (rightX - leftX) / 2;
        canvas.drawLine(startX, marginTop, startX, marginTop + mHeight, paint);
        //绘制底部日期
        paint.setTextSize(dateTextSize);
        drawBottomText(canvas, paint, startX, formatDate(list.get(index).getDate(), "yyyyMMdd", "yyyy-MM-dd"));
    }

    @Override
    protected void drawLine(Canvas canvas) {
        if (list == null || list.size() == 0) {
            return;
        }
        Paint paint = getLinePaint();
        paint.setStyle(Paint.Style.FILL);
        float centerLine = mAvarageLine * 3 + marginTop;

//        ColumnBean columnBean = list.get(0);
//        String date_value = columnBean.getDate();
//        dateIndex = Integer.valueOf(date_value.substring(date_value.lastIndexOf("-") + 1)) - 1;

        pointFs.clear();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            //每个柱图的高度
            float value = list.get(i).getValue();
            float percent = value / mAvarageValue;//比例
            float endY = Math.abs(percent * mAvarageLine);
            //y点的结束点
            if (value < 0) {
                paint.setColor(Color.parseColor(BLUECOLOR));
//                endY = centerLine + endY;
            } else {
                paint.setColor(Color.parseColor(REDCOLOR));
//                endY = centerLine - endY;
            }
            endY = Float.parseFloat(formatValue(endY));
            int startX = ((int) (i * (mColumnWidth + verticalSpec) + MARGIN + STOREWIDTH) /*+ orange*/);
            int endX = (int) (startX + mColumnWidth);
            if (startX > MARGIN && endX < MARGIN + STOREWIDTH + mWidth) {
                //记录每个柱状图的左右点
                PointFLocal pointF = new PointFLocal();
                pointF.x = startX;
                pointF.endX = endX;
                pointFs.add(pointF);

                Rect rect = new Rect();
                rect.left = startX;
                rect.right = endX;

                if (value < 0) {
                    rect.top = (int) centerLine;
                    rect.bottom = (int) (centerLine + (endY * heightPercent));
                } else {
                    rect.bottom = (int) centerLine;
                    rect.top = (int) (centerLine - (endY * heightPercent));
                }
                canvas.drawRect(rect, paint);
            }
        }
        //非常按时绘制底部日期
        if (!isLongPress) {
            paint.setTextSize(dateTextSize);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(COLOR_BOTTOMDATE));
            paint.setTextAlign(Paint.Align.LEFT);
            String startDate = formatDate(list.get(0).getDate(), "yyyyMMdd", "yyyy-MM-dd");
            float textWidth = paint.measureText(startDate);
            int startX = (int) ((dateIndex * (mColumnWidth + verticalSpec) + MARGIN + STOREWIDTH) - textWidth / 2);

            if (startX < MARGIN + STOREWIDTH) {
                startX = MARGIN + STOREWIDTH;
            }
            canvas.drawText(startDate, startX, marginTop + mHeight + textsize + verticalSpec, paint);
            //绘制中间日期
            if (size < daysOfMonth / 2) return;
            int index = Math.round(daysOfMonth / 2) - 1;
            String date = formatDate(list.get(index).getDate(), "yyyyMMdd", "yyyy-MM-dd");
            startX = (int) (((mColumnWidth + verticalSpec) * index + MARGIN + STOREWIDTH) - textWidth / 2 + verticalSpec);
            canvas.drawText(date, startX, marginTop + mHeight + textsize + verticalSpec, paint);

            //绘制右侧日期
            if (size != daysOfMonth) return;
            //绘制右侧日期
            String endDate = formatDate(list.get(list.size() - 1).getDate(), "yyyyMMdd", "yyyy-MM-dd");
            int endStartX = (int) ((dateIndex + list.size() - 1) * (mColumnWidth + verticalSpec) + MARGIN + STOREWIDTH - textWidth + mColumnWidth);
//            if (endStartX + textWidth > MARGIN + STOREWIDTH + mWidth) {
//                endStartX = (int) (MARGIN + mWidth - textWidth);
//            }
            canvas.drawText(endDate, endStartX, marginTop + mHeight + textsize + verticalSpec, paint);
        }
    }


    /**
     * 获取最大值的索引 如果没有数据返回-1
     */
    private int getMaxValueIndex() {
        if (list == null || list.size() == 0) {
            return -1;
        }
        int size = list.size();
        float indexValue = Math.abs(list.get(0).getValue());
        int index = 0;
        for (int i = 1; i < size; i++) {
            float tempValue = Math.abs(list.get(i).getValue());
            if (tempValue > indexValue) {
                indexValue = tempValue;
                index = i;
            }
        }
        return index;
    }

    public void setData(ArrayList<ColumnBean> list) {
        if (list == null || list.size() == 0) {
            isSetData = false;
            postInvalidate();
            return;
        }
        this.list = list;
        //计算最大值
        int maxValueIndex = getMaxValueIndex();
        if (maxValueIndex == -1) {
            return;
        }
        isSetData = true;
        //计算左侧数值
        leftValues = calculateAvarage(list.get(maxValueIndex).getValue(), true);
        requestLayout();
//        postInvalidate();
        animator.start();//开启动画
    }

    /**
     * 获取当前月的具体的最大天数
     *
     * @return
     */
    public static int getDaysOfMonth(String text, String regex) {
        if (Pattern.matches(regex, text)) {

        }
        SimpleDateFormat sdf = new SimpleDateFormat(regex);
        Calendar calendar = null;
        try {
            Date date = sdf.parse(text);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
