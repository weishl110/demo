package com.wei.demo.view.bitmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.wei.demo.ColumnBean;
import com.wei.demo.bean.PointF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ${wei} on 2017/2/16.
 */

public class ColumnView extends BaseChartView {

    private static final String TAG = "zpy_ColumnView";

    private ArrayList<ColumnBean> list;
    private ArrayList<PointF> pointFs = new ArrayList<>();//存放每个柱的左右侧位置

    private int dateIndex = 0;
    private float mColumnWidth;

    //负值的颜色
    private static final String BLUECOLOR = "#88049e63";
    //正值的颜色
    private static final String REDCOLOR = "#88f80d0d";
    private String[] doubles;

    public ColumnView(Context context) {
        super(context);
    }

    public ColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColumnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawLeftText(Canvas canvas) {
        Paint paint = getTextPaint(COLOR_BOTTOMDATE, 30);
//        int maxValueIndex = getMaxValueIndex();
//        if (maxValueIndex == -1) {
//            return;
//        }
//        String[] doubles = calculateAvarage(list.get(maxValueIndex).getValue(), true);
        if (doubles == null || doubles.length == 0) {
            return;
        }
        drawLeftText(canvas, doubles, paint);
    }

    @Override
    protected void drawLong(Canvas canvas) {

        int size = pointFs.size();
        PointF onePointF = pointFs.get(0);
        PointF lastPointF = pointFs.get(size - 1);
        //是否第一个点范围内
        if (longDownX <= onePointF.x || (longDownX > onePointF.x && longDownX < onePointF.endX + VERTICALSPEC / 2)) {
            drawDeshLine(canvas, onePointF.x, onePointF.endX, 0);
        } else if (longDownX >= lastPointF.endX || (longDownX > (lastPointF.x - VERTICALSPEC / 2) && longDownX < lastPointF.endX)) {
            //是否在最后一个点范围内
            drawDeshLine(canvas, lastPointF.x, lastPointF.endX, size - 1);
            Log.e(TAG, "drawLong: " + (size - 1));
        } else {
            //在中间
            PointF prePointF, nextPoint;
            for (int i = 1; i < size - 1; i++) {
                PointF pointF = pointFs.get(i);
                prePointF = pointFs.get(i - 1);
                nextPoint = pointFs.get(i + 1);

                float leftX = pointF.x - (pointF.x - prePointF.endX) / 2;
                float rightX = pointF.endX + (nextPoint.x - pointF.endX) / 2;
                if (longDownX > leftX && longDownX < rightX) {
                    Log.e(TAG, "drawLong: indesx = " + i);
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
        canvas.drawLine(startX, MARGINTOP, startX, MARGINTOP + mHeight, paint);
        //绘制底部日期
        Log.e(TAG, "drawDeshLine: index = " + index);
        drawBottomText(canvas, paint, startX, list.get(index).getDate());
    }

    @Override
    protected void drawLine(Canvas canvas) {
        if (list == null || list.size() == 0) {
            Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }
        Paint paint = getLinePaint();
        paint.setStyle(Paint.Style.FILL);
        float centerLine = mAvarageLine * 3 + MARGINTOP;

        ColumnBean columnBean = list.get(0);
        String date_value = columnBean.getDate();
//        dateIndex = Integer.valueOf(date_value.substring(date_value.lastIndexOf("-") + 1)) - 1;

        pointFs.clear();
        for (int i = 0; i < list.size(); i++) {

//            ColumnBean columnBean2 = list.get(i);
//            String date2 = columnBean2.getDate();
//            int index2 = Integer.valueOf(date2.substring(date2.lastIndexOf("-") + 1)) - 1;

            //每个柱图的高度
            double value = list.get(i).getValue();
            double percent = value / mAvarageValue;//比例
            double endY = Math.abs(percent * mAvarageLine);
            //y点的结束点
            if (value < 0) {
                paint.setColor(Color.parseColor(BLUECOLOR));
                endY = centerLine + endY;
            } else {
                paint.setColor(Color.parseColor(REDCOLOR));
                endY = centerLine - endY;
            }
            endY = Double.parseDouble(formatValue(endY));
            int startX = ((int) (i * (mColumnWidth + VERTICALSPEC) + MARGIN + STOREWIDTH) /*+ orange*/);
            int endX = (int) (startX + mColumnWidth);

            //记录每个柱状图的左右点
            PointF pointF = new PointF();
            pointF.x = startX;
            pointF.endX = endX;
            pointFs.add(pointF);

            Rect rect = new Rect();
            rect.left = startX;
            rect.top = value < 0 ? (int) centerLine : (int) endY;
            rect.right = endX;
            rect.bottom = value < 0 ? (int) endY : (int) centerLine;
            canvas.drawRect(rect, paint);
        }
        //非常按时绘制底部日期
        if (!isLongPress) {
            paint.setTextSize(textsize);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(COLOR_BOTTOMDATE));
            String startDate = list.get(0).getDate();
            String endDate = list.get(list.size() - 1).getDate();
            float textWidth = paint.measureText(startDate);
            int startX = (int) ((dateIndex * (mColumnWidth + VERTICALSPEC) + MARGIN + STOREWIDTH) - textWidth / 2);
            int endStartX = (int) ((dateIndex + list.size() - 1) * (mColumnWidth + VERTICALSPEC) + MARGIN + STOREWIDTH - textWidth / 2);

            if (startX < MARGIN + STOREWIDTH) {
                startX = MARGIN + STOREWIDTH;
            }
            if (endStartX + textWidth > MARGIN + STOREWIDTH + mWidth) {
                endStartX = (int) (MARGIN + mWidth - textWidth);
            }
            canvas.drawText(startDate, startX, MARGINTOP + mHeight + textsize, paint);
            canvas.drawText(endDate, endStartX, MARGINTOP + mHeight + textsize, paint);
        }
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

    public void setData(ArrayList<ColumnBean> list) {
        this.list = list;
        isSet = true;
        //计算最大值
        int maxValueIndex = getMaxValueIndex();
        if (maxValueIndex == -1) {
            return;
        }

        //每个柱状图的宽度 整体宽度-左右边距 - 左右线的宽度 - 间隔
        int daysOfMonth = getDaysOfMonth(list.get(0).getDate());
        Log.e(TAG, "setData: " + daysOfMonth);
        mColumnWidth = (mWidth - STOREWIDTH * 2 - VERTICALSPEC * (daysOfMonth - 1) / daysOfMonth);

        //计算左侧数值
        doubles = calculateAvarage(list.get(maxValueIndex).getValue(), true);
        postInvalidate();
    }

    /**
     * 获取当前月的具体的最大天数
     *
     * @return
     */
    public static int getDaysOfMonth(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = null;
        try {
            Date date = sdf.parse(string);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void sort() {
        int[] arr1 = new int[7];
        int[] arr2 = new int[10];
        int[] arr3 = new int[arr1.length + arr2.length];

        int i = 0, j = 0, index = 0;
        while (i < arr1.length || j < arr2.length) {
//            if (i == arr1.length && j < arr2.length) {
//                arr3[index++] = arr2[j++];
//            } else if (i < arr1.length && j < arr2.length) {
//                arr3[index++] = arr1[i++];
//            } else {
            if (arr1[i] < arr2[j]) {
                arr3[index++] = arr1[i++];
            } else {
                arr3[index++] = arr2[j++];
            }
//            }
        }

        while (i < arr1.length)
            arr3[index++] = arr1[i++];
        while (j < arr2.length)
            arr3[index++] = arr2[j++];

    }
}
