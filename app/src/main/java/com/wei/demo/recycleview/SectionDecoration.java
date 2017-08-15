package com.wei.demo.recycleview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import com.wei.demo.R;

/**
 * Created by ${wei} on 2017/8/14.
 * 添加联系人的首字母索引
 */

public class SectionDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "debug_SectionDecoration";
    private final Decorationcallback callback;
    private final Paint paint;
    private final Paint.FontMetrics fontMetrics;
    private final int topGap;
    private final TextPaint textPaint;

    public SectionDecoration(Context context, Decorationcallback callback) {
        Resources res = context.getResources();
        this.callback = callback;

        paint = new Paint();
        paint.setColor(res.getColor(R.color.colorAccent));

        textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(80);
        textPaint.setColor(Color.BLACK);
        fontMetrics = new Paint.FontMetrics();
        textPaint.getFontMetrics(fontMetrics);
        textPaint.setTextAlign(Paint.Align.LEFT);
        topGap = res.getDimensionPixelSize(R.dimen.value_dp32);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            long groupId = callback.getGroupId(position);
            if (groupId < 0) return;
            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (position == 0 || isFirstInGroup(position)) {
                float top = view.getTop() - topGap;
                float bottom = view.getTop();
                c.drawRect(left, top, right, bottom, paint);

                float textHeight = fontMetrics.descent + fontMetrics.ascent;
                float textBottom = bottom - (bottom - top - Math.abs(textHeight)) / 2;
                c.drawText(textLine, left, textBottom, textPaint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        long groupId = callback.getGroupId(position);
        if (groupId < 0) return;
        if (position == 0 || isFirstInGroup(position)) {
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }

    }

    private boolean isFirstInGroup(int position) {
        if (position == 0) {
            return true;
        } else {
            long preGroupId = callback.getGroupId(position - 1);
            long groupId = callback.getGroupId(position);
            return preGroupId != groupId;
        }
    }

    public interface Decorationcallback {
        long getGroupId(int position);

        String getGroupFirstLine(int position);
    }
}
