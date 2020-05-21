package com.ghkj.gaqservice.service.impl;

import com.ghkj.gaqcommons.untils.MD5Utils;
import com.ghkj.gaqdao.mapper.UserMapper;
import com.ghkj.gaqdao.utils.RedisUtil;
import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqservice.service.AdminUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName : JbbsUserServiceImpl
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/9/2 17:46
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private UserMapper userMapper;




    /**
     * page第一页传0
     * @param page
     * @param size
     * @return
     */
    @Override
    public Map<String, Object> selectAll(Integer page, Integer size) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(page + 1, size);
        List<AdminUser> list = new ArrayList<>();
        list = userMapper.selectAll();
        PageInfo pageInfo = new PageInfo(list);
        map.put("rows", list);
        //当前的页
        map.put("page", pageInfo.getPageNum());
        //总页数
        map.put("pages", pageInfo.getPages());
        //一共的条数
        map.put("total", pageInfo.getTotal());
        return map;
    }

    /**
     * 更新用户策略：先更新数据表，成功之后，删除原来的缓存，再更新缓存
     */
    @Override
    public Map<String, Object> saveOrUpdateUser(AdminUser jbbsUser) {
        Map<String, Object> map = new HashMap<>();
        int result = 0;
        if (jbbsUser.getId() != null) {
            //走修改
            String password= MD5Utils.stringToMD5(jbbsUser.getPassword());
            jbbsUser.setPassword(password);
            result = userMapper.updateByPrimaryKeySelective(jbbsUser);
            if(result > 0){
                String key = "login_" + jbbsUser.getId();
                boolean haskey = RedisUtil.hasKey(key);
                if (haskey) {
                    RedisUtil.del(key);
                    System.out.println("删除缓存中的key=========>" + key);
                }
                // 再将更新后的数据加入缓存
                AdminUser userNew=userMapper.selectByPrimaryKey(jbbsUser.getId());
                RedisUtil.setObject(key,userNew);
            }
        } else {
            String password= MD5Utils.stringToMD5(jbbsUser.getPassword());
            jbbsUser.setPassword(password);
            result = userMapper.insertSelective(jbbsUser);
        }
        if (result > 0) {
            map.put("state", 200);
            map.put("msg", "操作成功");
        }
        return map;
    }

    /**
     * 删除用户策略：删除数据表中数据，然后删除缓存
     */
    @Override
    public Map<String, Object> deleteUser(Integer id) {
        Map<String, Object> map = new HashMap<>();
        int result = 0;
        result = userMapper.deleteByPrimaryKey(id);
        if (result > 0) {
            String key="user_"+id;
            boolean hasKey=RedisUtil.hasKey(key);
            if(hasKey){
                RedisUtil.del(key);
                System.out.println("删除了缓存中的key:" + key);
            }
            map.put("state", 200);
            map.put("msg", "删除成功");
        }
        return map;
    }


    @Override
    public Map<String, Object> updateByName(AdminUser jbbsUser) {
        Map<String, Object> map = new HashMap<>();
        int result = 0;
        Example example = new Example(AdminUser.class);
        example.createCriteria().andEqualTo("name", jbbsUser);
        result = userMapper.updateByExampleSelective(jbbsUser, example);
        if (result > 0) {
            map.put("state", 200);
            map.put("msg", "修改成功");
        }
        return map;
    }

    @Override
    public Map<String, Object> findOne(Integer id) {
        Map<String, Object> map = new HashMap<>();
        String key = "user_" + id;
        boolean hasKey=RedisUtil.hasKey(key);
        if(hasKey){
            //从缓存中获得数据
            AdminUser user =(AdminUser) RedisUtil.getObject(key);
            map.put("jbbsUser", user);
        }else{
            AdminUser jbbsUser = userMapper.selectByPrimaryKey(id);
            //写入缓存
            RedisUtil.setObject(key,jbbsUser);
            map.put("jbbsUser", jbbsUser);
        }
        return map;
    }

    @Override
    public AdminUser findByUserName(String userName) {
        AdminUser adminUser=new AdminUser();
        adminUser.setUsername(userName);
        List<AdminUser> userList=userMapper.select(adminUser);
        return userList.get(0);
    }


}

