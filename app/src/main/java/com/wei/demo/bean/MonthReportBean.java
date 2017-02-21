package com.wei.demo.bean;

/**
 * Created by ${wei} on 2017/2/20.
 */

public class MonthReportBean {

    //总资产
    private float totalAsset;
    //持有资产
    private float hasAsset;
    //剩余体验金
    private float remaining;

    public float getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(float totalAsset) {
        this.totalAsset = totalAsset;
    }

    public float getHasAsset() {
        return hasAsset;
    }

    public void setHasAsset(float hasAsset) {
        this.hasAsset = hasAsset;
    }

    public float getRemaining() {
        return remaining;
    }

    public void setRemaining(float remaining) {
        this.remaining = remaining;
    }
}
