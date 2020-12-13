package com.alibaba.test;

/**
 * @author fada.yu
 * @date 2020/10/22 9:42
 * @Description：
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
/**
 *
 * @author derek
 */
public class GetJsonArray {
    public static void main(String[] args){
        JSONObject obj=new JSONObject();
        obj.put("derek","23");
        obj.put("dad", "49");
        obj.put("mom", "45");
        System.out.println("通过构造器的方式创建的JSONObject对象："+obj);

        Map<String,String> map=new HashMap<>();
        map.put("derek","23");
        map.put("dad", "49");
        map.put("mom", "45");
        System.out.println("通过fromObject方法将map对象转换为JSONObject对象："+JSONObject.toJSONString(map));

        JSONArray arr=new JSONArray();
        arr.add(0,"derek");
        arr.add(1,"dad");
        arr.add(2,"mom");
        System.out.println("通过构造器的方式创建的JSONArray："+arr);

        ArrayList<String> list=new ArrayList<>();
        list.add("derek");
        list.add("dad");
        list.add("mom");
//        System.out.println("通过fromObject方法将Arraylist对象转换为JSONArray对象："+JSONArray.parseArray(String.valueOf(list)));

        System.out.println("将HashMap对象通过fromObject方法转换为JSONArray对象"+JSONObject.toJSONString(map));

        String str="{\"derek\":23,\"dad\":49,\"mom\":45}";
        System.out.println("解析之后的JSON对象："+JSONObject.parse(str));

        //遍历输出
        JSONObject jsonObject = JSON.parseObject(String.valueOf(obj));
        Iterator<String> it = jsonObject.keySet().iterator();
        while(it.hasNext()){
            String key=it.next();
            System.out.println(key+":"+obj.getString(key));
        }
    }

}
