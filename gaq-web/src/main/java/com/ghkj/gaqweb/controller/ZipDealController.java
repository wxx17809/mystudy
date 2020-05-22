package com.ghkj.gaqweb.controller;


import com.ghkj.gaqcommons.untils.ZIPUtil;
import com.ghkj.gaqservice.service.ZipDealService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * @version 1.0
 * @ClassName : ZipDealController
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/12/6 11:58
 */
@RestController
@RequestMapping("/hello")
public class ZipDealController {
    private static final Logger logger = LoggerFactory.getLogger(ZipDealController.class);

    @Autowired
    private ZipDealService zipDealService;

    /**
     * 上传target文件压缩包
     * 吴璇璇
     * 2019.10.28
     * @return
     * @throws Exception
     */
    @PostMapping("/importEsByXml")
    public com.alibaba.fastjson.JSONObject importEsByXml(HttpServletRequest request) throws Exception {
        String token=request.getParameter("token");
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            logger.info("进入导入数据的方法====");
            try {
                MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
                List<MultipartFile> files = params.getFiles("file");
                jsonObject = zipDealService.importEsByXml(files);
                logger.info("进入图片处理=====");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("导入失败==" + e.getLocalizedMessage());
                logger.info("可能原因:服务器未启动，数据库没连接，用户名密码错误，数据库里没hjj索引");
            }
            jsonObject.put("state", 200);
        return jsonObject;
    }


    /**
     * 这个deomo入参的类型是MultipartFile，很多网上的例子是File类型
     * @return
     * @throws Exception
     */
    @PostMapping("/addPersonsFileOfZip")
    public JSONObject addPersonsFileOfZip(HttpServletRequest request) throws Exception {
        JSONObject jsonObject=new JSONObject();
        try {
            MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
            List<MultipartFile> files = params.getFiles("file");
            MultipartFile file=files.get(0);
            String name=file.getOriginalFilename();
            //把接收的压缩包存到D://test 文件夹下
            FileInputStream input=(FileInputStream) file.getInputStream();
            OutputStream outputStream=new FileOutputStream("D://test//"+name);
            byte[] but=new byte[8 * 1024];
            int s;
            while ((s=input.read(but,0,but.length))!=-1){
                outputStream.write(but);
                outputStream.flush();
            }
            //解压存放的压缩包
            ZIPUtil.decompressZip("D://test//"+name,"D://test//");
            //List<List<String>> aa=ExcelReader.readXlsx("D://test//aa//aa.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导入失败==" + e.getLocalizedMessage());
            logger.info("可能原因:服务器未启动，数据库没连接，用户名密码错误，数据库里没hjj索引");
        }

        return jsonObject;

    }
}
