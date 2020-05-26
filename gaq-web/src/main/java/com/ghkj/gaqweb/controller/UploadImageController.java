package com.ghkj.gaqweb.controller;

import com.ghkj.gaqcommons.untils.Ftp;
import com.ghkj.gaqcommons.untils.SSHUtils;
import com.ghkj.gaqservice.service.UploadImageServive;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.*;

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

    //用FTP上传
    @ApiOperation(value = "上传和执行命令")
    @PostMapping("/install")
    public Map<String,Object> install() {
        Map<String,Object> map=new HashMap<>();
        try {
            //ftp上传文件先连接，在上传。第一个参数是要上传的目录，第二个参数是本地目录下的图片
            Ftp ftp = Ftp.getSftpUtil("106.13.229.155", 22, "root", "Aa19980112z!");
            ftp.upload("/root", "C:\\Users\\Administrator\\Desktop\\qianduan\\5.jpg");
            ftp.delete("/root","5.jpg");
            //执行linux命令
            SSHUtils sshUtils=SSHUtils.getSSHUtils("106.13.229.155", 22, "root", "Aa19980112z!");
            sshUtils.execCommandByShell("cd /root && ls");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SSHUtils.release();
            Ftp.release();
        }
        return map;
    }



}
