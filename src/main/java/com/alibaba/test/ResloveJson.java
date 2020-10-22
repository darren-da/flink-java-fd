package com.alibaba.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author fada.yu
 * @date 2020/10/22 9:44
 * @Description：
 */
public class ResloveJson {
    public static void main(String[] args) {

       String jsonStr="{\"name\":\"胡小威\" , \"age\":20 , \"male\":true}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);
//        System.out.println(jsonObject.getString("name"));
        JSONObject jsonObject1 = new JSONObject(jsonObject);
//        System.out.println(jsonObject1.getString("name"));





    }
}
