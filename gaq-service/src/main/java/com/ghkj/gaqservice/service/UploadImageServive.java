package com.ghkj.gaqservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName : UploadImageServive
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/12/2 16:29
 */
public interface UploadImageServive {
    Map<String, Object> uploadImage(List<MultipartFile> fileList);
}
