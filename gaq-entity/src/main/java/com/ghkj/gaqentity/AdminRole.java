package com.ghkj.gaqentity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "admin_role")
@Data
public class AdminRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String roleName;

    private String roleContent;

    private Date createTime;

}