package com.ghkj.gaqweb.controller;


import com.ghkj.gaqcommons.untils.TokenUtils;
import com.ghkj.gaqservice.service.ZipDealService;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Encoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @version 1.0
 * @ClassName : ZipDealController
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/12/6 11:58
 */
@RestController
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
        int result = TokenUtils.ValidToken(token);
        logger.info("=========" + result);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        if (result == 0) {
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
        }
        return jsonObject;
    }




}
