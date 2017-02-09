package com.wei.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.wei.demo.ColumnBean;
import com.wei.demo.bean.PointF;
import com.wei.demo.utils.StringUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ${wei} on 2017/1/4.
 */
public class TimeSharingView extends View {

    private static final String TAG = "zpy_TimeSharingView";

    //边框的颜色
    private final String COLOR_BOX = "#999999";
    private final String TEXT_COLOR = "#666666";
    //线的颜色
    private final String COLOR_BLUE = "#429ae6";
    //背景渐变色
    private final String COLOR_BLUE_ALPHA = "#33429ae6";
    //底部背景日期颜色
    private final String COLOR_BOTTOMDATEBG = "#88E2EBF9";
    //底部日期颜色
    private final String COLOR_BOTTOMDATE = "#66666666";
    /**
     * 线的颜色
     */
    private final String COLOR_LINE = "#275CAA";
    /**
     * 一屏展示的总数
     */
    private int totalPoints = 48;
    /**
     * 横向间距
     */
    private float spacPointHorizontal = 0, spacVertical = 0, spac = 4;
    private float spacLineHorizontal = 0.0f;
    /**
     * 边距
     */
    private static final float MARGINTOP = 48, MARGINBOTTOM = 48;
    //左右边距
    private static final float MARGINLEFT = 24, MARGINRIGHT = 24;
    //边框内的x y 的起始
    private float leftSpac = 0;//框的左边的起始点，包括左侧文案的宽度
    private float mWidth, mHeight;
    private int leftTextSize = 12, textSize = 14;
    private String[] times = new String[]{"9:30", "10:30", "11:30", "14:00", "15:00"};
    private String[] leftValue = new String[9];

    private ArrayList<ColumnBean> list;
    private boolean isSet = false;
    private Context context;
    private double midValue = 1.0000;
    /**
     * 最大值
     */
    private double maxValue;
    private double avarageValue;//平均值
    /**
     * 浮动值
     */
    private double floatingValue = 0.02f;

    private ArrayList<PointF> pointLists = new ArrayList<>();
    private Path linePath, alphaPath;
    private long moringTime;
    private long midTime;
    private long afterTime;
    private GestureDetector detector;
    private boolean isLongPress;

    public TimeSharingView(Context context) {
        this(context, null);
    }

    public TimeSharingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeSharingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        //去掉硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setBackgroundColor(Color.WHITE);
        leftTextSize = dp2px(leftTextSize);
        textSize = dp2px(textSize);

        detector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - MARGINLEFT - MARGINRIGHT;
        mHeight = h - MARGINBOTTOM - MARGINTOP - MARGINBOTTOM;
        spacVertical = mHeight / 8;//纵向间距
        spac = dp2px((int) spac);
        formatFinalTiem();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthMeasureSpec = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    float downX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isLongPress) {
                    downX = event.getX();
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isLongPress = false;
                postInvalidate();
                break;
        }
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBoxPath(canvas);//绘制边框及中间线
        if (isSet) {
            drawLeftText(canvas);//绘制左侧净值
            drawBottomText(canvas);//绘制底部时间
            drawLine(canvas);//绘制分时曲线
            drawLong(canvas);//长按绘制
        }
    }

    /**
     * 绘制布局
     */
    private void drawBoxPath(Canvas canvas) {
        Paint paint = getPathPaint();
        paint.setTextSize(leftTextSize);
        //测量maxValue的宽度
        float textWidth = paint.measureText(get4Decimal(maxValue));
        leftSpac = MARGINLEFT + textWidth + spac;
        float width = mWidth - textWidth - spac;
        spacPointHorizontal = width / totalPoints;//点与点的间距
        spacLineHorizontal = width / 8;
        Path path = new Path();
        path.moveTo(leftSpac, MARGINTOP);
        path.lineTo(leftSpac + width, MARGINTOP);
        path.moveTo(leftSpac + width, MARGINTOP + mHeight);
        path.lineTo(leftSpac, MARGINTOP + mHeight);
        path.moveTo(leftSpac, MARGINTOP);
        path.close();
        canvas.drawPath(path, paint);

        //绘制中间的横线
        int i = 1;
        for (; i < leftValue.length; i++) {
            float startY = MARGINTOP + i * spacVertical;
            canvas.drawLine(leftSpac, startY, leftSpac + width, startY, paint);
        }
        //绘制竖线
        for (i = 1; i < 8; i++) {
            float startX = leftSpac + spacLineHorizontal * i;
            canvas.drawLine(startX, MARGINTOP, startX, MARGINTOP + mHeight, paint);
        }
    }

    /**
     * 绘制左侧文字
     */
    private void drawLeftText(Canvas canvas) {
        Paint paint = getTextPaint();
        paint.setTextSize(leftTextSize);
        paint.setColor(Color.parseColor(TEXT_COLOR));
        paint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < leftValue.length; i++) {
            String text = leftValue[i];
            float startY = 0;
            //第一个和最后一个在需特殊计算
            if (i == 0) {
                startY = MARGINTOP + leftTextSize - 10 + i * spacVertical;
            } else if (i == leftValue.length - 1) {
                startY = MARGINTOP + i * spacVertical;
            } else {
                startY = MARGINTOP + leftTextSize / 2 + i * spacVertical;
            }
            canvas.drawText(text, MARGINLEFT, startY, paint);
        }
    }

    /**
     * 绘制底部日期
     */
    private void drawBottomText(Canvas canvas) {
        float startY = MARGINTOP + mHeight + textSize + spac;
        Paint paint = getTextPaint();
        paint.setTextSize(textSize);
        paint.setColor(Color.parseColor(TEXT_COLOR));
        paint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < times.length; i++) {
            String timeText = times[i];
            float textWidth = paint.measureText(timeText);
            float mid = textWidth / 2;
            float startX = leftSpac + i * spacLineHorizontal * 2 - mid;
            if (startX < leftSpac) {
                startX = leftSpac;
            }
            if ((startX + textWidth) > (MARGINLEFT + mWidth)) {
                startX = MARGINLEFT + mWidth - textWidth + spac / 2;
            }
            canvas.drawText(timeText, startX, startY, paint);
        }
    }


    /**
     * 绘制净值曲线
     */
    private void drawLine(Canvas canvas) {
        //计算点的位置
        calcuatePoints();
        if (pointLists.size() > 0) {

            //绘制曲线
            Paint paint = getLinePaint();
            paint.setAlpha(255);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(linePath, paint);
            paint.reset();
            //绘制背景
            PointF pointF = pointLists.get(0);
            //设置渐变背景
            LinearGradient linearGradient = new LinearGradient(pointF.x, MARGINTOP, 0, mHeight,
                    Color.parseColor(COLOR_BLUE), Color.parseColor(COLOR_BLUE_ALPHA), Shader.TileMode.MIRROR);
            paint.setShader(linearGradient);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.TRANSPARENT);
            paint.setStrokeWidth(0);
            paint.setAlpha(80);
            canvas.drawPath(alphaPath, paint);
            paint.reset();
        }
    }

    private void drawLong(Canvas canvas) {
        //绘制长按时的虚线
        if (isLongPress) {
            //初始化画笔
            Paint paint = getDeshPathPaint();
            paint.setColor(Color.GRAY);

            PointF startPoint = pointLists.get(0);
            PointF endPoint = pointLists.get(pointLists.size() - 1);
            int index = 0;
            if (downX < startPoint.x) {
                index = 0;
            } else if (downX > endPoint.x) {
                index = pointLists.size() - 1;
            } else {
                index = search(downX, 0, pointLists.size() - 1);
            }
//            if (index < 0) return;
            PointF point = pointLists.get(index);
            //横线
            canvas.drawLine(leftSpac, point.y, MARGINLEFT + mWidth, point.y, paint);
            //竖线
            canvas.drawLine(point.x, MARGINTOP, point.x, MARGINTOP + mHeight, paint);
            //绘制底部时间  绘制左侧净值
            ColumnBean columnBean = list.get(index);
            drawLongText(canvas, getTextPaint(), (int) point.x, point.y, columnBean.getDate(), String.valueOf(columnBean.getNetValue()));
            paint.reset();
        }
    }


    /**
     * 绘制底部日期及背景
     *
     * @param startX       线的起始点X轴
     * @param bottomDate   文本
     * @param leftNetValue 左侧净值
     */
    private void drawLongText(Canvas canvas, Paint paint, float startX, float y, String bottomDate, String leftNetValue) {
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1.5f);
        paint.setColor(Color.parseColor(COLOR_BOTTOMDATEBG));
        //绘制底部日期
        paint.setTextSize(textSize);
        int textWidth = (int) paint.measureText(bottomDate);
        int right = (int) (startX + textWidth / 2 + spac);
        int left = (int) (startX - textWidth / 2 - spac);
        if (left < leftSpac) {
            left = (int) leftSpac;
            right = (int) (left + textWidth + spac * 2);
        }
        if (right > MARGINLEFT + mWidth) {
            right = (int) (MARGINLEFT + mWidth);
            left = (int) (right - textWidth - spac * 2);
        }
        //绘制日期的背景
        Rect rect = new Rect(left, (int) (MARGINTOP + mHeight + spac / 2), right, (int) (MARGINTOP + mHeight + textSize + spac * 2));
        canvas.drawRect(rect, paint);

        //绘制左侧净值
        float leftValueWidth = paint.measureText(leftNetValue);
        int valueleft = (int) MARGINLEFT;
        right = (int) (MARGINLEFT + leftValueWidth + spac);
        int top = (int) (y - leftTextSize / 2 + spac / 2);
        int bottom = (int) (top + leftTextSize + spac);
        rect = new Rect(valueleft, top, right, bottom);
        canvas.drawRect(rect, paint);

        //绘制底部日期 左侧净值
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.parseColor(COLOR_BOTTOMDATE));
        //
        canvas.drawText(bottomDate, left + spac, MARGINTOP + mHeight + textSize + spac, paint);
        // 绘制左侧净值
        canvas.drawText(leftNetValue, valueleft + spac / 2, bottom - spac / 2, paint);


    }

    /**
     * 递归 二分查找 查找按下的位置在那个时间点上
     */
    private int search(float downX, int startIndex, int endIndex) {
        if (startIndex > endIndex) return -1;

        int midIndex = endIndex - startIndex;
        PointF point = pointLists.get(midIndex);
        //下一个点 上一个点
        PointF nextPoint, prePoint;
        if (midIndex != pointLists.size() - 1) {
            nextPoint = pointLists.get(midIndex + 1);
        } else {
            nextPoint = pointLists.get(pointLists.size() - 1);
        }
        if (midIndex != 0) {
            prePoint = pointLists.get(midIndex - 1);
        } else {
            prePoint = pointLists.get(0);
        }

        float left = point.x - (point.x - prePoint.x) / 2;
        float right = point.x + (nextPoint.x - point.x) / 2;
        if (left < leftSpac) {
            left = leftSpac;
        }
        if (right > MARGINLEFT + mWidth) {
            right = MARGINLEFT + mWidth;
        }
        //返回索引
        if (downX < right && downX > left) {
            return midIndex;
        }

        if (downX < left && downX > leftSpac) {
            return search(downX, startIndex, midIndex - 1);
        } else if (downX > right && downX < pointLists.get(pointLists.size() - 1).x) {
            return search(downX, midIndex + 1, endIndex);
        } else {
            return -1;
        }
    }


    /**
     * 计算点的位置放入集合中
     */
    private void calcuatePoints() {
        //计算点的位置 从左往右画
        float midY = (MARGINTOP + spacVertical * 4);//中间点Y轴的位置
        pointLists.clear();
        linePath = new Path();
        alphaPath = new Path();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ColumnBean columnBean = list.get(i);
            PointF pointF = getPoint(midY, columnBean);
            pointLists.add(pointF);
            if (i == 0) {
                linePath.moveTo(pointF.x, pointF.y);
                alphaPath.moveTo(pointF.x, MARGINTOP + mHeight);
            }
            alphaPath.lineTo(pointF.x, pointF.y);
            if (i > 0) {
                linePath.lineTo(pointF.x, pointF.y);
                if (i == list.size() - 1) {
                    alphaPath.lineTo(pointF.x, MARGINTOP + mHeight);
                    alphaPath.close();
                } else if (i != list.size() - 1) {//或者是下一个点大于宽度时
                    PointF point = getPoint(midY, list.get(i + 1));
                    if (point.x > MARGINLEFT + mWidth) {
                        alphaPath.lineTo(pointF.x, MARGINTOP + mHeight);
                        alphaPath.close();
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param midY   中间值
     * @param column
     * @return
     */
    private PointF getPoint(float midY, ColumnBean column) {
        PointF pointF = new PointF();
        float startX = 0.0f;
        float startY = 0.0f;
        //根据时间计算X点
        String startDate = column.getDate();
        startX = getX(startDate);
        //根据净值计算Y点
        //起始点
        double netValue = column.getNetValue();
        startY = getY(midY, netValue);

        pointF.x = startX;
        pointF.y = startY;
        return pointF;
    }

    /**
     * @param midY     中间值
     * @param netValue 净值数
     * @return 根据净值计算的Y点
     */
    private float getY(float midY, double netValue) {
        float startY;
        double percent = (midValue - netValue) / avarageValue;//计算比例
        double tempY = percent * spacVertical;
        startY = Float.parseFloat(get4Decimal(Math.abs(midY + tempY)));
        return startY;
    }

    private float getX(String date) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        try {
            //大于13:00的需减去13:00  上午减去 9:30
            long endTime = format.parse(date).getTime();//需计算的时间
            long poorTime = 0L;
            if (endTime > afterTime) {
                poorTime = endTime - (afterTime - midTime) - moringTime;
            } else {
                poorTime = endTime - moringTime;
            }
            long index = poorTime / 1000 / 60 / 5;
            float startX = index * spacPointHorizontal + leftSpac;
//            Log.e(TAG, "getX: startX = " + startX + "  leftSpac = " + leftSpac + "  index = " + index);
            return startX;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    /**
     * 计算平均值，并计算左侧值
     */
    private void calcuateAvarage() {
        double poorValue = Math.abs(maxValue - midValue);
        avarageValue = poorValue / 4 + floatingValue;
        double tempAvarage = poorValue / 4;
        int tempIndex = 4;
        for (int i = 0; i < leftValue.length; i++) {
            double tempValue = tempAvarage * tempIndex--;
            if (i == 4) {
                leftValue[i] = get4Decimal(midValue + tempValue);
            } else {
                tempValue = tempValue > 0 ? tempValue + floatingValue : tempValue - floatingValue;
                leftValue[i] = get4Decimal(midValue + (tempValue/* - floatingValue*/));
            }
        }
    }

    /**
     * 格式化固定时间 9:30 11:30 13:00 转换为毫秒
     */
    private void formatFinalTiem() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        try {
            afterTime = format.parse("13:00").getTime();//下午
            moringTime = format.parse("09:30").getTime();//上午
            midTime = format.parse("11:30").getTime();//中午
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //绘制边框的画笔
    private Paint getPathPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(COLOR_BOX));
        return paint;
    }

    //绘制长按虚线的画笔
    private Paint getDeshPathPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{8f, 8f}, 0);
        paint.setPathEffect(dashPathEffect);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(COLOR_BOX));
        return paint;
    }

    //绘制边框的画笔
    private Paint getLinePaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(1.5f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(COLOR_BLUE));
        return paint;
    }

    /**
     * 绘制文字的画笔 画笔颜色、字体大小需自己设置
     */
    private Paint getTextPaint() {
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT);
        return paint;
    }

    private String get4Decimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
        return decimalFormat.format(value);
    }

    private int dp2px(int dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    private int px2dp(int pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    //设置数据
    public void setData(ArrayList<ColumnBean> list) {
        this.list = list;
        int maxValueIndex = StringUtil.getMaxIndex(list, context, midValue);
        if (maxValueIndex == -1) {
            Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
            return;
        }
        isSet = true;
        this.list = list;
        maxValue = list.get(maxValueIndex).getNetValue();
        Log.e(TAG, "setData: maxValue  = " + maxValue);
        calcuateAvarage();
        postInvalidate();
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            isLongPress = true;
            postInvalidate();
        }
    }
}
