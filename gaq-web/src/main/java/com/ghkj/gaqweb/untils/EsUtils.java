package com.ghkj.gaqweb.untils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;
import java.io.*;


/**
 * @version 1.0
 * @ClassName : EsUtils
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/9/4 15:37
 */
public class EsUtils {

    private static  final Logger logger= LoggerFactory.getLogger(EsUtils.class);



    /**
     * 获取Es数据，如果是查询，term里传Es的查询语句。
     * 如果是新增或者修改,term里传的是要增加的或者要修改的数据.
     * url查询结尾_search，新增直接 POSt/hjj/hjj
       *@param queryJson
       *@param httpMethod
       *@param elasticUserPassword
       *@return
       *@Author : 吴璇璇
       *@throws IOException
       */
    public static ResponseEntity<String> jointES(@RequestBody JSONObject queryJson, HttpMethod httpMethod, String elasticUserPassword) throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] Base64UserPassword = elasticUserPassword.getBytes("UTF-8");
        String base64elasticUserPassword = encoder.encode(Base64UserPassword);
        RestTemplate restTemplate = new RestTemplate();
        MediaType mediaType = MediaType.parseMediaType("application/json;charset=UTF-8");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + base64elasticUserPassword);
        httpHeaders.setContentType(mediaType);
        HttpEntity<String> httpEntity = null;
        if (!"".equals(queryJson.get("term")) && queryJson.get("term") != null) {
            httpEntity = new HttpEntity<String>(net.sf.json.JSONObject.fromObject(queryJson.get("term")).toString(), httpHeaders);
        } else {
            httpEntity = new HttpEntity<String>("", httpHeaders);
        }
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryJson.get("url").toString(), httpMethod, httpEntity, String.class);
        return responseEntity;
    }





}
