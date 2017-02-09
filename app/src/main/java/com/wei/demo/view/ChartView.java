package com.wei.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.demo.ColumnBean;
import com.wei.demo.ColumnLocation;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ${wei} on 2016/12/15.
 */

public class ChartView extends SurfaceView {
    private static final String TAG = "zpy_ChartView";

    private ArrayList<ColumnBean> list = new ArrayList<>();
    private Context context;

    //默认线的颜色
    private static final String LINECOLOR = "#666666";
    //负值的颜色
    private static final String BLUECOLOR = "#8800ffff";
    //正值的颜色
    private static final String REDCOLOR = "#666666";
    //边距
    private final int MARGIN = 24;
    private final int MARGINTOP = 48;
    private final int MARGINBOTTOM = 48;
    private int VERTICALSPEC = 4;//柱状图间距
    private int mHeight;
    private int mWidth;
    private int avarageLine;
    private float mColumnWidth;
    private double avarageValue;//平均值
    private GestureDetector gestureDetector;
    private int touchSlop;
    private int downX;
    private static boolean isLongPress;
    private final int STOREWIDTH = 3;
    //底部背景日期颜色
    private final String COLOR_BOTTOMDATEBG = "#88E2EBF9";
    //底部日期颜色
    private final String COLOR_BOTTOMDATE = "#66666666";
    //虚线的颜色
    private final String COLOR_DASHLINE = "#999999";
    //边框的颜色
    private final String COLOR_BOX = "#999999";
    private int index;
    private boolean isCanvas;
    private int dateIndex;
    private boolean isSet;
    private int orange;
    private boolean isMoveLeft, isMoveRight;
    private int textsize = 14;

    private ArrayList<ColumnLocation> locations = new ArrayList<>();

    public ChartView(Context context) {
        super(context);
        init(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - MARGIN * 2;
        mHeight = h - MARGINTOP - MARGINBOTTOM * 2;
        //每个柱状图的宽度 整体宽度-左右边距 - 左右线的宽度 - 间隔
        mColumnWidth = (mWidth - STOREWIDTH * 2 - VERTICALSPEC * 30) / 31.0f;
    }

    private void init(Context context) {
        this.context = context;
        //系统认为最小的滑动距离
        isSet = false;
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        gestureDetector = new GestureDetector(context, new MyGustrueListener());

        textsize = dp2px(textsize);
        VERTICALSPEC = dp2px(VERTICALSPEC);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制边框及虚线
        drawBox(canvas);
        if (isSet) {
            //绘制左侧数值
            drawLeftText(canvas);
            //绘制柱状图
            //平均值不等于0 的时候开始绘制
            drawColumnBitmap(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                int diffX = Math.abs(moveX - downX);
                int finalMoveX = moveX - MARGIN;//减去左侧边距
                //滑动只在范围内开始绘制虚线
                if (isLongPress && moveX > MARGIN && moveX < (mWidth + MARGIN) && moveY > MARGINTOP && moveY < (MARGINTOP + mHeight)) {
                    if (diffX >= touchSlop) {
                        isCanvas = true;
                        calculateLong(finalMoveX);
                    } else {
                        isCanvas = false;
                    }
                } else {
                    isCanvas = false;

                    if (list.size() > 31) {
                        if (moveX - downX > 0 && !isMoveLeft) {
                            orange = moveX - downX + orange;
                            downX = (int) event.getX();
                            postInvalidate();
                        } else if (moveX - downX < 0 && !isMoveRight) {
                            orange = moveX - downX + orange;
                            downX = (int) event.getX();
                            postInvalidate();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isLongPress = false;
                postInvalidate();
                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * 绘制框架
     */

    private void drawBox(Canvas canvas) {
        Paint paint = getLinePaint();
        //绘制边框
        Path path = new Path();
        path.moveTo(MARGIN, MARGINTOP);
        path.lineTo(mWidth + MARGIN, MARGINTOP);
        path.lineTo(mWidth + MARGIN, mHeight + MARGINTOP);
        path.lineTo(MARGIN, mHeight + MARGINTOP);
        path.lineTo(MARGIN, MARGINTOP);
        path.close();
        canvas.drawPath(path, paint);
        path.reset();

        //平均值
        avarageLine = mHeight / 6;
        //绘制横线
        paint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));
        paint.setColor(Color.parseColor(COLOR_DASHLINE));
        for (int i = 1; i <= 5; i++) {
            int startY = MARGINTOP + avarageLine * i;
            canvas.drawLine(MARGIN, startY, mWidth + MARGIN, startY, paint);
        }
    }


    /**
     * 绘制左侧数值
     **/
    private void drawLeftText(Canvas canvas) {
        Paint paint = getTextPaint(COLOR_BOTTOMDATE, 30);
        int maxValueIndex = getMaxValueIndex();
        if (maxValueIndex == -1) {
            return;
        }
        double[] doubles = calculateAvarage(list.get(maxValueIndex).getValue());
        if (doubles == null || doubles.length == 0) {
            return;
        }
        String[] leftValues = new String[doubles.length];
        //转换成字符串
        for (int i = 0; i < doubles.length; i++) {
            double aDouble = doubles[i];

            if (aDouble > 0) {
                leftValues[i] = String.valueOf(String.format("%s%s", "+", formatValue(aDouble)));
            } else if (aDouble == 0) {
                leftValues[i] = String.valueOf(String.format("%s", "0.00"));
            } else leftValues[i] = String.valueOf(formatValue(aDouble));
        }

        for (int i = 0; i < leftValues.length; i++) {
            canvas.drawText(leftValues[i], MARGIN + 10, MARGINTOP - 10 + avarageLine * i, paint);
        }
        paint.reset();
    }

    /**
     * 绘制柱状图
     */
    private void drawColumnBitmap(Canvas canvas) {
        if (list == null || list.size() == 0) {
            Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }
        Paint paint = getLinePaint();
        paint.setStyle(Paint.Style.FILL);
        int centerLine = avarageLine * 3 + MARGINTOP;

        ColumnBean columnBean = list.get(0);
        String date_value = columnBean.getDate();
        dateIndex = Integer.valueOf(date_value.substring(date_value.lastIndexOf("-") + 1)) - 1;

        for (int i = 0; i < list.size(); i++) {

            ColumnBean columnBean2 = list.get(i);
            String date2 = columnBean2.getDate();
            int index2 = Integer.valueOf(date2.substring(date2.lastIndexOf("-") + 1)) - 1;

            //每个柱图的高度
            double value = list.get(i).getValue();
            double percent = value / avarageValue;//比例
            double endY = Math.abs(percent * avarageLine);
            //y点的结束点
            if (value < 0) {
                paint.setColor(Color.GREEN);
                endY = centerLine + endY;
            } else {
                paint.setColor(Color.RED);
                endY = centerLine - endY;
            }
            endY = formatValue(endY);
            int startX = ((int) (index2 * (mColumnWidth + VERTICALSPEC) + MARGIN + STOREWIDTH) /*+ orange*/);
            int endX = (int) (startX + mColumnWidth);
            int left = MARGIN + STOREWIDTH;

//            if (i == 0 && startX >= left) {
//                isMoveLeft = true;
//                isMoveRight = false;
//            }
//
//            if (i == list.size() - 1 && endX <= MARGIN + STOREWIDTH + mWidth) {
//                isMoveRight = true;
//                isMoveLeft = false;
//            }
//
//            if (startX < left && endX > left) {
//                startX = left;
//            }
//            int right = left + mWidth;
//            if (endY > right && startX < right) {
//                endX = right;
//            }
//            Log.e(TAG, "drawColumnBitmap: endx = " + endX + "   startX = " + startX + "  i = " + i + "   ismoveleft = " + isMoveLeft);
//            if (startX >= left && endX <= right) {
            Rect rect = new Rect();
            rect.left = startX;
            rect.top = value < 0 ? centerLine : (int) endY;
            rect.right = endX;
            rect.bottom = value < 0 ? (int) endY : centerLine;
            canvas.drawRect(rect, paint);
//            }
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
            canvas.drawText(startDate, startX, MARGINTOP + mHeight +textsize, paint);
            canvas.drawText(endDate, endStartX, MARGINTOP + mHeight + textsize, paint);

        }

        //绘制虚线
        if (isLongPress) {
            paint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));
            paint.setStrokeWidth(STOREWIDTH);
            paint.setColor(Color.GRAY);
            int startX = (int) ((index + dateIndex) * (mColumnWidth + VERTICALSPEC) + MARGIN + STOREWIDTH + mColumnWidth / 2);
            canvas.drawLine(startX, MARGINTOP, startX, MARGINTOP + mHeight, paint);
            paint.reset();
            String date = list.get(index).getDate();
            drawBottomText(canvas, paint, startX, date);

            Log.e(TAG, "drawColumnBitmap: date = " + date);
        }
    }


    /**
     * 绘制底部日期及背景
     *
     * @param text   文本
     * @param startX 线的起始点X轴
     */
    private void drawBottomText(Canvas canvas, Paint paint, int startX, String text) {
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(STOREWIDTH);
        //绘制底部日期
        paint.setTextSize(40);

        paint.setColor(Color.parseColor(COLOR_BOTTOMDATEBG));
        int textLength = (int) paint.measureText(text);
        int right = startX + textLength / 2 + VERTICALSPEC;
        int left = startX - textLength / 2 - VERTICALSPEC;
        if (left < MARGIN) {
            left = MARGIN;
            right = left + textLength + VERTICALSPEC * 2;
        }
        if (right > MARGIN + mWidth) {
            right = MARGIN + mWidth;
            left = right - textLength - VERTICALSPEC * 2;
        }
        //绘制日期的背景
        Rect rect = new Rect(left, MARGINTOP + mHeight + VERTICALSPEC, right, MARGINTOP + mHeight + 50 + VERTICALSPEC);
        canvas.drawRect(rect, paint);

        //绘制底部日期
        paint.setTextSize(40);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.parseColor(COLOR_BOTTOMDATE));
        canvas.drawText(text, left + VERTICALSPEC, MARGINTOP + mHeight + 40 + VERTICALSPEC, paint);
    }


    /**
     * 计算长按事件的位置
     *
     * @param finalMoveX 去除左侧边距的位置
     */
    private void calculateLong(int finalMoveX) {
        index = (int) ((finalMoveX - VERTICALSPEC / 2) / (mColumnWidth + VERTICALSPEC));
        if (index > dateIndex + list.size() - 1) {
            index = list.size() - 1;
        } else if (index < dateIndex) {
            index = 0;
        } else {
            index = Math.abs(index - dateIndex);
        }
        if (index < 0) {
            index = 0;
        }
        postInvalidate();
    }

    /**
     * 虚线及边框的画笔
     */
    private Paint getLinePaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STOREWIDTH);
        paint.setAntiAlias(true);
        paint.setAlpha(0);
        paint.setColor(Color.parseColor(COLOR_BOX));
        return paint;
    }

    /**
     * 获取绘制文本的画笔
     *
     * @param textColor 文本颜色
     * @param textSize  文本大小
     * @return
     */
    private Paint getTextPaint(String textColor, int textSize) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(STOREWIDTH);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setAlpha(0);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.parseColor(textColor));
        return paint;
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

    //设置数据
    public void setData(ArrayList<ColumnBean> list) {
        this.list = list;
        isSet = true;
        postInvalidate();
    }

    //计算平均值 并把左侧文案放到数组中
    private double[] calculateAvarage(double maxValue) {
        if (maxValue == 0) {
            Toast.makeText(context, "最大值不能是0", Toast.LENGTH_SHORT).show();
            return null;
        }
        avarageValue = maxValue / 3;
        double[] textValue = new double[7];
        int tempIndex = 3;
        //如果最大值是小数则 大-->小 设置数据
        if (maxValue > 0) {
            for (int i = 0; i < 7; i++) {
                textValue[i] = formatValue(avarageValue * (tempIndex--));
                if (tempIndex + 1 == 0) {
                    textValue[i] = 0.00f;
                }
            }
        } else {
            for (int i = 6; i >= 0; i--) {
                textValue[i] = formatValue(avarageValue * (tempIndex--));
                if (tempIndex + 1 == 0) {
                    textValue[i] = 0.00f;
                }
            }
        }
        return textValue;
    }

    /**
     * 全部转换为两位小数
     */
    private double formatValue(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return Double.valueOf(decimalFormat.format(value));
    }

    private int dp2px(int dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    private int px2dp(int pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    private class MyGustrueListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            isLongPress = true;
            calculateLong((int) (e.getX() - MARGIN));
        }
    }
}
