package com.wei.demo.listener;

/**
 * Created by ${wei} on 2017/6/12.
 */

public class ChannelItemMoveEvent {

    private int fromPosition;
    private int toPosition;

    public ChannelItemMoveEvent(int fromPosition, int toPosition) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public int getToPosition() {
        return toPosition;
    }
}
