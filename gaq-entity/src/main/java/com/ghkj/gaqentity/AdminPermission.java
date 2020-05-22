package com.ghkj.gaqentity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name ="admin_permission")
@Data
public class AdminPermission {

    private static final long serialVersionUID =  5389525112337538165L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer permissionId;

    private String permissionName;

    private String permissionUrl;

    private Integer parentId;

    private String permissionCode;

    private Integer permissionIndex;

    private List<AdminPermission> adminPermissionlist = new ArrayList<AdminPermission>();

}