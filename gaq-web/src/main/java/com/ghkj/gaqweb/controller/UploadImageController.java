package com.ghkj.gaqweb.controller;

import com.ghkj.gaqservice.service.UploadImageServive;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName : UploadImageController
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/11/27 15:07
 */
@Api(description = "上传图片")
@RestController
public class UploadImageController {

    private static final Logger logger = LoggerFactory.getLogger(UploadImageController.class);

    @Autowired
    private UploadImageServive uploadImageServive;

    @Value("${uploadImages}")
    private String uploadImage;

    /**
     * 上传图片
     * @param request
     * @return
     */
    @PostMapping("/uploadPic")
    public Map<String, Object> uploadPic(HttpServletRequest request) {
        logger.info("进入上传图片的方法======");
        Map<String, Object> map = new HashMap<>();
        MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;
        List<MultipartFile> fileList = params.getFiles("files");
        map = uploadImageServive.uploadImage(fileList);
        return map;
    }

    /**
     *
     * @Description: 文件下载
     */
    @RequestMapping("downFile")
    public String downFile(String token,String fileName,HttpServletResponse response) {
        try {
                File path = new File(uploadImage);
                logger.info("路径=====" + path.getAbsolutePath());
                File file = new File(path.getAbsolutePath()+"/" + fileName);
                response.reset();
                downloadZip(file, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "下载成功";
    }


    public static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
        try {
            //以流的形式下载文件
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            //清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                File f = new File(file.getPath());
                f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
