package com.wei.demo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wei.demo.R;
import com.wei.demo.bean.ChannelTab;
import com.wei.demo.listener.ItemDragHelperCallback;

import java.util.Collections;
import java.util.List;

/**
 * Created by ${wei} on 2017/6/9.
 */

public class ChannelAdapter extends CommRecylerViewAdapter<ChannelTab>
        implements ItemDragHelperCallback.OnItemMoveListener {

    private static final String TAG = "debug_ChannelAdapter";
    private ItemDragHelperCallback mItemDragHelperCallback;
    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setItemDragHelperCallback(ItemDragHelperCallback itemDragHelperCallback) {
        this.mItemDragHelperCallback = itemDragHelperCallback;
    }

    public ChannelAdapter(Context context, int layoutId, List<ChannelTab> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolderHelper viewHolderHelper, ChannelTab channelTab) {
        viewHolderHelper.setText(R.id.news_channel_tv, channelTab.getNewsChannelName());
        //设置textview的颜色，
        if (channelTab.getNewsChannelFixed()) {//不可点击的
            viewHolderHelper.setTextColor(R.id.news_channel_tv, ContextCompat.getColor(mContext, R.color.color_dcdcdc));
        } else {//可点击的
            viewHolderHelper.setTextColor(R.id.news_channel_tv, ContextCompat.getColor(mContext, R.color.alpha_50_black));
        }
        Log.e(TAG, "52行...convert:  = " +channelTab.toString());
        handlerLongPress(viewHolderHelper, channelTab);
        handlerOnClick(viewHolderHelper, channelTab);
    }


    private void handlerLongPress(ViewHolderHelper viewHolderHelper, final ChannelTab channelTab) {
        if (mItemDragHelperCallback != null) {
            viewHolderHelper.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mItemDragHelperCallback.setLongPressEnable(!(channelTab.getNewsChannelIndex() == 0));
                    return false;
                }
            });
        }
    }

    private void handlerOnClick(final ViewHolderHelper viewHolderHelper, final ChannelTab channelTab) {
        if (mOnItemClickListener != null) {
            viewHolderHelper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!channelTab.getNewsChannelFixed()) {
                        mOnItemClickListener.onItemClick(v, viewHolderHelper.getLayoutPosition());
                    }
                }
            });
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int targetPosition) {
        if (isChannelFixed(fromPosition, targetPosition)) {
            return false;
        }
        Collections.swap(getAll(), fromPosition, targetPosition);
        notifyItemMoved(fromPosition, targetPosition);

        return true;
    }

    private boolean isChannelFixed(int fromPosition, int toPosition) {
        return (getAll().get(fromPosition).getNewsChannelFixed() || getAll().get(toPosition).getNewsChannelFixed())
                && (fromPosition == 0 || toPosition == 0);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
