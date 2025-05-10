package com.example.demo.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class CutPicture {


    public static String jietuguangwang() {
        try {
            // 参数
            String token = "6509040ca6634";
            String url = URLEncoder.encode("http://gdc1.cn/system/guanwang");
            int width = 1080;
            int height = 600;
            int full_page = 1;
            String zone = "hk";
            String device = "pc";
            int fresh = 1;
            // 构造URL
            String query = "https://www.screenshotmaster.com/api/v1/screenshot";
            query += String.format("?token=%s&url=%s&width=%d&height=%d&full_page=%s&zone=%s&device=%s&fresh=%d",
                    token, url, width, height, full_page,zone,device,fresh);
            String rel = HttpUtils.sendGet(query);
            if(null == rel){
                System.out.println("推送失败，重试第一次！");
                rel = HttpUtils.sendGet(query);
                if(null == rel){
                    System.out.println("推送失败，重试第二次！");
                    rel = HttpUtils.sendGet(query);
                }
            }
            System.out.println(rel);
            if(null == rel){
                System.out.println("图片获取失败，手动查库推送");
                return "开奖号码：" +MySqlConnection.chaxunkaijianghaoma();
            }else{
                JSONObject jsonObject = (JSONObject) JSON.parseObject(rel).get("data");
                return dowbendi(jsonObject.get("url").toString());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String jietujihua() {
        try {
            // 参数
            String token = "6509040ca6634";
            String url = URLEncoder.encode("http://gdc1.cn/system/jihuayidong");
            int width = 400;
            int height = 700;
            int full_page = 1;
            String zone = "hk";
            String device = "mobile";
            int fresh = 1;
            // 构造URL
            String query = "https://www.screenshotmaster.com/api/v1/screenshot";
            query += String.format("?token=%s&url=%s&width=%d&height=%d&full_page=%s&zone=%s&device=%s&fresh=%d",
                    token, url, width, height, full_page,zone,device,fresh);
            String rel = HttpUtils.sendGet(query);
            if(null == rel){
                System.out.println("推送失败，重试第一次！");
                rel = HttpUtils.sendGet(query);
                if(null == rel){
                    System.out.println("推送失败，重试第二次！");
                    rel = HttpUtils.sendGet(query);
                }
            }
            System.out.println(rel);
            if(null == rel){
                System.out.println("图片获取失败，手动查库推送");
                return "推送计划：" + MySqlConnection.chaxunjihuahaoma();
            }else{
                JSONObject jsonObject = (JSONObject) JSON.parseObject(rel).get("data");
                return dowbendi(jsonObject.get("url").toString());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static String dowbendi(String serverUrl) {
        String savePath = "/home/"+System.currentTimeMillis() + ".png";
        try {
            // 打开连接
            URL url = new URL(serverUrl);
            URLConnection connection = url.openConnection();
            // 设置请求超时为5秒
            connection.setConnectTimeout(5 * 1000);
            // 读取数据流并保存到本地
            InputStream input = connection.getInputStream();
            byte[] data = new byte[1024];
            int len;
            FileOutputStream output = new FileOutputStream(savePath);
            while ((len = input.read(data)) != -1) {
                output.write(data, 0, len);
            }
            output.close();
            input.close();
            System.out.println("图片保存成功：" + savePath);
            return savePath;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("图片保存失败：" + e.getMessage());
        }
        return null;
    }
}
