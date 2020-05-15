package com.ghkj.gaqweb.controller;

import com.ghkj.gaqservice.service.HjjService;
import com.alibaba.fastjson.JSONObject;
import com.ghkj.gaqweb.untils.EsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName : HjjController
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/9/4 15:17
 */
@Api(description = "目标操作")
@RestController
@RequestMapping("/hello")
public class HjjController {

    private static  final Logger logger= LoggerFactory.getLogger(HjjController.class);

    @Value("${elasticUserPassword}")
    private String elasticUserPassword;
    @Value("${elasticIp}")
    private String elasticIp;
    @Autowired
    private HjjService hjjService;



    /**查询、新增、修改 都可以用POST请求，查询term里是Es语句，新增和修改term是json格式数据。删除用DElete请求，term是空
     * 查询传参数
     {
       "term":{
         "query": {
           "match": {
            "name.keyword": "吴璇璇"
         }
       }
      },
      "operate":"query",
      "url":"http://192.168.1.15:9200/hjj/hjj/_search"
     }

     * 删除传参数
      {
        "term":{},
         "operate":"delete",
         "url":"http://192.168.1.15:9200/hjj/hjj/wXSk-2wBVGpRCmzM4bbO"

      }
     * 新增传参数
     {
         "term":{
             "name": "吴璇璇",
             "point": null,
             "keyname": "吴璇璇",
             "area": null,
             "version": 3
         },
        "operate":"add",
        "url":"http://192.168.1.15:9200/hjj/hjj/"
     }
     *修改传参数（全部修改）
     {
     "term":{
         "name": "吴璇璇",
         "point": null,
         "keyname": "吴璇璇",
         "area": null,
         "version": 3
     },
     "operate":"update",
     "url":"http://192.168.1.15:9200/hjj/hjj/IV4i_2wByewDtMsdBVIC"
     }
     *部分修改,term里传es语句
     {
       "term":{
         "doc":{
         "keycountry": "china"
         }
     },
     "operate":"update",
     "url":"http://192.168.1.15:9200/hjj/hjj/Il4x_2wByewDtMsdxVIy/_update"
     }
     * @param json
     * @return
     * @throws IOException
     */

    @ApiOperation("操作Es数据库，增查改")
    @PostMapping(value = "/queryAddUpdateByEs",produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> queryAddUpdateByEs(@RequestBody JSONObject json)throws IOException {
        String operate=json.get("operate").toString();
        logger.info(operate);
        if("delete".equals(operate)){
            return ResponseEntity.ok(EsUtils.jointES(json,HttpMethod.DELETE,elasticUserPassword).getBody());
        }else{
            return ResponseEntity.ok(EsUtils.jointES(json,HttpMethod.POST,elasticUserPassword).getBody());
        }
    }


    /**
     * 新增或者修改目标
     * 吴璇璇
     * @return
     */
    @PostMapping("/addOrUpdateHjj")
    public Map<String,Object> addOrUpdateHjj(@RequestBody JSONObject hjj)throws Exception{
          return hjjService.addOrUpdateHjj(hjj);
    }

    /**
     * 删除目标
     * @param ids
     * @return
     */
    @PostMapping("/deleteHjj")
    public Map<String,Object> deleteHjj(@RequestBody JSONObject ids){
        return hjjService.deleteHjj(ids);

    }

    /**
     * 查询目标详情
     * @return
     */
    @PostMapping("/findDetail")
    public Map<String,Object> findDetail(@RequestBody JSONObject ids){
        return hjjService.findDetail(ids);
    }


    /**
     * 多个搜索条件搜索
     * @return
     */
    @PostMapping("/findHjjList")
    public Map<String,Object> findHjjList(@RequestBody JSONObject hjjParams){
         Map result=hjjService.findHjjList(hjjParams);
         return result;
    }











}
