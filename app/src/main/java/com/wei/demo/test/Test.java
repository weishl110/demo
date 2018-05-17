package com.wei.demo.test;

/**
 * Created by ${wei} on 2017/9/5.
 */

public class Test {


    public static void main(String[] args) {
//        int[] arr1 = {10, 2, 4, 29, 18, 17, 12, 61, 64, 7, 8, 43, 32, 19, 76, 29, 1};
//        quickSort(arr1, 0, arr1.length - 1);
//        test(arr1);

        String str = ChildPerson.str;
        System.out.println("str = " + str);
        Person p = new Person();
        p.setName("zhangsan");
        p.setAge(12);
        System.out.println("p " + p.getName());
    }

    private static void test(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            System.out.print(arr[i] + ",");
        }
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low > high) return;

        int l = low, h = high, key = arr[low];

        while (l < h) {
            while (l < h && arr[h] >= key) {//从后往前排
                h--;
            }
            swap(arr, l, h);

            while (l < h && arr[l] <= key)//从前往后排
                l++;
            swap(arr, l, h);
        }


        quickSort(arr, low, h - 1);
        quickSort(arr, l + 1, high);


    }

    private static void swap(int[] arr, int l, int h) {
        int temp = arr[l];
        arr[l] = arr[h];
        arr[h] = temp;
    }
}
