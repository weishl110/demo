package com.wei.demo.bean;

/**
 * Created by Administrator on 2016/12/24.
 */

public class ColumnBean {
    //日期
    private String date;
    //
    private float value;
    //总资产
    private float totalValue;
    //持有资产
    private float hasValue;
    private float netValue;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(float netValue) {
        this.netValue = netValue;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }

    public float getHasValue() {
        return hasValue;
    }

    public void setHasValue(float hasValue) {
        this.hasValue = hasValue;
    }

    @Override
    public String toString() {
        return "ColumnBean{" +
                "date='" + date + '\'' +
                ", value=" + value +
                ", totalValue=" + totalValue +
                ", hasValue=" + hasValue +
                ", netValue=" + netValue +
                '}';
    }
}
