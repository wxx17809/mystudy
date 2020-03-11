package com.ghkj.gaqentity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "admin_user")
@Data
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private String name;

    private Integer roleId;

    private Date lastLoginTime;

    private Date loginTime;

    private Integer isDisabled;

    private Date createtime;
}