package com.ghkj.gaqservice.service.impl;

import com.ghkj.gaqcommons.untils.PermssUtil;
import com.ghkj.gaqdao.mapper.AdminPermissionMapper;
import com.ghkj.gaqdao.mapper.AdminRoleMapper;
import com.ghkj.gaqentity.AdminPermission;
import com.ghkj.gaqentity.AdminRole;
import com.ghkj.gaqservice.service.RoleAndPermissService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @ClassName : RoleAndPermissServiceImpl
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/11/15 9:24
 */
@Service
public class RoleAndPermissServiceImpl implements RoleAndPermissService {
    @Autowired
    private AdminPermissionMapper adminPermissionMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;


    @Override
    public List<String> findRole(Long roleId) {
        List<String> list=new ArrayList<>();
        AdminRole adminRole=adminRoleMapper.selectByPrimaryKey(roleId);
        if(adminRole==null){
            return list;
        }
        return list;
    }
}
