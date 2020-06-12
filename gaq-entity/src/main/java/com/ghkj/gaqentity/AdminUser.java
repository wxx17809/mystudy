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
    private Integer userId;

    private String userName;

    private String password;

    private Date lastLoginTime;

    private Date loginTime;

    private Integer isDisabled;

    private Date createtime;
}