package com.ghkj.gaqservice.service.impl;

import com.ghkj.gaqcommons.untils.DateTimeUtil;
import com.ghkj.gaqservice.service.UploadImageServive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @version 1.0
 * @ClassName : UploadImageServiceImpl
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/12/2 16:30
 *
 */
@Service
public class UploadImageServiceImpl implements UploadImageServive {

    private static  final Logger logger= LoggerFactory.getLogger(UploadImageServiceImpl.class);

    @Value("${uploadImages}")
    private String uploadImages;
    @Value("${elasticIp}")
    private String elasticIp;
    @Value("${server.port}")
    private String port;

    @Override
    public Map<String, Object> uploadImage(List<MultipartFile> fileList) {
        Map<String,Object> map=new HashMap<>();
        if(fileList.size()>0){
            for (int i=0;i<fileList.size();i++){
                logger.info("====="+fileList.size());
                File fileDir=new File(uploadImages);
                fileDir.mkdirs();
                String fileName=fileList.get(i).getOriginalFilename();
                //获取文件名后缀名
                String suffix=fileList.get(i).getOriginalFilename();
                logger.info("suffix==="+suffix);
                String prefix=suffix.substring(suffix.lastIndexOf(".")+1);
                logger.info("prefix===="+prefix);
                if("jpg".equals(prefix) || "jpeg".equals(prefix) || "gif".equals(prefix)){
                    //给图片重新命名
                    Random random=new Random();
                    Integer randomFileName=random.nextInt(1000);
                    logger.info("randomFileName======"+randomFileName);
                    fileName= DateTimeUtil.NowTime()+randomFileName+"."+prefix;
                    logger.info("fileName========="+fileName);
                }
                File saveFile=new File(uploadImages,fileName);
                try {
                    fileList.get(i).transferTo(saveFile);
                }catch (Exception e){
                    logger.error("错误==="+e.getLocalizedMessage());
                    e.printStackTrace();
                }
                map.put("msg","上传成功");
                map.put("state","200");
                map.put("path","http://"+elasticIp+":"+port+"/img/"+fileName);
                map.put("fileName",fileName);
            }
        }
        return map;
    }
}
