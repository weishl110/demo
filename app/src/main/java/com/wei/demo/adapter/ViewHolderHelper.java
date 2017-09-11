package com.wei.demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by ${wei} on 2017/6/6.
 */

public class ViewHolderHelper extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private Context mContext;
    private View mContentView;
    private int mPosition;
    private int mLayoutId;

    public ViewHolderHelper(Context context, View itemView, ViewGroup parent, int position) {
        super(itemView);
        this.mContext = context;
        this.mContentView = itemView;
        mPosition = position;
        mViews = new SparseArray<>();
        mContentView.setTag(this);
    }

    public static ViewHolderHelper get(Context context, View convertView, ViewGroup parent,
                                       int layoutId, int position) {
        if (convertView == null) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            ViewHolderHelper holderHelper = new ViewHolderHelper(context, itemView, parent, position);
            holderHelper.mLayoutId = layoutId;
            return holderHelper;
        } else {
            ViewHolderHelper holderHelper = (ViewHolderHelper) convertView.getTag();
            holderHelper.mPosition = position;
            return holderHelper;
        }
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mContentView;
    }

    /**
     * 给textview设置文本
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolderHelper setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * imageview设置图片
     *
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolderHelper setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    public ViewHolderHelper setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolderHelper setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolderHelper setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolderHelper setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolderHelper setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolderHelper setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ViewHolderHelper setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolderHelper setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolderHelper setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolderHelper setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolderHelper setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ViewHolderHelper setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public ViewHolderHelper setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolderHelper setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolderHelper setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolderHelper setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public ViewHolderHelper setOnClickListener(int viewId,
                                               View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolderHelper setOnTouchListener(int viewId,
                                               View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolderHelper setOnLongClickListener(int viewId,
                                                   View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public void updatePosition(int position) {
        mPosition = position;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

}
