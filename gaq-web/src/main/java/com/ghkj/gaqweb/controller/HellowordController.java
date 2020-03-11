package com.ghkj.gaqweb.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @version 1.0
 * @ClassName : HellowordController
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/9/2 11:15
 */
@Api(description = "测试项目启动")
@RestController
@RequestMapping("/hello")
public class HellowordController {

    private static final Logger logger = LoggerFactory.getLogger(HellowordController.class);

}
