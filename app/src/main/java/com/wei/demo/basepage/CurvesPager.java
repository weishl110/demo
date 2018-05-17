package com.wei.demo.basepage;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wei.demo.R;
import com.wei.demo.adapter.MyRecyclerAdapter;
import com.wei.demo.anim.MyAnimation;
import com.wei.demo.bean.ColumnBean;
import com.wei.demo.bean.TabItem;
import com.wei.demo.factory.DialogFactory;
import com.wei.demo.service.MyAccessibiliService;
import com.wei.demo.ui.SencondActivity;
import com.wei.demo.ui.TestActivity;
import com.wei.demo.view.AutoTabLayout;
import com.wei.demo.view.BouncingMenu;
import com.wei.demo.view.TimeSharingView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/1.
 */
public class CurvesPager extends BasePager {

    private static final String TAG = "debug_CurvesPager";
    private TimeSharingView timesharing_view;
    private BouncingMenu bouncingMenu;
    private AutoTabLayout indicator;
    private EditText edittext;
    private List<TabItem> mTabItems;
    private MyAdapter adapter;
    private ImageView iv_anim;
    private MyAnimation myAnimation;

    public CurvesPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(weak.get(), R.layout.layout_curvesview, null);
        edittext = (EditText) view.findViewById(R.id.edit_text);
        view.findViewById(R.id.updata).setOnClickListener(this);
        timesharing_view = (TimeSharingView) view.findViewById(R.id.timesharing_view);
        view.findViewById(R.id.tv_get).setOnClickListener(this);
        view.findViewById(R.id.tv_dialog).setOnClickListener(this);
        view.findViewById(R.id.tv_jump).setOnClickListener(this);
        view.findViewById(R.id.tv_setting).setOnClickListener(this);
        iv_anim = (ImageView) view.findViewById(R.id.iv_anim);
        indicator = (AutoTabLayout) view.findViewById(R.id.indicatorview);
        return view;
    }

    @Override
    public void initData() {
        //创建假数据
        h = 9;
        m = 25;
        ArrayList<ColumnBean> list = getData();
        timesharing_view.setData(list);

//        List<TabItem> itemList = initItems(3);
//        indicator.setItems(itemList);
        if (mTabItems == null) {
            mTabItems = new ArrayList<>();
        }
        adapter = new MyAdapter(mTabItems);
        indicator.setAdapter(adapter);

        indicator.setOnItemClickListener(new AutoTabLayout.OnItemClickListener() {
            @Override
            public void onItemClick(AutoTabLayout.BaseAdapter adapter, int position, View view) {

            }
        });
    }

    private class MyAdapter implements AutoTabLayout.BaseAdapter {

        private List<TabItem> list;

        public MyAdapter(List<TabItem> list) {
            this.list = list;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public View getItemView(int position, ViewGroup parent) {
            View view = View.inflate(parent.getContext(), R.layout.layout_indicator, null);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
            TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
            TabItem tabItem = list.get(position);
            tv_text.setText(tabItem.text);
            tv_num.setVisibility(View.GONE);
            iv_icon.setImageResource(tabItem.imgRes);
            return view;
        }
    }

    private List<TabItem> initItems(int size) {
        ArrayList<TabItem> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TabItem tabItem = new TabItem();
            tabItem.text = "dd-" + i;
            tabItem.imgRes = R.mipmap.my_center;
            tabItem.num = 10;
            list.add(tabItem);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_get:
                Context context = weak.get();
                Intent intent = new Intent();
                intent.putExtra("className", TestActivity.class.getName());
                intent.setClass(context, SencondActivity.class);
                intent.putExtra("test", 1);
                intent.putExtra("isStart", true);
                context.startActivity(intent);
//                initData();
                break;
            case R.id.tv_dialog:
                ArrayList<String> items = new ArrayList<String>();
                for (int i = 0; i < 12; i++) {
                    items.add("2016年" + (i + 1) + "月");
                }
                if (DialogFactory.isShow()) {
                    DialogFactory.dismiss();
                }
                DialogFactory.createDialog(weak.get(), items, false);
                DialogFactory.show();
                break;
            case R.id.tv_jump:
                if (bouncingMenu != null && bouncingMenu.isShow()) {
                    bouncingMenu.dismiss();
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < 40; i++) {
                        list.add("item=====" + i);
                    }
                    MyRecyclerAdapter adapter = new MyRecyclerAdapter(list);
                    bouncingMenu = BouncingMenu.makeMenu(timesharing_view, R.layout.layout_ru_sweet, adapter).show();
                }
                break;
            case R.id.tv_setting:
//                iv_anim.setRotationX(180);
                myAnimation = new MyAnimation(360);
                myAnimation.setFillAfter(true);
                iv_anim.startAnimation(myAnimation);
//                ObjectAnimator.ofFloat(iv_anim, "rotationY", 0, 360).setDuration(1500).start();
//                animator.setDuration(1500);
//                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        android.util.Log.e(TAG, "onAnimationUpdate: animatorValue = " + animation.getAnimatedValue());
//                    }
//                });
//                animator.start();
//                weak.get().startService(new Intent(weak.get(), MyAccessibiliService.class));
//                boolean accessibilitySettingOn = isAccessibilitySettingOn(weak.get());
//                Log.e(TAG, "88行...onClick:  = " + accessibilitySettingOn);
//                if (!accessibilitySettingOn) {
//                    Intent in = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//                    weak.get().startActivity(in);
//                } else {
//                    Toast.makeText(weak.get(), "已经开启服务", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.updata:
//                String text = edittext.getText().toString();
//                android.util.Log.e(TAG, "onClick:  = " + (text.matches("[0-9]{1,}")));
//                if (text.matches("[0-9]{1,}")) {
//                    int num = Integer.parseInt(text);
//                    List<TabItem> tabItems = initItems(num);
//                    mTabItems.clear();
//                    mTabItems.addAll(tabItems);
//                    indicator.notifyAdapter();
////                    indicator.setItems(tabItems);
//                }
                break;
        }
    }

    private boolean isAccessibilitySettingOn(Context context) {
        int accessibilityEnable = 0;
        String service = context.getPackageName() + "/" + MyAccessibiliService.class.getCanonicalName();
        Log.e(TAG, "92行...isAccessibilitySettingOn: service = " + service);
        try {
            accessibilityEnable = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.e(TAG, "96行...isAccessibilitySettingOn: accessibilityEnable = " + accessibilityEnable);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        TextUtils.SimpleStringSplitter mStringComlonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnable == 1) {
            String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringComlonSplitter.setString(settingValue);
                while (mStringComlonSplitter.hasNext()) {
                    String next = mStringComlonSplitter.next();
                    Log.e(TAG, "109行...isAccessibilitySettingOn: next = " + next);
                    if (next.equalsIgnoreCase(service)) {
                        Log.e(TAG, "111行...isAccessibilitySettingOn: true = ");
                    }
                }
            }
        }
        return false;
    }

    //创建假数据
    int h = 9;
    int m = 25;

    public ArrayList<ColumnBean> getData() {
        ArrayList<ColumnBean> list = new ArrayList<ColumnBean>();
        for (int i = 0; i < 49; i++) {
            ColumnBean columnBean = new ColumnBean();
            float value = (float) (Math.random() * 10000);
            float netValue = (float) (Math.random() * 2);
            value = getDecimal(value);
            columnBean.setValue(value);
            columnBean.setNetValue(get4Decimal(netValue));
            m += 5;
            String mm = m >= 60 ? "00" : String.valueOf(m < 10 ? ("0" + m) : m);
            if (m >= 60) {
                h++;
                m = 0;
            }
            if (h == 11 && m > 30) {
                h = 13;
                m = 5;
                mm = "05";
            }
            columnBean.setDate((h < 10 ? ("0" + h) : h) + ":" + mm);
            list.add(columnBean);
        }
//        for (int i = 0; i < list.size(); i++) {
//            Log.e(TAG, "getData: value = " + list.get(i).toString());
//        }
        return list;

    }

    private float getDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return Float.parseFloat(decimalFormat.format(value));
    }

    private float get4Decimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.0000");
        return Float.parseFloat(decimalFormat.format(value));
    }

    //快速排序
    public void sort(int arr[], int low, int high) {
        int l = low;
        int h = high;
        int povit = arr[low];

        while (l < h) {
            //从后往前排
            while (l < h && arr[h] >= povit)
                h--;
            if (l < h) {
                int temp = arr[h];
                arr[h] = arr[l];
                arr[l] = temp;
                l++;
            }

            while (l < h && arr[l] <= povit)
                l++;

            if (l < h) {
                int temp = arr[h];
                arr[h] = arr[l];
                arr[l] = temp;
                h--;
            }
        }
        if (l > low) sort(arr, low, l - 1);
        if (h < high) sort(arr, l + 1, high);
    }
}
