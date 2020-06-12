package com.ghkj.gaqdao.mapper;

import com.ghkj.gaqentity.AdminPermission;
import com.ghkj.gaqentity.AdminRole;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdminPermissionMapper extends Mapper<AdminPermission> {

    List<AdminPermission> findPermissionByRole(AdminRole roleDto);
}