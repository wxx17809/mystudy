package com.ghkj.gaqcommons.untils;

import com.alibaba.fastjson.JSONObject;

/**
 * @description: 生成json数据的工具
 * @creatTime: 2017/1/25 13:55
 * @author: 吴璇璇
 */

public class JSONDataUtil {

    /**
     * @Description: 生成String
     * @Author: 吴璇璇
     * @CreatTime: 2017/1/25 14:05
     * @Param: msg 简短消息内容   status   状态码    data   数据对象
     * @Return: String
     */
    public static String generateJSONString(String msg, int status, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("status", status);
        jsonObject.put("data", data);
        return jsonObject.toString();
    }

    /**
     * @Description: 生成JSONObject
     * @Author: 吴璇璇
     * @CreatTime: 2017/1/25 14:05
     * @Param: msg 简短消息内容   status   状态码    data   数据对象
     * @Return: json对象
     */
    public static JSONObject generateJSONObject(String msg, int status, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("status", status);
        jsonObject.put("data", data);
        return jsonObject;
    }

}
