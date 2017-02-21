package com.wei.demo.utils;

import android.content.Context;

import com.wei.demo.bean.ColumnBean;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ${wei} on 2017/1/11.
 */

public class StringUtil {

    /**
     * 获取最大值的索引 如果没有数据返回-1
     */
    public static int getMaxValueIndex(ArrayList<ColumnBean> list, Context context) {
        if (list == null || list.size() == 0) {
            return -1;
        }
        int size = list.size();
        double indexValue = Math.abs(list.get(0).getValue());
        int index = 0;
        for (int i = 1; i < size; i++) {
            double tempValue = Math.abs(list.get(i).getValue());
            if (tempValue > indexValue) {
                indexValue = tempValue;
                index = i;
            }
        }
        return index;
    }

    /**
     * 根据中间值获取如果没有数据返回-1
     *
     * @param list     数据集合
     * @param midValue 中间值
     */
    public static int getMaxIndex(ArrayList<ColumnBean> list, Context context, double midValue) {
        if (list == null || list.size() == 0) {
            return -1;
        }
        int size = list.size();
        float indexValue = (float) Math.abs(list.get(0).getNetValue() - midValue);
        int index = 0;

        for (int i = 1; i < size; i++) {
            float tempValue = (float) Math.abs(list.get(i).getNetValue() - midValue);
            if (tempValue > indexValue) {
                indexValue = tempValue;
                index = i;
            }
        }
        return index;
    }


    /**
     * 保留两位小数
     * @param value
     * @return
     */
    public  static float getDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return Float.parseFloat(decimalFormat.format(value));
    }
}
