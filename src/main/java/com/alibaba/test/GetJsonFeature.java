package com.alibaba.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

/**
 * @author fada.yu
 * @date 2020/10/28 13:31
 * @Description：
 */
public class GetJsonFeature {
//    private static final String URL = "*********";
    private static final String URL = "http://d-feature-service.***.com/manager/loadKeyMapping?useCache=false";

    /**
     * 通过网络访问json并读取文件
     *
     * @param url:http://127.0.0.1:80/dashboard/dept_uuid.json
     * @return:json文件的内容
     */
    public static String loadJson(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 通过本地文件访问json并读取
     *
     * @param path：E:/svn/05.Hospital/templatedept_uuid.json
     * @return：json文件的内容
     */
    public static String ReadFile(String path) {
        String laststr = "";
        File file = new File(path);// 打开文件
        BufferedReader reader = null;
        try {
            FileInputStream in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));// 读取文件
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr = laststr + tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException el) {
                }
            }
        }
        return laststr;
    }


    public static void main(String[] args) {
        String featureJson = loadJson(URL);
        JSONObject jsonObject = JSON.parseObject(featureJson).getJSONObject("data");

        Set<String> features = jsonObject.keySet();

        System.out.println("现所有feature个数：" + features.size());
        for (String feature : features) {
//            System.out.println("feature:"+feature+"\t"+"desc:"+jsonObject.getString(feature));
            if (feature.startsWith("user") || feature.startsWith("jd") || feature.startsWith("cv") ||
                    feature.startsWith("relation")) {
                System.out.println(feature);
            }


        }


    }
}
