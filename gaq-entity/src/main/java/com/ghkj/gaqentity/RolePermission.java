package com.ghkj.gaqentity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * RolePermission
 * @author dolyw.com
 * @date 2018/8/31 14:43
 */
@Table(name = "role_permission")
@Data
public class RolePermission implements Serializable {

    private static final long serialVersionUID = -8564770707000796503L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限id
     */
    private Integer permissionId;
}