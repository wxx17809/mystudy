package com.ghkj.gaqweb.controller;

import com.alibaba.fastjson.JSONObject;
import com.ghkj.gaqcommons.untils.SessionUtil;
import com.ghkj.gaqservice.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName : LoginController
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/11/14 16:32
 */
@RestController
@RequestMapping(value = "/login")
@Api(description = "登录退出方法")
public class LoginController {
    private static  final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginService loginService;

    /**
     * 登录方法
     * @param jsonObject
     * @return
     */
    @ApiOperation("登录方法")
    @PostMapping(value = "/loginUser")
    public Map<String,Object> loginUser(@RequestBody JSONObject jsonObject){
        logger.info("进入登录用户方法===");
        String userName=jsonObject.get("userName").toString();
        String password=jsonObject.get("password").toString();
        logger.info("userName==="+userName+"==password==="+password);
        Map<String,Object> map=loginService.login(userName,password);
        logger.info("离开登录方法==");
        return map;
    }


    /**
     * 退出方法
     */
    @ApiOperation("退出")
    @PostMapping("/loginOut")
    public Map<String,Object> loginOut(){
        Map<String,Object> map=new HashMap<>();
        SessionUtil.loginOut();
        map.put("success",true);
        map.put("msg","退出成功");
        return map;
    }


}
