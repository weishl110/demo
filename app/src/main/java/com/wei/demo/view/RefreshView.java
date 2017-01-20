package com.wei.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wei.demo.R;

/**
 * Created by Administrator on 2017/1/2.
 */

public class RefreshView extends FrameLayout {

    private ViewDragHelper dragHelper;
    private View mContentView;
    private ViewGroup mTopView;
    private ListView listView;
    private RecyclerView recyclerView;
    private ScrollView scrollView;
    private ImageView iv_circel;
    private RelativeLayout rl_content;

    private int mWidth, mHeight;
    private int maxRange = 0;//最大下拉的高度
    private int refreshHeight;//下拉出发刷新的高度
    private int text_bottom;//中间文字的底部

    private int downY;
    private int childViewTop;

    private boolean isMoveChildView, isLayout;//是否包含可滑动的view true包含
    private boolean isStop, isStartAnim;//是否停止刷新，是否开启动画

    //下拉刷新的状态
    private final int STATE_DOWN = 0;//下拉中
    private final int STATE_REFRESHING = 1;//刷新中
    private final int STATE_REFRESH = 2;//可刷新
    private int current_state = STATE_DOWN;

    //属性值
    private int refresh_gravity = 1;//1 top 2 center 3 bottom
    private OnRefreshListener listener;
    private boolean isRefresh = true;//是否可以刷新
    private TextView tv_text;

    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.refreshView);
        refresh_gravity = ta.getInt(R.styleable.refreshView_refresh_gravity, 1);
        ta.recycle();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    /**
     * 是否可以刷新
     */
    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    /**
     * 初始化ViewDragHelper
     */
    private void init() {
        dragHelper = ViewDragHelper.create(this, 2.0f , new MyCallBack());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //健壮性检查
        if (getChildCount() > 1) {
            throw new RuntimeException("只能包含一个子view或viewgroup");
        }
        if (getChildCount() == 0) {
            throw new RuntimeException("not childview，childview would not be null！");
        }

        isLayout = false;
        //因为在onFinishInflate中添加一个viewgroup在0的位置
        mContentView = getChildAt(0);//获取布局中的view
        isMoveChildView = childType(mContentView);//查看是否有可滑动的view
        //添加下拉刷新头布局
        mTopView = (ViewGroup) View.inflate(getContext(), R.layout.layout_refresh, null);
        addView(mTopView, 0);

        rl_content = (RelativeLayout) mTopView.findViewById(R.id.fl_header);
        rl_content.measure(0, 0);
        refreshHeight = rl_content.getMeasuredHeight();
        tv_text = (TextView) mTopView.findViewById(R.id.tv_text);
        iv_circel = (ImageView) mTopView.findViewById(R.id.iv_circel);
        iv_circel.setAlpha(0.0f);
        //根据设置的属性设置头布局的位置
        if (refresh_gravity == 1) {
            rl_content.setGravity(Gravity.TOP);
        } else if (refresh_gravity == 2) {
            rl_content.setGravity(Gravity.CENTER);
        } else {
            rl_content.setGravity(Gravity.BOTTOM);
        }

    }

    /**
     * 判断此view是否是可滑动的view，或此view中是否包含可滑动的view（listview，scrollview，recyclerview）
     *
     * @param view
     * @return true则表示包含可滑动的view
     */
    private boolean childType(View view) {
        boolean conform = false;
        conform = isTypeConform(view, conform);
        if (!conform) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childView = viewGroup.getChildAt(i);
                    if (childView instanceof ViewGroup) {
                        boolean typeConform = isTypeConform(childView, conform);
                        if (typeConform) {
                            childViewTop = childView.getTop();//可滑动view的初始top值
                            return typeConform;
                        }
                        childType(childView);
                    }
                }
            }
        }
        return conform;
    }

    /**
     * 是否是可滑动的view
     */
    private boolean isTypeConform(View view, boolean type) {
        if (view instanceof ListView) {
            type = true;
            listView = (ListView) view;
        } else if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;
            type = true;
        } else if (view instanceof ScrollView) {
            scrollView = (ScrollView) view;
            type = true;
        }
        return type;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!isLayout) {
            if (iv_circel != null && isStartAnim) {
                iv_circel.clearAnimation();
                isStartAnim = false;
                iv_circel.setAlpha(0.0f);
            }
            super.onLayout(changed, left, top, right, bottom);
        }
        maxRange = mHeight - refreshHeight;//最大下拉距离
        text_bottom = refreshHeight - refreshHeight / 2;
        mTopView.layout(0, -(mHeight - refreshHeight), mWidth, refreshHeight);
        isLayout = false;
        //重新摆放刷新布局
        if (refresh_gravity == 1 && mContentView.getTop() >= refreshHeight) {
            mTopView.setTranslationY(mContentView.getTop() - refreshHeight);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = (int) ev.getY();
        }
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    private class MyCallBack extends ViewDragHelper.Callback {
        /**
         * 返回可拖拽的范围
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            //如果包含可滑动的view
            if (isMoveChildView && downY > childViewTop) {
                if (listView != null && listView.getAdapter() != null) {
                    View childView = listView.getChildAt(0);
                    if (listView.getAdapter().getCount() > 0 && childView != null && childView.getTop() < 0)
                        return super.getViewVerticalDragRange(child);
                } else if (scrollView != null && scrollView.getScrollY() > 0) {
                    return super.getViewVerticalDragRange(child);
                } else if (recyclerView != null && recyclerView.getAdapter() != null && recyclerView.getChildAt(0).getTop() < 0) {
                    return super.getViewVerticalDragRange(child);
                }
            }
            return maxRange;
        }

        /**
         * 返回true表示可以拖拽
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //如果触摸的是刷新的头部view  或者是设置为不可刷新则返回false
            if (child == mTopView || !isRefresh) {
                return false;
            }
            return true;
        }

        /**
         * 当被拖拽的view移动位置后，会调用此方法。可以用于处理View之间的联动
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //处理上面view的移动
            float moveTop = (float) top /** 0.9f*/;
            if (moveTop >= text_bottom) {
                float alpha = (moveTop - text_bottom) / text_bottom;
                if (alpha > 1) {
                    alpha = 1.0f;
                } else if (alpha < 0.0) {
                    alpha = 0.0f;
                }
                iv_circel.setAlpha(alpha);
            } else {
                iv_circel.setAlpha(0.0f);
            }

            if (tv_text != null) {
                isLayout = true;
                tv_text.setText("下拉刷新");
            }

            if (moveTop >= refreshHeight) {
                if (iv_circel != null && isStartAnim) {
                    isStartAnim = false;
                    iv_circel.clearAnimation();
                }
                if (refresh_gravity == 1 || refresh_gravity == 2) {
                    isLayout = true;
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_content.getLayoutParams();
                    params.height = (int) moveTop;
                    rl_content.setLayoutParams(params);
                    rl_content.requestLayout();
                }
                if (refresh_gravity == 2 || refresh_gravity == 3) {
                    float translationY = moveTop - refreshHeight;
                    mTopView.setTranslationY(translationY);
                }
            }
        }

        /**
         * 拖拽松开时调用
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int mContentViewTop = mContentView.getTop();
            if (mContentViewTop > refreshHeight) {
                current_state = STATE_REFRESH;
                refreshOpen();
            } else {
                close();
            }
        }

        /**
         * 返回top值
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            float newTop = top /** 0.87f*/;
            if (newTop >= maxRange) {
                newTop = maxRange;
            } else if (top < 0) {
                newTop = 0;
            }
            return (int) newTop;
        }
    }

    /**
     * 开始刷新 并且滚动至相应的位置
     */
    private void refreshOpen() {
        int top = refreshHeight;
        if (dragHelper.smoothSlideViewTo(mContentView, 0, top)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 关闭面板 此时下拉的高度没有达到刷新的高度，滚动至0的位置
     */
    private void close() {
        int top = 0;
        if (dragHelper.smoothSlideViewTo(mContentView, 0, top)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        mContentView.setFocusable(true);
        isLayout = false;
        current_state = STATE_DOWN;
        iv_circel.clearAnimation();
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else if (mContentView.getTop() >= refreshHeight && current_state == STATE_REFRESH) {
            startAnim();
            current_state = STATE_REFRESHING;//改变状态
            if (listener != null) {
                listener.onRefresh();
            }
            if (tv_text != null) {
                tv_text.setText("刷新中");
                isLayout = true;
            }
            mContentView.setFocusable(false);
        }
    }

    private long duration = 500L;

    private void startAnim() {
        isStop = false;
        duration = 500L;
        isStartAnim = true;
        RotateAnimation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        anim.setDuration(duration);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        iv_circel.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                current_state = STATE_DOWN;//清楚动画的同时并改变是否刷新的状态
                if (tv_text != null) {
                    isLayout = true;
                    tv_text.setText("刷新完毕");
                }
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        close();//回滚至顶部
                    }
                }, 300);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (!isStop) {
                    duration -= 100;
                    if (duration <= 230) {
                        duration = 230;
                    }
                } else {
                    duration += 100;
                    if (duration >= 500) {
                        duration = 500;
                        anim.setRepeatCount(1);
                    }
                }
                anim.setDuration(duration);
            }
        });
    }

    /**
     * 刷新完毕
     */
    public void refreshFinish() {
        isStop = true;
    }
}
