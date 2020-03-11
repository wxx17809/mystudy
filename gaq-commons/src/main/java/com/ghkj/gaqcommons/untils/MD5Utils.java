package com.ghkj.gaqcommons.untils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version 1.0
 * @ClassName : MD5Utils
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/11/15 15:23
 */
public class MD5Utils {

    private static  final Logger logger= LoggerFactory.getLogger(MD5Utils.class);

    public static String stringToMD5(String credentials) {
        Object obj = new SimpleHash("MD5", credentials, null, 2);
        return obj.toString();
    }


    public static void main(String[] args) {
        String pwd = stringToMD5("123456");
        logger.info("Md5加密2次后的结果====="+pwd);
    }

}

