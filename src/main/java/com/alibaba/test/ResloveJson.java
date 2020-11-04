package com.alibaba.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;

/**
 * @author fada.yu
 * @date 2020/10/22 9:44
 * @Description：
 */
public class ResloveJson {
    public static void main(String[] args) {

      /*  {
            "cluster_name":"21test",
                "hearth":"true",
                "nodes":{
            "abc":{
                "ip":"192.168.200.191",
                        "version":"2.1.1"
            },
            "def":{
                "ip":"192.168.200.191",
                        "version":"2.1.1"
            },
            "ghi":{
                "ip":"192.168.200.196",
                        "version":"2.1.1"
            }
        }
        }*/


        String jsonStr = "{\"cluster_name\":\"21test\",\"hearth\":\"true\",\"nodes\":{\"abc\":{\"ip\":\"192.168.200.191\",\"version\":\"2.1.1\"},\"def\":{\"ip\":\"192.168.200.191\",\"version\":\"2.1.1\"},\"ghi\":{\"ip\":\"192.168.200.196\",\"version\":\"2.1.1\"}}}";
        JSONObject jsonObj = JSONObject.parseObject(jsonStr);
        System.out.println(jsonObj.getString("cluster_name"));
        System.out.println(jsonObj.getString("nodes"));
        System.out.println(jsonObj.getJSONObject("nodes").size());
//        System.out.println(jsonObj.getJSONObject("nodes").isEmpty());
//        System.out.println(jsonObj.getJSONObject("nodes").getJSONObject("def"));
        System.out.println(jsonObj.getJSONObject("nodes").getString("def"));

        JSONObject nodes = jsonObj.getJSONObject("nodes");
        Iterator<String> it = nodes.keySet().iterator();

       while (it.hasNext()){
           String key = it.next();
           System.out.println(key + ":" + nodes.getJSONObject(key).getString("ip") + " " +"version:"+ nodes.getJSONObject(key).getString("version"));

       }

       //测试get JsonArray()
        String arrayJson = "{\"info\":[{\"goodsId\":\"1234\",\"goodsq\":\"10\"},{\"goodsId\":\"5678\",\"goodsq\":\"20\"}]}";
       testArray(arrayJson);
 /*
        {
          "info": [
            {
              "goodsId": "1234",
              "goodsq": "10"
            },
            {
              "goodsId": "5678",
              "goodsq": "20"
            }
          ]
        }
    */

//json数组

    }

    public static void testArray(String jsonStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONArray arrayJson = jsonObject.getJSONArray("info");
        for (int i = 0; i <arrayJson.size() ; i++) {
            JSONObject arrayMode = arrayJson.getJSONObject(i);
            System.out.println(arrayMode.getString("goodsId"));

        }

    }
}
