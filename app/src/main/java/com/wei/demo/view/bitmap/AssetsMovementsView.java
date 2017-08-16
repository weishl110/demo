package com.wei.demo.view.bitmap;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.wei.demo.bean.ColumnBean;
import com.wei.demo.bean.PointFLocal;
import com.wei.demo.view.LineEvaltor;

import java.util.ArrayList;

/**
 * Created by ${wei} on 2017/2/21.
 * <p>
 * 资产走势
 */

public class AssetsMovementsView extends BaseChartView {
    private static final String TAG = "debug_AssetsMovementsView";
    private float mPointSpec;
    private ArrayList<ColumnBean> list;
    private String[] leftTexts;
    private float maxValue;
    private ArrayList<PointFLocal> mTotalPointFs = new ArrayList<>();
    private ArrayList<PointFLocal> mHasAssetsPointFs = new ArrayList<>();
    private Path totalAlphaPath, hasAssetAlphaPath;

    //背景颜色
    private static final String BGSTARTCOLOR = "#66ff5a00";
    private static final String BGENDCOLOR = "#11ff5a00";
    private static final String BGBLUESTARTCOLOR = "#77429AE6";
    private static final String BGBLUEENDCOLOR = "#11429AE6";

    private static final String COLOR_429AE6 = "#4a9ae6";
    private static final String COLOR_FF5A00 = "#ff5a00";

    private static final String[] texts = {"持有资产", "总资产"};
    private ObjectAnimator animator;


    public AssetsMovementsView(Context context) {
        this(context, null);
    }

    public AssetsMovementsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AssetsMovementsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimation();
    }

    float percent = 0;

    private void initAnimation() {
        animator = ObjectAnimator.ofFloat(this, "percent", 0, 1);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);

        totalAlphaPath = new Path();
        hasAssetAlphaPath = new Path();
    }

    public void setPercent(float percent) {
        this.percent = percent;
        calculatePath(list);
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (list != null && list.size() > 0) {
            mPointSpec = mWidth / (list.size() - 1);
            //计算点的位置
            calculatePointLocal(list);
        }
    }

    @Override
    protected void drawOther(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(COLOR_429AE6));
        paint.setTextSize(textsize);

        int tempLeft = mWidth / 2;
        int left = tempLeft + tempLeft / 7 + MARGIN;
        int top = marginTop - textsize - verticalSpec / 2;
        int right = left + block_size;
        int bottom = top + block_size;
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, paint);
        paint.setColor(Color.parseColor(COLOR_LEFTTEXT));
        canvas.drawText(texts[0], right + verticalSpec * 2, bottom, paint);

        //测量文案的宽度
        paint.setColor(Color.parseColor(COLOR_FF5A00));
        float textWidth = paint.measureText(texts[0]);
        left = (int) (left + textWidth + tempLeft / 4);
        right = left + block_size;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, paint);
        paint.setColor(Color.parseColor(COLOR_LEFTTEXT));
        canvas.drawText(texts[1], right + verticalSpec * 2, bottom, paint);

    }

    @Override
    protected void drawLeftText(Canvas canvas) {
        if (leftTexts == null) return;
        Paint textPaint = getTextPaint(COLOR_LEFTTEXT, textsize);
        drawLeftText(canvas, leftTexts, textPaint);
    }

    @Override
    protected void drawLine(Canvas canvas) {
        if (mTotalPointFs.size() == 0 || mHasAssetsPointFs.size() == 0) return;
        int size = mTotalPointFs.size();
        PointFLocal currentPointF, nextPointF;
        Paint paint = getLinePaint();
        //绘制总资产
        paint.setColor(Color.parseColor("#ff5a00"));

        float bottom = marginTop + mHeight;
        for (int i = 0; i < size - 1; i++) {
            currentPointF = mTotalPointFs.get(i);
            nextPointF = mTotalPointFs.get(i + 1);
            float tempCurrentY = bottom - currentPointF.y;
            float tempnextY = bottom - nextPointF.y;
            canvas.drawLine(currentPointF.x, bottom - tempCurrentY * percent, nextPointF.x, bottom - tempnextY * percent, paint);
        }

        //绘制持有资产
        size = mHasAssetsPointFs.size() - 1;
        paint.setColor(Color.parseColor("#429ae6"));
        for (int i = 0; i < size; i++) {
            currentPointF = mHasAssetsPointFs.get(i);
            nextPointF = mHasAssetsPointFs.get(i + 1);
            float tempCurrentY = bottom - currentPointF.y;
            float tempnextY = bottom - nextPointF.y;
            canvas.drawLine(currentPointF.x, bottom - tempCurrentY * percent, nextPointF.x, bottom - tempnextY * percent, paint);
        }
        //绘制背景
//        paint.setColorFilter(new PorterDuffColorFilter(0x66429ae6, PorterDuff.Mode.XOR));
        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(0);
        float x = mTotalPointFs.get(0).x;
        LinearGradient linearGradient = new LinearGradient(x, marginTop, 0, marginTop + mHeight,
                Color.parseColor(BGSTARTCOLOR), Color.parseColor(BGENDCOLOR), Shader.TileMode.MIRROR);
        paint.setShader(linearGradient);
        canvas.drawPath(totalAlphaPath, paint);

        linearGradient = new LinearGradient(x, marginTop, x, marginTop + mHeight,
                Color.parseColor(BGBLUESTARTCOLOR), Color.parseColor(BGBLUEENDCOLOR), Shader.TileMode.MIRROR);
        paint.setShader(linearGradient);
        canvas.drawPath(hasAssetAlphaPath, paint);

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

    @Override
    protected void drawLong(Canvas canvas) {
        drawLongBottomDateAndDeshLine(canvas, mTotalPointFs, list, longDownX, "yyyyMMdd", "yyyy-MM-dd");
    }


    public void setData(ArrayList<ColumnBean> list) {
        if (list == null || list.size() == 0) {
            isSetData = false;
            postInvalidate();
            return;
        }
        this.list = list;
        isSetData = true;
        requestLayout();
        //获取最大值
        maxValue = getMaxValueIndex(list);
        //计算左侧数值
        leftTexts = calculateLeftText(list, maxValue);
//        postInvalidate();
        animator.start();
    }

    /**
     * 获取集合中的最大值
     *
     * @param list
     * @return
     */
    private float getMaxValueIndex(ArrayList<ColumnBean> list) {
        int size = list.size();
        float maxValue = list.get(0).getHasValue();
        for (int i = 0; i < size; i++) {
            ColumnBean columnBean = list.get(i);
            float hasValue = columnBean.getHasValue();
            float totalValue = columnBean.getTotalValue();
            maxValue = hasValue > maxValue ? hasValue : maxValue;
            maxValue = totalValue > maxValue ? totalValue : maxValue;
        }
        return maxValue;
    }

    /**
     * 计算点的位置
     *
     * @param list
     */
    private void calculatePointLocal(ArrayList<ColumnBean> list) {
        int size = list.size();
        mHasAssetsPointFs.clear();
        mTotalPointFs.clear();

        totalAlphaPath.reset();
        hasAssetAlphaPath.reset();
        float bottom = marginTop + mHeight;
        totalAlphaPath.moveTo(MARGIN, bottom);
        hasAssetAlphaPath.moveTo(MARGIN, bottom);
        for (int i = 0; i < size; i++) {
            PointFLocal pointF = new PointFLocal();
            ColumnBean columnBean = list.get(i);
            //持有资产
            pointF.x = i * mPointSpec + MARGIN + STOREWIDTH / 2;
            pointF.y = (maxValue - columnBean.getHasValue()) / mAvarageValue * mAvarageLine + marginTop + STOREWIDTH / 2;
            mHasAssetsPointFs.add(pointF);

//            hasAssetAlphaPath.lineTo(pointF.x, pointF.y);
            pointF = new PointFLocal();
            pointF.x = i * mPointSpec + MARGIN + STOREWIDTH / 2;
            //总资产
            pointF.y = (maxValue - columnBean.getTotalValue()) / mAvarageValue * mAvarageLine + marginTop + STOREWIDTH / 2;
            mTotalPointFs.add(pointF);

//            totalAlphaPath.lineTo(pointF.x, pointF.y);
        }
//        float x = mHasAssetsPointFs.get(mHasAssetsPointFs.size() - 1).x;
//        totalAlphaPath.lineTo(x, marginTop + mHeight);
//        hasAssetAlphaPath.lineTo(x, marginTop + mHeight);
//        totalAlphaPath.close();
//        hasAssetAlphaPath.close();
    }

    private void calculatePath(ArrayList<ColumnBean> list) {
        int size = list.size();
        totalAlphaPath.reset();
        hasAssetAlphaPath.reset();
        float totalLastX = MARGIN + mWidth;
        float hasAssetLastX = MARGIN + mWidth;

        float bottom = marginTop + mHeight;
        totalAlphaPath.moveTo(MARGIN, bottom);
        hasAssetAlphaPath.moveTo(MARGIN, bottom);
        for (int i = 0; i < size; i++) {
            PointFLocal pointF = new PointFLocal();
            ColumnBean columnBean = list.get(i);
            //持有资产
            pointF.x = i * mPointSpec + MARGIN + STOREWIDTH / 2;
            pointF.y = (bottom - ((maxValue - columnBean.getHasValue()) / mAvarageValue * mAvarageLine + marginTop + STOREWIDTH / 2)) * percent;
            pointF.y = bottom - pointF.y;
            totalLastX = pointF.x;

            hasAssetAlphaPath.lineTo(pointF.x, pointF.y);
            pointF = new PointFLocal();
            pointF.x = i * mPointSpec + MARGIN + STOREWIDTH / 2;
            //总资产
            pointF.y = bottom - (bottom - ((maxValue - columnBean.getTotalValue()) / mAvarageValue * mAvarageLine + marginTop + STOREWIDTH / 2)) * percent;
            totalAlphaPath.lineTo(pointF.x, pointF.y);
            hasAssetLastX = pointF.x;
        }
        totalAlphaPath.lineTo(totalLastX, marginTop + mHeight);
        hasAssetAlphaPath.lineTo(hasAssetLastX, marginTop + mHeight);
        totalAlphaPath.close();
        hasAssetAlphaPath.close();

    }


    /**
     * 计算左侧数值
     *
     * @param list
     * @param maxValue 集合中最大值
     * @return 返回左侧数值的数组
     */
    private String[] calculateLeftText(ArrayList<ColumnBean> list, float maxValue) {
        String[] leftTexts = new String[7];
        int length = leftTexts.length;
        //平均值
        mAvarageValue = maxValue / (length - 1);
        int index = length - 1;
        for (int i = 0; i < length; i++) {
            leftTexts[i] = formatValue(mAvarageValue * index);
            if (i == length - 1) {
                leftTexts[i] = "0.00";
            }
            index--;
        }
        return leftTexts;
    }

}
