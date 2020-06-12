package com.ghkj.gaqentity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * UserRole
 * @author dolyw.com
 * @date 2018/8/31 14:43
 */
@Table(name = "user_role")
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = -3397516891053950951L;

    /**
     * ID
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色id
     */
    private Integer roleId;
}