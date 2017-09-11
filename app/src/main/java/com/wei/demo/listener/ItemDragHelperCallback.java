package com.wei.demo.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by ${wei} on 2017/6/9.
 */

public class ItemDragHelperCallback extends ItemTouchHelper.Callback {

    private OnItemMoveListener mOnItemMoveListener;
    private boolean mIsLongPressEnable;

    public interface OnItemMoveListener {
        boolean onItemMove(int fromPosition, int targetPosition);
    }

    public ItemDragHelperCallback(OnItemMoveListener onItemMoveListener) {
        this.mOnItemMoveListener = onItemMoveListener;
    }

    public void setLongPressEnable(boolean longPressEnable) {
        this.mIsLongPressEnable = longPressEnable;
    }

    public boolean isLongPressEnable() {
        return mIsLongPressEnable;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = setDragFlag(recyclerView);
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return !isDifferentItemViewType(viewHolder, target) && (mOnItemMoveListener != null
                && mOnItemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition()));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    /**
     * 根据layoutManager判断可拖动的方向
     *
     * @param recyclerView
     * @return
     */
    public int setDragFlag(RecyclerView recyclerView) {
        int dragFlags;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        return dragFlags;
    }

    private boolean isDifferentItemViewType(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return viewHolder.getItemViewType() != target.getItemViewType();
    }
}
