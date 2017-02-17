package com.wei.demo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wei.demo.R;
import com.wei.demo.adapter.MyRecyclerAdapter;

/**
 * Created by ${wei} on 2017/2/10.
 */

public class BouncingMenu {

    private ViewGroup mParentVG;
    private View rootView;
    private BouncingView bouncingView;
    private RecyclerView recyclerView;
    private boolean isShow;

    public BouncingMenu(View view, int resId, MyRecyclerAdapter adapter) {
        //查找跟布局
        mParentVG = findRootParent(view);
        rootView = LayoutInflater.from(view.getContext()).inflate(resId, null, false);

        bouncingView = (BouncingView) rootView.findViewById(R.id.bv);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        recyclerView.setAdapter(adapter);

    }

    /**
     * 查找最外层的父布局
     */
    private ViewGroup findRootParent(View view) {
        do {
            if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    return (ViewGroup) view;
                }
            }
            if (view != null) {
                ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);
        return null;
    }

    public static BouncingMenu makeMenu(View view, int resId, MyRecyclerAdapter adapter) {
        return new BouncingMenu(view, resId, adapter);
    }

    public BouncingMenu show() {
        if (mParentVG != null) {
            if (rootView.getParent() != null) {
                mParentVG.removeView(rootView);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mParentVG.addView(rootView, lp);
            isShow = true;
            bouncingView.show();
        }
        return this;
    }

    public void dismiss() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(rootView, "translationY", 0, rootView.getHeight());
        animator.setDuration(600);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mParentVG.removeView(rootView);
                rootView = null;
                isShow = false;
            }
        });
        animator.start();
    }

    public boolean isShow() {
        return isShow;
    }

}
