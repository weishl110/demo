package com.wei.demo.recycleview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by ${wei} on 2017/8/14.
 */

public class MyItemDecorration extends RecyclerView.ItemDecoration {

    private static final String TAG = "debug_MyItemDecorration";
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        Log.e(TAG, "21行...onDraw: state = " + state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

    }
}
