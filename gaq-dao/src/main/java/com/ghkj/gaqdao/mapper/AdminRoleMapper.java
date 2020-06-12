package com.ghkj.gaqdao.mapper;

import com.ghkj.gaqentity.AdminRole;
import com.ghkj.gaqentity.AdminUser;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdminRoleMapper extends Mapper<AdminRole> {

    List<AdminRole> findRoleByUser(AdminUser userDto);
}