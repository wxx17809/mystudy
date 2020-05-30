package com.ghkj.gaqweb.controller;

import com.ghkj.gaqcommons.untils.DateTimeUtil;
import com.ghkj.gaqcommons.untils.ExcelUtil;
import com.ghkj.gaqdao.mapper.AdminRoleMapper;
import com.ghkj.gaqdao.mapper.AdminUserMapper;
import com.ghkj.gaqdao.mapper.StudentMapper;
import com.ghkj.gaqentity.AdminRole;
import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqentity.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private StudentMapper studentMapper;

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
            adminRole.setRoleContent(oneMap.get("1").toString());
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
           objectMap.put(0,list.get(i).getId());
           objectMap.put(1,list.get(i).getUsername());
           objectMap.put(2,list.get(i).getName());
           objectMap.put(3, DateTimeUtil.dateToStrLong(list.get(i).getCreatetime()));
           listContent.add(objectMap);
       }
       String[] title={"Id","名称","真实名称","时间"};
       ExcelUtil.exportExcel("用户表.xls",title,listContent,response);
    }

    /**
     * 新增学生
     * @param student
     * @return
     */
        @PostMapping(value = "/insetStudent")
        @ApiOperation("添加学生信息")
       public Map<String,Object> inset(Student student){
           Map<String,Object> map=new HashMap<>();
           try {
               int result=studentMapper.insert(student);
               logger.info("插入的主键id==="+student.getId());
               if(result > 0){
                   map.put("msg","成功");
                   map.put("state",200);
               }else{
                   map.put("msg","失败");
                   map.put("state",201);
               }
           }catch (Exception e){
               e.printStackTrace();
           }
           return map;
       }

    /**
     * 查询全部学生
      * @return
     */
    @PostMapping(value = "/selectStudent")
    @ApiOperation("查询学生信息")
    public Map<String,Object> selectStudent(){
        Map<String,Object> map=new HashMap<>();
        try {
            List<Student>list=studentMapper.selectList(null);
            if(list.size() > 0){
                map.put("msg","成功");
                map.put("state",200);
                map.put("list",list);
            }else{
                map.put("msg","失败");
                map.put("state",201);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 修改学生
     * @param student
     * @return
     */
    @PostMapping(value = "/updateStudent")
    @ApiOperation("添加学生信息")
    public Map<String,Object> update(Student student){
        Map<String,Object> map=new HashMap<>();
        try {
            int result=studentMapper.updateById(student);
            if(result > 0){
                map.put("msg","成功");
                map.put("state",200);
            }else{
                map.put("msg","失败");
                map.put("state",201);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 删除学生
     * @return
     */
    @PostMapping(value = "/deleteStudent")
    @ApiOperation("删除学生信息")
    public Map<String,Object> delete(Integer id){
        Map<String,Object> map=new HashMap<>();
        try {
            int result=studentMapper.deleteById(id);
            if(result > 0){
                map.put("msg","成功");
                map.put("state",200);
            }else{
                map.put("msg","失败");
                map.put("state",201);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 根据Id查询学生信息
     * @return
     */
    @PostMapping(value = "/findById")
    @ApiOperation("根据id查询学生信息")
    public Map<String,Object> findById(Integer id){
        Map<String,Object> map=new HashMap<>();
        try {
            Student student=studentMapper.selectById(id);
            map.put("msg","成功");
            map.put("state",200);
            map.put("student",student);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }






}
