package com.ghkj.gaqservice.service;


import java.util.List;

/**
 * @version 1.0
 * @ClassName : RoleAndPermissService
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/11/15 9:23
 */
public interface RoleAndPermissService {
    /**
     * 查询权限集合
     * @param roleId
     * @return
     */
    List<String> findRole(Long roleId);
}
