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
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.wei.demo.R;

/**
 * Created by ${wei} on 2017/8/14.
 * 粘性item
 */

public class StickySectionDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "debug_SectionDecoration";
    private final Decorationcallback callback;
    private final Paint paint;
    private final Paint.FontMetrics fontMetrics;
    private final int topGap;
    private final TextPaint textPaint;

    public StickySectionDecoration(Context context, Decorationcallback callback) {
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

   /* @Override
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
            if (TextUtils.isEmpty(textLine)) continue;

            if (position == 0 || isFirstInGroup(position)) {
                float top = view.getTop() - topGap;
                float bottom = view.getTop();
                c.drawRect(left, top, right, bottom, paint);

                float textHeight = fontMetrics.descent - fontMetrics.ascent;
                float textBottom = bottom - (bottom - top - textHeight);
                c.drawText(textLine, left, textBottom, textPaint);
            }
        }
    }*/

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        long preGroupId, groupId = -1;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            preGroupId = groupId;
            groupId = callback.getGroupId(position);
            if (groupId < 0 || preGroupId == groupId) continue;

            String textLine = callback.getGroupFirstLine(position);
            if (TextUtils.isEmpty(textLine)) continue;

            int viewBottom = view.getBottom();
            int textY = Math.max(topGap, view.getTop());
            if (position + 1 < itemCount) {
                long nextGroupId = callback.getGroupId(position + 1);
                if (nextGroupId != groupId && viewBottom < textY) {
                    textY = viewBottom;
                }
            }
            float top = textY - topGap;
            c.drawRect(left, top, right, textY, paint);

            float tempHeight = textY - top;
            float textHeight = fontMetrics.ascent + fontMetrics.descent;
            float textBottom = textY - (tempHeight - Math.abs(textHeight)) / 2;
            c.drawText(textLine, left, textBottom, textPaint);
        }
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
