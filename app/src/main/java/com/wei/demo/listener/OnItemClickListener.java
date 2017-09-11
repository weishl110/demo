package com.wei.demo.listener;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ${wei} on 2017/6/9.
 */

public interface OnItemClickListener<T> {
    void onItemClick(ViewGroup viewGroup, View view, T t, int position);

    boolean onItemLongClick(ViewGroup viewGroup, View view, T t, int position);
}
