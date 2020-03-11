package com.ghkj.gaqcommons.untils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @version 1.0
 * @ClassName : DownloadUtils
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2020/1/8 14:09
 */

/**
 *  * 下载 工具类
 *  * 
 *  * @author sun
 *  
 */
public class DownloadUtils {

    public static void main(String[] args) {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        FileOutputStream out = null;
        try
        {
//            File file0=new File("D:\\test111");
//            if(!file0.isDirectory()&&!file0.exists()){
//                file0.mkdirs();
//            }
//            out = new FileOutputStream(file0+"\\2.jpg");
            // 建立链接
            URL httpUrl=new URL("http://172.16.6.111/dfs/group1/M00/01/19/rBAGb1tYWC-ALvf4AAErJEekP8U334.jpg");
            conn=(HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream=conn.getInputStream();
            bis = new BufferedInputStream(inputStream);
            byte b [] = new byte[1024];
            int len = 0;
            while((len=bis.read(b))!=-1){
                out.write(b, 0, len);
            }
            System.out.println("下载完成...");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(out!=null){
                    out.close();
                }
                if(bis!=null){
                    bis.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }
}

