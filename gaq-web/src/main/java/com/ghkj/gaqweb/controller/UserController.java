package com.ghkj.gaqweb.controller;
import com.alibaba.fastjson.JSONObject;
import com.ghkj.gaqcommons.untils.TokenUtils;
import com.ghkj.gaqdao.utils.RedisUtil;
import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqservice.service.AdminUserService;
import io.swagger.annotations.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @version 1.0
 * @ClassName : UserController
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/9/2 16:08
 */
@Api(description = "用户管理模块")
@RequestMapping("/user")
@RestController
public class UserController {

    private static  final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private AdminUserService adminUserService;


    /**
     * 查询用户列表
     * @return
     */
    @ApiOperation("查询全部用户接口")
    @PostMapping("/findUserList")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "当前页数",required = true),
            @ApiImplicitParam(name = "size", value = "每页条数",required = true),
            })
    public Map<String,Object> findUserList(Integer page,Integer size){
        logger.info("进入查询全部用户的方法===page=="+page+"size===="+size);
        Map<String,Object> map=new HashMap<>();
        String token= RedisUtil.getObject("token").toString();
        int result=TokenUtils.ValidToken(token);
        if(result==0){
            map=adminUserService.selectAll(page,size);
        }
        logger.info("离开查询全部用户的方法");
        return map;
    }

    /**
     * 新增或者修改用户
     * @param jbbsUser
     * @return
     */
    @ApiOperation("保存或者修改用户")
    @PostMapping(value = "/saveorUpdateUser",produces ="application/json;charset=UTF-8")
    public Map<String,Object> saveOrUpdateUser(@RequestBody @ApiParam(name="用户对象",value="传入json格式",required=true)AdminUser jbbsUser){
        logger.info("进入保存或者修改用户的方法=="+ JSONObject.toJSONString(jbbsUser));
        Map<String,Object> map=adminUserService.saveOrUpdateUser(jbbsUser);
        logger.info("离开保存或者修改用户的方法=="+ JSONObject.toJSONString(jbbsUser));
        return map;

    }

    /**
     * 删除用户
     * @param jsonObject
     * @return
     */
    @ApiOperation("删除用户")
    @PostMapping(value = "/deleteUser")
    public Map<String,Object> deleteUser(@RequestBody JSONObject jsonObject){
        logger.info("进入删除用户方法==="+Integer.parseInt(jsonObject.get("id").toString()));
        Map<String,Object> map=adminUserService.deleteUser(Integer.parseInt(jsonObject.get("id").toString()));
        logger.info("离开删除用户方法==");
        return map;
    }


    /**
     * 根据用户名修改
     */
    @ApiOperation("根据用户名修改")
    @PostMapping(value = "/updateByName",produces ="application/json;charset=UTF-8")
    public Map<String,Object> updateByName(@RequestBody AdminUser adminUser){
        Map<String,Object> map=adminUserService.updateByName(adminUser);
        return map;
    }


    /**
     * 查询用户信息
     * @param jsonObject
     * @return
     */
    @ApiOperation("查询用户详情")
    @PostMapping("/findUserDetail")
    public Map<String,Object> findUserDetail(@RequestBody JSONObject jsonObject){
         logger.info("进入查询用户详情==="+Integer.parseInt(jsonObject.get("id").toString()));
         Map<String,Object> map=adminUserService.findOne(Integer.parseInt(jsonObject.get("id").toString()));
         return map;

    }






}
