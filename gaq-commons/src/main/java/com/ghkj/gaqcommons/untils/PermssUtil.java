package com.ghkj.gaqcommons.untils;



import com.ghkj.gaqentity.AdminPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * 类描述：权限标识和集合 <br>
 * 作者：徐诚 <br>
 * 创建时间：2017-4-10 下午09:00:49 <br>
 * 版本：V1.0
 */
public class PermssUtil {
	public final static Integer PARENTID=0;			//父id
	public final static String SY="SY";			//首页
	public final static String HYK="HYK";			//会员卡管理
	public final static String HYKLB="HYKLB";			//会员卡列表
	public final static String JFGL="JFGL";			//积分管理
	public final static String TSGL="TSGL";			//图书管理
	public final static String TSFL="TSFL";			//图书分类
	public final static String TULB="TULB";			//图书列表
	
	public final static String JHGL="JHGL";			//借还管理
	public final static String JYJL="JYJL";			//借阅记录
	public final static String SDHS="SDHS";			//手动还书
	public final static String XTSZ="XTSZ";			//系统数值
	public final static String TSGGL="TSGGL";			//图书馆基本信息管理
	public final static String ZHGL="ZHGL";			//账号管理
	/**
	 * 权限<Market_ADMIN_PERMISSION表id,Market_PERMISSION_CODE权限唯一标识>
	 */
	public static Map<Integer,String> adminPermissionMap=new HashMap<Integer, String>();
	/**
	 * 权限<Market_ADMIN_PERMISSION表id,Market_AdminPermission权限实体>
	 */
	public static Map<Integer,AdminPermission> adminPermissionBeanMap=new HashMap<Integer, AdminPermission>();

	/**
	 * 权限<Market_ADMIN_PERMISSION>升序排序
	 */
	public static List<AdminPermission> adminPermissionList=null;









}
