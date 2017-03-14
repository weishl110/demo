package com.wei.demo.bean;

/**
 * Created by ${wei} on 2017/3/11.
 */

public class CircleValueBean {
    private float value;
    private String name;

    public CircleValueBean() {
    }

    public CircleValueBean(float value, String name) {
        this.value = value;
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
