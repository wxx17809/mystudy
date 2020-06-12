package com.ghkj.gaqservice.service;

import com.ghkj.gaqentity.AdminPermission;
import com.ghkj.gaqentity.AdminUser;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName : JbbsUserService
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/9/2 17:43
 */
public interface AdminUserService {
    /**
     * 查询全部用户
     * @param page
     * @param size
     * @return
     */
    Map<String,Object> selectAll(Integer page, Integer size);

    /**
     *  增或者修改用户
     * @param jbbsUser
     * @return
     */
    Map<String, Object> saveOrUpdateUser(AdminUser jbbsUser);

    /**
     * 删除用户
     * @param id
     * @return
     */
    Map<String, Object> deleteUser(Integer id);

    /**
     * 根据用户名修改
     * @param jbbsUser
     * @return
     */
    Map<String, Object> updateByName(AdminUser jbbsUser);

    /**
     * 查询用户详情
     * @param id
     * @return
     */
    Map<String, Object> findOne(Integer id);

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    AdminUser findByUserName(String userName);

    /**
     * 查询全部权限菜单
     * @param adminUser
     * @return
     */
    List<AdminPermission> leftTab(AdminUser adminUser);

    AdminUser selectone(String userName, String password);
}
