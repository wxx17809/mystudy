package com.ghkj.gaqweb.controller;

import com.ghkj.gaqcommons.untils.DateTimeUtil;
import com.ghkj.gaqcommons.untils.ExcelUtil;
import com.ghkj.gaqdao.mapper.AdminRoleMapper;
import com.ghkj.gaqdao.mapper.AdminUserMapper;
import com.ghkj.gaqentity.AdminRole;
import com.ghkj.gaqentity.AdminUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "Excel操作和Sqlite测试")
@RestController
@RequestMapping(value = "/dealExcel")
public class ExcelController {

    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private AdminUserMapper adminUserMapper;


    /**
     * 解析Excel，存到数据库
     * @param file
     * @return
     */
    @PostMapping(value = "/readExcel")
    @ApiOperation("读取excel")
    public Map<String, Object> readExcel(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();//最后的结果
        map=ExcelUtil.readExcel(file);
        List<Map<String,Object>> list=(List<Map<String,Object>>)map.get("arrcell");
        for (int i=0;i<list.size();i++){
            Map<String,Object> oneMap=list.get(i);
            AdminRole adminRole=new AdminRole();
            adminRole.setRoleName(oneMap.get("0").toString());
            adminRoleMapper.insert(adminRole);
        }
        return map;
    }


    /**
     * 解析Excel，存到数据库
     * @return
     */
    @GetMapping(value = "/exportExcel")
    @ApiOperation("导出excel")
    public void exportExcel(HttpServletResponse response) {
        List<Map<Integer, Object>> listContent=new ArrayList<>();
        List<AdminUser> list= adminUserMapper.selectAll();
       for (int i=0;i<list.size();i++){
           Map<Integer,Object> objectMap=new HashMap<>();
           objectMap.put(0,list.get(i).getUserId());
           objectMap.put(1,list.get(i).getUserName());
           objectMap.put(3, DateTimeUtil.dateToStrLong(list.get(i).getCreatetime()));
           listContent.add(objectMap);
       }
       String[] title={"Id","名称","真实名称","时间"};
       ExcelUtil.exportExcel("用户表.xls",title,listContent,response);
    }






}
