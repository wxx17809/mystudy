package com.ghkj.gaqweb.controller;

import com.ghkj.gaqdao.utils.EncryUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @version 1.0
 * @ClassName : HellowordController
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/9/2 11:15
 */
@Api(description = "测试项目启动")
@RestController
public class HellowordController {

    private static final Logger logger = LoggerFactory.getLogger(HellowordController.class);

    @GetMapping("/hello")
    public String hello(){
        return "qqqq";
    }

    public static void main(String[] args) {
        //System.out.println("度转度分秒====="+D2Dms(119.66994));
        //System.out.println("度分秒转度===="+Dms2D(D2Dms(119.66994)));
        System.out.println("日期加密====="+desCode("2020-04-08"));
        System.out.println("日期解密====="+desDecode("fa81a01cf4f3f33dcb5631aacb2ddeae"));
    }


    //度→度分秒：
    public static String D2Dms(double d_data){
        int d = (int)d_data;
        int m = (int)((d_data-d)*60);
        double s =(((d_data-d)*60-m)*60);
        return  d+"°"+m+"′"+s+"″";
    }

    //度分秒-----转度
    public static double Dms2D(String dms_data) {
        if (!dms_data.contains("°")||!dms_data.contains("′")||!dms_data.contains("″"))
            return 0;
        double d = Double.parseDouble(dms_data.split("°")[0]);
        double m = Double.parseDouble(dms_data.split("°")[1].split("′")[0]);
        double s =Double.parseDouble(dms_data.split("°")[1].split("′")[1].replace("″", ""));
        return d+m/60+s/60/60;
    }


    //加密
    private static String desCode(String str) {
        //str为加密的截止日期
        String t = EncryUtil.encrypt(str);
        return t;
    }


    //解密
    private static Boolean desDecode(String str) {
        String t = "";
        //System.out.println("加密后：" + EncryUtil.encrypt(t));
        t = EncryUtil.decrypt(str);
        //System.out.println("解密后：" + t);
        if(t.equals("perpetual license")) {
            return true;
        }else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String nowDate = format.format(date);
            Integer result = EncryUtil.compareDate(t,nowDate);
            if(result == -1) {
                return false;
            }
        }

        return true;
    }










}
