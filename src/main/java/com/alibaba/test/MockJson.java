package com.alibaba.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author :YuFada
 * @date： 2021/3/22 0022 上午 9:45
 * Description：
 */
public class MockJson {
    private Logger logger = LoggerFactory.getLogger(MockJson.class);

    @Test
    public  void mock() {
        String tags1[] = {"211",
                "985工程",
                "双一流",
                "世界名校",
                "世界一流大学建设高校A类",
                "985"};

        List<String> tags11 = Arrays.asList(tags1);

        String tags2[] = {"211",
                "985平台",
                "双一流",
                "世界名校",
                "世界一流学科建设高校"};

        List<String> tags12 = Arrays.asList(tags2);

        HashMap<String, String> industryMap1= new HashMap<>();
        industryMap1.put("indu1","12443");
        industryMap1.put("indu2","23492");
        industryMap1.put("indu3","24378");
        JSONObject jsonIndu1 = JSONObject.parseObject(JSONObject.toJSONString(industryMap1));
        HashMap<String, String> industryMap2= new HashMap<>();
        industryMap2.put("indu1","12443");
        industryMap2.put("indu2","23492");
        industryMap2.put("indu3","24378");
        JSONObject jsonIndu2 = JSONObject.parseObject(JSONObject.toJSONString(industryMap2));


        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("universityName","北京科技大学");

        map1.put("universityTag",tags11);
        map1.put("industry",jsonIndu1);
        JSONObject jsonObject1 = new JSONObject(map1);


        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("masterSchoolName","北京航空航天大学");
        map2.put("masterSchoolTag",tags12);
        map2.put("industry",jsonIndu2);
        JSONObject jsonObject2 = new JSONObject(map2);


        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0,jsonObject1);
        jsonArray.add(1,jsonObject2);

        System.out.println(jsonArray);
        for (Object s:
             jsonArray) {

            JSONObject jsonObject = JSONObject.parseObject(s.toString());
            System.out.println(jsonObject.getJSONObject("industry"));
        }




    }




}
