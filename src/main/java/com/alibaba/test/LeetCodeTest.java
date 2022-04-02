package com.alibaba.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/4/2 10:40
 * @Desc: LeetCode 测试用例
 */
public class LeetCodeTest {
    @Test
    public void towSum() {
        //nums = [2,7,11,15], target = 9
        int[] nums = {11, 7, 2, 15};
        int target = 9;
        int[] res = new int[2];
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (list.contains(nums[i])) {
                res[0] = list.indexOf(nums[i]);
                res[1] = i;
            }
            list.add(target - nums[i]);
        }
        System.out.println(Arrays.toString(res));

    }

    /**
     * 交替字符串 定义为：如果字符串中不存在相邻两个字符相等的情况，那么该字符串就是交替字符串。例如，字符串 "010" 是交替字符串，而字符串 "0100" 不是。
     * <p>
     * 返回使 s 变成 交替字符串 所需的 最少 操作数。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/minimum-changes-to-make-alternating-binary-string
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */
    @Test
    public void jiaohuanerjinzhizifuchuang() {
        String s = "1111";
        int startZero = 0;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            if (ch == ((i & 1) == 0 ? '1' : '0')) {
                startZero++;
            }
        }
        System.out.println(Math.min(len - startZero, startZero));
        System.out.println(0 | 0);
        System.out.println(1 & 1);
    }
}



