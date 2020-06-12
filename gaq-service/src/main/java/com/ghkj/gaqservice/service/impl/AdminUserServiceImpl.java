package com.ghkj.gaqservice.service.impl;

import com.ghkj.gaqcommons.untils.MD5Utils;
import com.ghkj.gaqcommons.untils.userJIaMi.AesCipherUtil;
import com.ghkj.gaqdao.mapper.AdminPermissionMapper;
import com.ghkj.gaqdao.mapper.AdminRoleMapper;
import com.ghkj.gaqdao.mapper.UserMapper;
import com.ghkj.gaqdao.utils.RedisUtil;
import com.ghkj.gaqentity.AdminPermission;
import com.ghkj.gaqentity.AdminRole;
import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqservice.service.AdminUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

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
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private AdminPermissionMapper adminPermissionMapper;




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
        if (jbbsUser.getUserId() != null) {
            //走修改
            String password= AesCipherUtil.enCrypto(jbbsUser.getUserName()+jbbsUser.getPassword());
            jbbsUser.setPassword(password);
            result = userMapper.updateByPrimaryKeySelective(jbbsUser);
            if(result > 0){
                String key = "login_" + jbbsUser.getUserId();
                boolean haskey = RedisUtil.hasKey(key);
                if (haskey) {
                    RedisUtil.del(key);
                    System.out.println("删除缓存中的key=========>" + key);
                }
                // 再将更新后的数据加入缓存
                AdminUser userNew=userMapper.selectByPrimaryKey(jbbsUser.getUserId());
                RedisUtil.setObject(key,userNew);
            }
        } else {
            //String password= MD5Utils.stringToMD5(jbbsUser.getPassword());
            String password= AesCipherUtil.enCrypto(jbbsUser.getUserName()+jbbsUser.getPassword());
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
        adminUser.setUserName(userName);
        List<AdminUser> userList=userMapper.select(adminUser);
        return userList.get(0);
    }

    @Override
    public List<AdminPermission> leftTab(AdminUser adminUser) {
        AdminRole adminRole = adminRoleMapper.selectByPrimaryKey(adminUser.getUserId());
        return null;
    }

    @Override
    public AdminUser selectone(String userName, String password) {
        AdminUser adminUser1=new AdminUser();
        adminUser1.setUserName(userName);
        adminUser1.setPassword(password);
        AdminUser adminUser=userMapper.selectOne(adminUser1);
        return adminUser;
    }

//    public List<AdminPermission> leftTab(AdminRole adminRole){//权限表主键1,2,3,4,5,6
//        List<AdminPermission> lefiOneVoList=new ArrayList<>();//一级菜单
//        List<AdminPermission> lefichildList=new ArrayList<>();//子极菜单
//        String roleContent[]=adminRole.getRoleContent().split(",");
//        for (int i=0;i<roleContent.length;i++){
//            AdminPermission adminPermission=adminPermissionMapper.selectByPrimaryKey(Integer.parseInt(roleContent[i]));
//            if(adminPermission.getParentId()==0){
//                Example example=new Example(AdminPermission.class);
//                example.createCriteria().andEqualTo("parentId",adminPermission.getPermissionId());
//                lefichildList=adminPermissionMapper.selectByExample(example);
//                adminPermission.setAdminPermissionlist(lefichildList);
//                lefiOneVoList.add(adminPermission);
//            }
//        }
//        return lefiOneVoList;
//    }

}

