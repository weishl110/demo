package com.wei.demo.recycleview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.security.PolicySpi;

/**
 * Created by wei on 2016/10/9.
 */
public class MyCalllBack extends ItemTouchHelper.Callback {
    private static final String TAG = "debug_MyCalllBack";
    private ICallBackSwopLocation iCallBackSwopLocation;

    public MyCalllBack(ICallBackSwopLocation iCallBackSwopLocation) {
        this.iCallBackSwopLocation = iCallBackSwopLocation;
    }

    //处理拖动方向
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragflags = ItemTouchHelper.DOWN | ItemTouchHelper.UP;//上下拖动
        int swipeFlags = ItemTouchHelper.LEFT;//左右滑动
        return makeMovementFlags(dragflags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (iCallBackSwopLocation != null) {
            iCallBackSwopLocation.onMoveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e(TAG, "34行...onSwiped: direction = " + direction + "   " + viewHolder.getAdapterPosition());
        if (iCallBackSwopLocation != null && direction == ItemTouchHelper.LEFT) {
            iCallBackSwopLocation.onRemoveItem(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}
