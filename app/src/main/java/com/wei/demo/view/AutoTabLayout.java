package com.wei.demo.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wei.demo.R;
import com.wei.demo.bean.TabItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${wei} on 2017/11/12.
 */

public class AutoTabLayout extends FrameLayout {

    private List<TabItem> mItems = new ArrayList<>();
    private Context mContext;
    private int mHorizontalSpec = 10;

    private IndicatorView mIndicatorViews = new IndicatorView();
    private float mScrollMaxDistance;//可滑动距离
    private float downX, mOffsetX;
    //控制滑动
    private OverScroller mOverScroller;
    //最大滑动距离
//    private int mMaxScroll;
    //速度获取
//    private VelocityTracker mVelocityTracker;
    //惯性最大最小速度
    private int mMinimumVelocity;

    //自定义属性
    private boolean isShowImg;
    private int imgRes;
    private int imgWidth;
    private int imgHeight;
    private int imgGravity;

    private int textSize;
    private int textColor;
    private ColorStateList textColorStateList;

    private int textNum;
    private boolean isShowNum;
    private int numColor;
    private ColorStateList numColorStateList;

    private int horizontalSpec;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int bgColor;
    private final int IMG_LEFT = 0X11;
    private final int IMG_TOP = 0x12;
    private final int IMG_RIGHT = 0X13;
    private final int IMG_BOTTOM = 0x14;
    private int imgMargin;
    private int numTextSize;
    private int mWidth;
    private BaseAdapter mAdapter;

    private static final int STYLE_ROUND = 11;
    private static final int STYLE_NORMAL = 12;

    private static final int GRAVITY_TOP = 0x21;
    private static final int GRAVITY_BOTTOM = 0x22;

    private boolean isShowIndicator;
    private int indicatorHeight, indicatorColor, indicatorMargin, indicatorStyle, indicatorRadius, indicatorGravity;
    private Paint mIndicatorPaint;
    private OnItemClickListener mOnItemClickListener;
    private GestureDetector mDetector;


    public AutoTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        getAttr(context, attrs);

        initIndicatorPaint();
    }

    private void getAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoTabLayout);
        isShowImg = ta.getBoolean(R.styleable.AutoTabLayout_tl_isShowImage, false);
        imgRes = ta.getResourceId(R.styleable.AutoTabLayout_tl_imageRes, -1);
        imgWidth = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_imageWidth, 0);
        imgHeight = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_imageHeight, 0);
        imgGravity = ta.getInt(R.styleable.AutoTabLayout_tl_imageGravity, IMG_TOP);

        textSize = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_textSize, 0);
        textColor = ta.getColor(R.styleable.AutoTabLayout_tl_textColor, -1);
        textColorStateList = ta.getColorStateList(R.styleable.AutoTabLayout_tl_textColor);

        textNum = ta.getInteger(R.styleable.AutoTabLayout_tl_textNum, 0);
        numTextSize = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_numSize, -1);
        isShowNum = ta.getBoolean(R.styleable.AutoTabLayout_tl_isShowNum, false);
        numColor = ta.getColor(R.styleable.AutoTabLayout_tl_tabBg, -1);
        numColorStateList = ta.getColorStateList(R.styleable.AutoTabLayout_tl_tabBg);

        horizontalSpec = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_horizontalSpec, 0);
        imgMargin = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_textImageMargin, 0);
        paddingLeft = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_paddingLeft, 0);
        paddingTop = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_paddingTop, 0);
        paddingRight = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_paddingRight, 0);
        paddingBottom = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_paddingBottom, 0);

        bgColor = ta.getColor(R.styleable.AutoTabLayout_tl_background, -1);
        if (bgColor == -1) {
            bgColor = ta.getResourceId(R.styleable.AutoTabLayout_tl_background, -1);
        }
//        bgColorStateList = ta.getColorStateList(R.styleable.AutoTabLayout_tl_background);

        isShowIndicator = ta.getBoolean(R.styleable.AutoTabLayout_tl_isShowIndicator, false);
        indicatorHeight = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_indicatorHeight, 0);
        indicatorColor = ta.getColor(R.styleable.AutoTabLayout_tl_indicatorColor, -1);
        indicatorMargin = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_indicatorMargin, 0);
        indicatorStyle = ta.getInt(R.styleable.AutoTabLayout_tl_indicatorStyle, STYLE_NORMAL);
        indicatorRadius = ta.getDimensionPixelOffset(R.styleable.AutoTabLayout_tl_indicatorRadius, 0);
        indicatorGravity = ta.getInt(R.styleable.AutoTabLayout_tl_indicatorGravity, GRAVITY_BOTTOM);
        ta.recycle();
    }

    private void init() {
        mOverScroller = new OverScroller(mContext);
        //配置速度
        mMinimumVelocity = ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity();

        mDetector = new GestureDetector(mContext, new MyGestureListener());
    }

    private void initIndicatorPaint() {
        if (!isShowIndicator) return;
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setColor(indicatorColor);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setStrokeWidth(px2dp(indicatorHeight));
        mIndicatorPaint.setStrokeCap(indicatorStyle == STYLE_ROUND ? Paint.Cap.ROUND : Paint.Cap.BUTT);
    }

    private static final String TAG = "debug_AutoTabLayout";

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();

        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (childCount == 0) {
            return;
        }
        mIndicatorViews = new IndicatorView();
        int childHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.measure(0, 0);
            mIndicatorViews.addView(childView);
            childHeight = Math.max(childHeight, childView.getMeasuredHeight());
        }
        if (isShowIndicator) {
            childHeight += indicatorHeight;
        }
        setMeasuredDimension(width, childHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int size = getChildCount();
        if (size == 0) {
            return;
        }

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        if (indicatorGravity == GRAVITY_TOP) {
            paddingTop += indicatorHeight + indicatorMargin;
        }
        int left = 0, right = 0, bottom;
        //计算是否超出屏幕
        int remainSpec = getWidth() - paddingLeft - paddingRight - mIndicatorViews.getLineWidth();
        float perSpec = -1;
        mScrollMaxDistance = Math.abs(remainSpec);
        if (remainSpec > 0) {
            perSpec = remainSpec / size;
            mIndicatorViews.clearViews();
        }

        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            //处理留白
            if (perSpec > 0) {
                int widthSpec = MeasureSpec.makeMeasureSpec((int) (child.getMeasuredWidth() + perSpec), MeasureSpec.EXACTLY);
                child.measure(widthSpec, 0);
            }

            if (i == 0) {
                left = paddingLeft;
                right = left + child.getMeasuredWidth();
                bottom = paddingTop + child.getMeasuredHeight();
                child.layout(left, paddingTop, right, bottom);
            } else {
                left = right + mHorizontalSpec;
                right = left + child.getMeasuredWidth();
                bottom = paddingTop + child.getMeasuredHeight();
                child.layout(left, paddingTop, right, bottom);
            }

            mIndicatorViews.addView(child);
        }

        setCurrentTab(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        if (isShowIndicator) {
            if (indicatorGravity == GRAVITY_TOP) {
                mStartY = paddingTop;
            } else if (indicatorGravity == GRAVITY_BOTTOM) {
                mStartY = h - paddingBottom;
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());
            postInvalidate();
        }
    }

    float mStartX = 0, mStartY = 0, mEndX = 0;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mIndicatorPaint == null || !isShowIndicator) return;
        if (indicatorGravity == GRAVITY_TOP) {
            mStartY += indicatorMargin;
        } else if (indicatorGravity == GRAVITY_BOTTOM) {
            mStartY -= indicatorMargin;
        }
        android.util.Log.e(TAG, "dispatchDraw:  = " + mStartX + "    endx = " + mEndX);
        canvas.drawLine(mStartX, mStartY, mEndX, mStartY, mIndicatorPaint);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIndicatorViews.getLineWidth() > mWidth) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIndicatorViews.getLineWidth() <= mWidth) {
            return super.onTouchEvent(event);
        }
        return mDetector.onTouchEvent(event);
//        mVelocityTracker.addMovement(event);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                getParent().requestDisallowInterceptTouchEvent(true);
//                downX = event.getX();
//                mVelocityTracker.clear();
//                mVelocityTracker.addMovement(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveX = event.getX();
//                float diffX = moveX - downX;
//                if (mScrollMaxDistance > 0) {
//                    mOffsetX = diffX;
//                    scrollBy((int) (-mOffsetX), 0);
//                    downX = moveX;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
//                float xVelocity = mVelocityTracker.getXVelocity();
//                if (Math.abs(xVelocity) > mMinimumVelocity) {
//                    fling((int) -xVelocity);
//                }
//                break;
//        }
//        return true;
    }

    private void fling(int velocityX) {
        mOverScroller.fling(getScrollX(), 0, velocityX, 0, 0, (int) mScrollMaxDistance, 0, 0);
        postInvalidate();
    }

    //重写滑动方法，防止滑出边界
    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (x < 0) {
            x = 0;
        } else if (x > mScrollMaxDistance) {
            x = (int) mScrollMaxDistance;
        }
        if (x != getScrollX()) {
            super.scrollTo(x, y);
        }
    }


    public void setHorizontalSpec(int horizontalSpec) {
        this.mHorizontalSpec = horizontalSpec;
    }

    public void setItems(List<TabItem> list) {
        mItems.clear();
        mItems.addAll(list);
        addView();
        requestLayout();
    }

    public void setAdapter(BaseAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("adapter cannot be null");
        }
        this.mAdapter = adapter;
        addAdapterView(adapter);
    }

    public void notifyAdapter() {
        if (mAdapter != null) {
            addAdapterView(mAdapter);
        }
    }

    private void addAdapterView(BaseAdapter adapter) {
        int itemCount = adapter.getItemCount();
        this.removeAllViews();
        mOffsetX = 0;
        scrollTo(0, 0);
        for (int i = 0; i < itemCount; i++) {
            View itemView = adapter.getItemView(i, this);
            if (itemView == null) {
                throw new IllegalArgumentException("getItemView() return view cannot be null");
            }
            android.util.Log.e(TAG, "addAdapterView:  = " + i);
            click(itemView, i);
            addView(itemView);
        }
        requestLayout();
    }

    private int mPreTabPosition, mCurrTabPosition;

    public void setCurrentTab(int position) {
        if (getChildCount() == 0) return;
        if (position >= getChildCount() || position < 0) {
            throw new IndexOutOfBoundsException("position :" + position + "  size : " + getChildCount());
        }

        mPreTabPosition = mCurrTabPosition;
        mCurrTabPosition = position;

        updataIndicator();
        postInvalidate();
    }

    private void updataIndicator() {
        int currIndicatorWidth = mIndicatorViews.getChildWidth(mCurrTabPosition);
        mStartX = getChildAt(mCurrTabPosition).getLeft();
        mEndX = mStartX + currIndicatorWidth;
    }

    private void addView() {
        if (mItems == null && mItems.size() == 0) {
            return;
        }

        int size = mItems.size();
        this.removeAllViews();
        mOffsetX = 0;
        scrollTo(0, 0);
        for (int i = 0; i < size; i++) {
            TabItem tabItem = mItems.get(i);
            View view = View.inflate(mContext, R.layout.layout_indicator, null);
            RelativeLayout ll_continer = (RelativeLayout) view.findViewById(R.id.indicator_continer);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
            TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
            //外布局
            ll_continer.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            if (bgColor != -1) {
                ll_continer.setBackgroundColor(bgColor);
            }

            //图片
            if (!isShowImg) {
                iv_icon.setVisibility(GONE);
            } else {
                iv_icon.setVisibility(VISIBLE);
                iv_icon.setImageResource(tabItem.imgRes);
                if (imgWidth != -1 && imgHeight != -1) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_icon.getLayoutParams();
                    params.width = imgWidth;
                    params.height = imgHeight;
                    iv_icon.setLayoutParams(params);
                }

                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_text.getLayoutParams();
                RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) iv_icon.getLayoutParams();
                ivParams.height = imgHeight > 0 ? (int) imgHeight : LinearLayout.LayoutParams.WRAP_CONTENT;
                ivParams.width = imgWidth > 0 ? (int) imgWidth : LinearLayout.LayoutParams.WRAP_CONTENT;
                int margin = px2dp(imgMargin);
                if (imgGravity == IMG_LEFT) {
                    tvParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    ivParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    tvParams.addRule(RelativeLayout.RIGHT_OF, iv_icon.getId());
                    ivParams.rightMargin = margin;
                } else if (imgGravity == IMG_RIGHT) {
                    tvParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    ivParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    ivParams.addRule(RelativeLayout.RIGHT_OF, tv_text.getId());
                    ivParams.leftMargin = margin;
                } else if (imgGravity == IMG_TOP) {
                    tvParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    ivParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    tvParams.addRule(RelativeLayout.BELOW, iv_icon.getId());
                    ivParams.bottomMargin = margin;
                } else if (imgGravity == IMG_BOTTOM) {
                    tvParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    ivParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    tvParams.addRule(RelativeLayout.ABOVE, iv_icon.getId());
                    ivParams.topMargin = margin;
                }
                iv_icon.setLayoutParams(ivParams);
                tv_text.setLayoutParams(tvParams);
            }
            //文案
            tv_text.setText(tabItem.text);
            if (textSize != -1) {
                tv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
            if (textColor != -1) {
                tv_text.setTextColor(textColor);
            } else if (textColorStateList != null) {
                tv_text.setTextColor(textColorStateList);
            }

            //数字
            if (!isShowNum) {
                tv_num.setVisibility(GONE);
            } else {
                tv_num.setVisibility(VISIBLE);
                tv_num.setTextSize(numTextSize);
                if (numColor != -1) {
                    tv_num.setTextColor(numColor);
                }
                if (numColorStateList != null) {
                    tv_num.setTextColor(numColorStateList);
                }
                tv_num.setText(String.valueOf(tabItem.num));
            }
            addView(view);
        }
//        setCurrentTab(0);
    }

    private void click(View view, int position) {
        final int tempPosition = position;
        final View tempView = view;
        view.setClickable(true);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentTab(tempPosition);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mAdapter, tempPosition, tempView);
                }
            }
        });
    }


    private class MyGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float downX = e.getX();
            if (getChildCount() > 0) {
                ArrayList<View> viewList = mIndicatorViews.mViewList;
                int size = viewList.size();
                int index = 0;
                while (index < size) {
                    View view = viewList.get(index);
                    if (downX > view.getLeft() && downX < view.getRight()) {
                        setCurrentTab(index);
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(mAdapter, index, view);
                        }
                        break;
                    }
                    index++;
                }
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy((int) (distanceX), 0);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > mMinimumVelocity) {
                fling((int) -velocityX);
            }
            return true;
        }
    }

    protected int px2dp(float pxValue) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    public interface BaseAdapter {
        int getItemCount();

        View getItemView(int position, ViewGroup parent);
    }


    private class IndicatorView {
        private ArrayList<View> mViewList = new ArrayList<>();
        private int mLineWidth;
        private ArrayList<Integer> mChildViewWidths = new ArrayList<>();

        public void addView(View childView) {
            if (!mViewList.contains(childView)) {
                mViewList.add(childView);
                int childWidth = childView.getMeasuredWidth();
                mChildViewWidths.add(childWidth);
                if (mViewList.size() == 1) {
                    mLineWidth = childWidth;
                } else {
                    mLineWidth = mLineWidth + mHorizontalSpec + childWidth;
                }
            }
        }

        public List<View> getViewList() {
            return mViewList;
        }

        public int getLineWidth() {
            return mLineWidth;
        }

        public int getChildWidth(int position) {
            return mChildViewWidths.get(position);
        }

        public View getChildView(int position) {
            return mViewList.get(position);
        }

        public void clearViews() {
            mViewList.clear();
            mChildViewWidths.clear();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BaseAdapter adapter, int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }
}
