package com.wei.demo.test;

/**
 * Created by ${wei} on 2017/9/5.
 */

public abstract class AbstractHandler {

    private Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
