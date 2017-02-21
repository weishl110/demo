package com.wei.demo.view.bitmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${wei} on 2017/2/16.
 */

public abstract class BaseChartView extends View {

    protected static final String TAG = "zpy_BaseChartView";
    protected Context context;
    protected int mWidth, mHeight, downX;
    //纵向平均值
    protected float mAvarageLine, mAvarageValue;
    //画笔的宽度
    protected final int STOREWIDTH = 3;
    //左右上下边距
    protected int MARGIN = 11;
    protected int marginTop = 16;
    protected int MARGINBOTTOM = 16;
    protected int VERTICALSPEC = 2;//柱状图间距

    //底部背景日期颜色
    protected final String COLOR_BOTTOMDATEBG = "#88E2EBF9";
    //底部日期颜色
    protected final String COLOR_BOTTOMDATE = "#666666";
    //左侧文字颜色
    protected final String COLOR_LEFTTEXT = "#999999";
    //虚线的颜色
    protected final String COLOR_DASHLINE = "#999999";
    //边框的颜色
    protected final String COLOR_BOX = "#999999";
    //虚线的颜色
    protected final String COLOR_DOTTED = "#333333";
    protected int textsize = 12;
    protected int dateTextSize = 14;

    protected boolean isSet = false, isLongPress = false;
    protected int mTouchSlop;//系统默认为滑动距离
    protected GestureDetector mGestureDetector;
    protected float longDownX;

    public BaseChartView(Context context) {
        super(context);
        init(context);
    }

    public BaseChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        //系统认为最小的滑动距离
        isSet = false;
        isLongPress = false;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mGestureDetector = new GestureDetector(context, new MyGustrueListener());

        textsize = dp2px(textsize);
        dateTextSize = dp2px(dateTextSize);
        VERTICALSPEC = dp2px(VERTICALSPEC);
        MARGINBOTTOM = dp2px(MARGINBOTTOM);
        MARGIN = dp2px(MARGIN);
        marginTop = textsize + VERTICALSPEC * 2;

        setBackgroundResource(android.R.color.white);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - MARGIN * 2;
        mHeight = h - marginTop - MARGINBOTTOM * 2;
        //平均值
        mAvarageLine = mHeight / 6;
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
                if (isLongPress && moveX > MARGIN && moveX < (mWidth + MARGIN) && moveY > marginTop && moveY < (marginTop + mHeight)) {
                    if (diffX >= mTouchSlop) {
//                        calculateLong(finalMoveX);
                        longDownX = moveX;
                        postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isLongPress = false;
                postInvalidate();
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    long startTime = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBox(canvas);//绘制框架
        //绘制长按
        if (isSet) {
            startTime = System.currentTimeMillis();
            drawLeftText(canvas);
            drawLine(canvas);
            if (isLongPress) {
                startTime = System.currentTimeMillis();
                drawLong(canvas);
            }
        }
    }

    /**
     * 绘制左侧刻度数据
     *
     * @param canvas
     */
    protected abstract void drawLeftText(Canvas canvas);

    /**
     * 绘制曲线或柱状图
     *
     * @param canvas
     */
    protected abstract void drawLine(Canvas canvas);

    /**
     * 长按绘制
     *
     * @param canvas
     */
    protected abstract void drawLong(Canvas canvas);

    /**
     * 绘制框架
     */
    private void drawBox(Canvas canvas) {
        Paint paint = getLinePaint();
        //绘制边框
        Path path = new Path();

        path.moveTo(MARGIN, marginTop);
        path.lineTo(mWidth + MARGIN, marginTop);
        path.lineTo(mWidth + MARGIN, mHeight + marginTop);
        path.lineTo(MARGIN, mHeight + marginTop);
        path.lineTo(MARGIN, textsize + VERTICALSPEC * 2);
        path.close();
        canvas.drawPath(path, paint);
        path.reset();

        //绘制横线
        paint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));
        paint.setColor(Color.parseColor(COLOR_DASHLINE));
        for (int i = 1; i <= 5; i++) {
            float startY = (marginTop + mAvarageLine * i);
            canvas.drawLine(MARGIN, startY, mWidth + MARGIN, startY, paint);
        }
    }

    /**
     * 绘制左侧数值
     *
     * @param values 左侧数值数组
     **/
    protected void drawLeftText(Canvas canvas, String[] values, Paint paint) {
        if (values == null || values.length == 0) return;

        for (int i = 0; i < values.length; i++) {
            canvas.drawText(values[i], MARGIN + STOREWIDTH + VERTICALSPEC, marginTop - VERTICALSPEC + mAvarageLine * i, paint);
        }
        paint.reset();
    }

    /**
     * 长按绘制底部日期及背景
     *
     * @param text   文本
     * @param startX 线的起始点X轴
     */
    protected void drawBottomText(Canvas canvas, Paint paint, float startX, String text) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(STOREWIDTH);
        //绘制底部日期
        paint.setTextSize(dateTextSize);

        paint.setColor(Color.parseColor(COLOR_BOTTOMDATEBG));
        int textLength = (int) paint.measureText(text);
        int right = (int) (startX + textLength / 2 + VERTICALSPEC);
        int left = (int) (startX - textLength / 2 - VERTICALSPEC);
        if (left < MARGIN) {
            left = MARGIN;
            right = left + textLength + VERTICALSPEC * 2;
        }
        if (right > MARGIN + mWidth) {
            right = MARGIN + mWidth;
            left = right - textLength - VERTICALSPEC * 2;
        }
        //绘制日期的背景
        Rect rect = new Rect(left, marginTop + mHeight + VERTICALSPEC * 2, right, marginTop + mHeight + textsize + VERTICALSPEC * 3);
        canvas.drawRect(rect, paint);

        //绘制底部日期
        paint.setTextSize(dateTextSize);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(COLOR_BOTTOMDATE));
        canvas.drawText(text, left + VERTICALSPEC, marginTop + mHeight + textsize + VERTICALSPEC * 2, paint);
    }

    /**
     * 虚线及边框的画笔
     */
    protected Paint getLinePaint() {
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
    protected Paint getTextPaint(String textColor, int textSize) {
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


    //计算平均值 并把左侧文案放到数组中

    /**
     * @param maxValue
     * @param isSymbol 是否添加+-符号
     * @return
     */
    protected String[] calculateAvarage(float maxValue, boolean isSymbol) {
        if (maxValue == 0) {
            Toast.makeText(context, "最大值不能是0", Toast.LENGTH_SHORT).show();
            return null;
        }
        mAvarageValue = (float) Math.abs(maxValue / 3);
        String[] textValue = new String[7];
        int tempIndex = 3;
        //如果最大值是小数则 大-->小 设置数据
        for (int i = 0; i < 7; i++) {
            addData(isSymbol, textValue, tempIndex, i);
            tempIndex--;
        }
        return textValue;
    }

    //左侧数据添加到数组中
    private void addData(boolean isSymbol, String[] textValue, int tempIndex, int index) {
        if (isSymbol) {
            textValue[index] = tempIndex > 0 ? "+" + formatValue(mAvarageValue * (tempIndex)) : formatValue(mAvarageValue * (tempIndex));
        } else {
            textValue[index] = formatValue(mAvarageValue * (tempIndex));
        }
        if (tempIndex == 0) {
            textValue[index] = "0.00";
        }
    }

    protected int dp2px(int dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 全部转换为两位小数
     */
    protected String formatValue(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(value);
    }

    private class MyGustrueListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            isLongPress = true;
//            calculateLong((int) (e.getX() - MARGIN));
            longDownX = e.getX()/*- MARGIN*/;
            postInvalidate();
        }
    }

    protected String formatDate(String date, String currentRegex, String finalRegex) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(currentRegex);
        try {
            Date parse = simpleDateFormat.parse(date);
            simpleDateFormat = new SimpleDateFormat(finalRegex);
            return simpleDateFormat.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;


    }
}
