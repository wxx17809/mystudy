package com.ghkj.gaqentity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AdminPermission adminPermission;
	private List<AdminPermission> adminPermissionlist = new ArrayList<AdminPermission>();


}
