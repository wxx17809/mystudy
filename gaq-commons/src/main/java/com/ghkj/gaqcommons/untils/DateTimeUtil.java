package com.ghkj.gaqcommons.untils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0
 * @ClassName : DateTimeUtil
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/12/2 13:27
 */
public class DateTimeUtil {

    public static  String NowTime(){

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date date=new Date();
        return sdf.format(date);
    }


    /**
     * 失效时间 当前时间+10min
     * @return
     */
    public static String expriredDate(){
        Date now = new Date();
        Date now_10 = new Date(now.getTime() + 600000); //1分钟=60秒，10*60=600秒  1秒=1000毫秒  所以:600*1000=600000
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyyMMddHHmmss");
        String nowTime_10 = dateFormat.format(now_10);
        return nowTime_10;
    }

}
