package com.ghkj.gaqservice.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @version 1.0
 * @ClassName : ZipDealService
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/12/6 12:40
 */
public interface ZipDealService {
    /**
     * 上传压缩包，里边是.target文件
     * @param files
     * @return
     */
    JSONObject importEsByXml(List<MultipartFile> files)throws Exception;
}
