package com.alibaba.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/3/7 15:01
 * @Desc:
 */
public class TestTmp {
    public static void main(String[] args) {
        //List<String> list1 = Arrays.asList("15000300600000|44,15000300360000|141,10000200140000|8".split(","));
        //List<String> list2 = Arrays.asList("15000300600000|44,10000200140000|8,15000300360000|141".split(","));


        //List<String> list1 = Arrays.asList("{\\\"0-4\\\":\\\"5\\\",\\\"7-9\\\":\\\"8\\\"}".replace("{","").replace("}","") .split(","));
        //List<String> list2 = Arrays.asList("{\\\"7-9\\\":\\\"8\\\",\\\"0-4\\\":\\\"5\\\"}".replace("{","").replace("}","") .split(","));


        //boolean b = list1.containsAll(list2);

        HashMap<String,  Integer> map = new HashMap<>();
        map.put("age",28);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("wo","fada");
        jsonObject.put("ni","xifu");
        String s = JSONObject.toJSONString(map);
        jsonObject.put("detail", JSON.parseObject(s));

        String age = jsonObject.getJSONObject("detail").getString("age");
        System.out.println(age);

    }
}
