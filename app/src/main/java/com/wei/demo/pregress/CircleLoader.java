package com.wei.demo.pregress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.wei.demo.R;

public class CircleLoader extends View {
    private ParamsCreator paramsCreator = new ParamsCreator(this.getContext());
    private Paint paint = new Paint();
    //属性
    private String text;
    private int textSize;
    private int textColor;
    private int textStrokeWidth;
    private boolean textFakeBold;//文字是否加粗
    private int circleRadius;//内圆半径
    private int circleStrokeWidth;//圆环的宽度
    private int circleColor;//圆环的颜色
    private int circleAndTextSpacing;//圆和文字的间距
    private int backgroundColor;
    //运行时
    private int startAngle;//画圆的起点

    public CircleLoader(Context context) {
        super(context);
    }

    public CircleLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.circleloader);
        text = a.getString(R.styleable.circleloader_text);
        text = text == null ? "" : text.trim();
        textSize = (int) a.getDimension(R.styleable.circleloader_textSize, paramsCreator.getDefaultTextSize());

        textColor = (int) a.getColor(R.styleable.circleloader_textColor, 0);
        if (textColor == 0)
            textColor = a.getResourceId(R.styleable.circleloader_textColor, 0);
        if (textColor == 0)
            textColor = 0xFF2E7FA9;

        textFakeBold = (boolean) a.getBoolean(R.styleable.circleloader_textFakeBold, true);
        textStrokeWidth = (int) paint.getStrokeWidth();

        circleRadius = (int) a.getDimension(R.styleable.circleloader_circleRadius, paramsCreator.getDefaultRadiusForCircle());
        circleStrokeWidth = (int) a.getDimension(R.styleable.circleloader_circleStrokeWidth, paramsCreator.getDefaultStrokeWidthForCircle());

        circleColor = (int) a.getColor(R.styleable.circleloader_circleColor, 0);
        if (circleColor == 0)
            circleColor = a.getResourceId(R.styleable.circleloader_circleColor, 0);
        if (circleColor == 0)
            circleColor = 0xFF2E7FA9;

        backgroundColor = a.getColor(R.styleable.circleloader_backgroundColor, 0);
        if (backgroundColor == 0)
            backgroundColor = a.getResourceId(R.styleable.circleloader_backgroundColor, 0);

        circleAndTextSpacing = (int) a.getDimension(R.styleable.circleloader_circleAndTextSpacing, 0);
        if (circleAndTextSpacing == 0) {
            setPaintForText();
            FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
            circleAndTextSpacing = (int) (txtHeight * 0.6);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 计算组件宽度
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getDefaultWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 计算组件高度
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getDefaultHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 计算默认宽度
     */
    private int getDefaultWidth() {
        int defaultWidth = this.circleRadius * 2 + this.circleStrokeWidth * 2;
        if (this.text.equals(""))
            return defaultWidth;
        setPaintForText();
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int txtWidth = (int) paint.measureText(this.text);
        if (txtWidth > defaultWidth)
            return txtWidth;
        return defaultWidth;
    }

    /**
     * 计算默认宽度
     */
    private int getDefaultHeight() {
        int defaultHeight = this.circleRadius * 2 + this.circleStrokeWidth * 2;
        if (this.text.equals(""))
            return defaultHeight;
        setPaintForText();
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
        return defaultHeight + circleAndTextSpacing + txtHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        if (this.backgroundColor != 0) {
            canvas.drawColor(this.backgroundColor);
        }
        setPaintForText();
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
        if (!this.text.equals("")) {//有文字
            int totalHeight = (this.circleRadius + this.circleStrokeWidth) * 2 + txtHeight + circleAndTextSpacing;
            int circleTop = this.getHeight() / 2 - totalHeight / 2;
            drawCircle(canvas, circleTop);
            int textTop = circleTop + (+this.circleRadius + this.circleStrokeWidth) * 2 + circleAndTextSpacing;
            drawText(canvas, textTop);
            this.invalidate();
            return;
        }
        //没有文字
        int circleTotalHeight = this.circleRadius * 2 + this.circleStrokeWidth * 2;//圆的总高度
        drawCircle(canvas, this.getHeight() / 2 - circleTotalHeight / 2);
        this.invalidate();
    }

    /**
     * 画圆
     */
    private void drawCircle(Canvas canvas, int top) {
        startAngle += 4;
        if (startAngle == 360)
            startAngle = 0;
        setPaintForCircle();
        int circleTotalWidth = this.circleRadius * 2 + this.circleStrokeWidth * 2;//圆的总宽度
        RectF oval = new RectF();
        oval.left = this.getWidth() / 2 - circleTotalWidth / 2;
        oval.top = top;
        oval.right = oval.left + circleTotalWidth;
        oval.bottom = oval.top + circleTotalWidth;
        oval.left += this.circleStrokeWidth;
        oval.top += this.circleStrokeWidth;
        oval.right -= this.circleStrokeWidth;
        oval.bottom -= this.circleStrokeWidth;
        canvas.drawArc(oval, startAngle, 160, false, paint);
    }

    /**
     * 画文字
     */
    private void drawText(Canvas canvas, int top) {
        setPaintForText();
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int txtWidth = (int) paint.measureText(this.text);
        canvas.drawText(this.text, this.getWidth() / 2 - txtWidth / 2, top - fontMetrics.ascent, paint);
    }

    /**
     * 设置paint值，为text
     */
    private void setPaintForText() {
        paint.setTextSize(this.textSize);
        paint.setColor(this.textColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth((float) textStrokeWidth);
        paint.setFakeBoldText(textFakeBold);
    }

    /**
     * 设置paint值，为Circle
     */
    private void setPaintForCircle() {
        paint.setAntiAlias(true);//定义画笔无锯齿
        paint.setColor(this.circleColor);
        paint.setStrokeWidth((float) this.circleStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(100);
    }
}
