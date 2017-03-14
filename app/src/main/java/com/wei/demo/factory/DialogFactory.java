package com.wei.demo.factory;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wei.demo.R;
import com.weigan.loopview.LoopView;

import java.util.ArrayList;

/**
 * Created by ${wei} on 2017/3/12.
 */

public class DialogFactory {


    private static Dialog dialog;
    private static boolean isShow;

    public static void createDialog(Context context, ArrayList<String> list,boolean isCancelable) {
        dialog = new Dialog(context, R.style.filtrateDialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_item, null);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        LoopView loopView = (LoopView) view.findViewById(R.id.loop_view);
        loopView.setItems(list);
        loopView.setNotLoop();

        MyOnClickListener listener = new MyOnClickListener();
        tv_cancel.setOnClickListener(listener);
        tv_confirm.setOnClickListener(listener);

        dialog.setContentView(view);
        dialog.setCancelable(isCancelable);

        //设置弹窗的大小
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = window.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = display.getWidth();
        lp.height = (int) (display.getHeight() * 0.4f);
        window.setAttributes(lp);
    }

    public static void show() {
        if (dialog != null) {
            dialog.show();
            isShow = true;
        }
    }

    public static void dismiss() {
        if (dialog != null) {
            isShow = false;
            dialog.dismiss();
            dialog = null;
        }
    }

    public static boolean isShow() {
        return isShow;
    }

    public static class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_cancel:
                case R.id.tv_confirm:
                    dialog.cancel();
                    break;
            }
        }
    }
}
