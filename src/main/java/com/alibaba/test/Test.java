package com.alibaba.test;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/3/23 15:10
 * @Desc:
 */
public class Test {

    @org.junit.Test
    public void Test1() {
        String s = "{\"eventType\":\"USER_BLACK\",\"userId\":\"1127303296\"}";
        //String s="{\"eventType\":\"USER_BLACK\"}";
        JSONObject jsonObject = JSONObject.parseObject(s);
        String flag = jsonObject.getLong("userId") != null ? "1" : "0";

        if (!flag.equals("0")) {
            System.out.println("ok");
        }
    }

    @org.junit.Test
    public void bubbleSort() {
        int[] arr = {8, 4, 5, 1, 6, 2, 9, 7, 3};
        for (int i = 0; i < arr.length; i++) {
            boolean flag = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = tmp;
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
        }
        System.out.print(Arrays.toString(arr));
    }

    @org.junit.Test
    public void testBinarySearch(){
        System.out.println(binarySearch());
    }

    public int binarySearch() {
        int[] nums = {8, 4, 5, 1, 6, 2, 9, 7, 3};
        int target = 9;
        int left = 0;
        int right = nums.length - 1; // 注意

        while (left <= right) { // 注意
            int mid = (right + left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1; // 注意
            } else if (nums[mid] > target) {
                right = mid - 1; // 注意
            }else {
                return -1;
            }
        }
        return  target;
    }

}
