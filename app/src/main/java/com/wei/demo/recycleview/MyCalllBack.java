package com.wei.demo.recycleview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by wei on 2016/10/9.
 */
public class MyCalllBack extends ItemTouchHelper.Callback {

    private ICallBackSwopLocation iCallBackSwopLocation;

    public MyCalllBack(ICallBackSwopLocation iCallBackSwopLocation){
        this.iCallBackSwopLocation = iCallBackSwopLocation;
    }
    //处理拖动方向
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragflags = ItemTouchHelper.DOWN| ItemTouchHelper.UP;//上下拖动
        int swipeFlags = ItemTouchHelper.LEFT;//左右滑动
        return makeMovementFlags(dragflags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        iCallBackSwopLocation.onMoveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}
