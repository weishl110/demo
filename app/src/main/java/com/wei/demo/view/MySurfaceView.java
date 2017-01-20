package com.wei.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by ${wei} on 2017/1/13.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private static final String TAG = "zpy_MySurfaceView";
    private Canvas canvas;

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    boolean isDrawing = false;


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        canvas = mHolder.lockCanvas();
        isDrawing = true;
        new Thread(new MyRunnable()).start();
        Log.e(TAG, "surfaceCreated:");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
        Log.e(TAG, "surfaceDestroyed");
        mHolder.unlockCanvasAndPost(canvas);
    }

    private class MyRunnable implements Runnable{
        @Override
        public void run() {
            Log.e(TAG, "run: ");
        }
    }
}
