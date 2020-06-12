package com.ghkj.gaqcommons.untils;


import com.ghkj.gaqentity.AdminUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;



/**
 * 用户管理
 */
public class SessionUtil {
	//session失效时间 半个小时
	private static Long sessionTime=30*60*1000L;
	private static String sessionKeyadmin="adminUser";
	private static String lefiOneVoList="lefiOneVoList";
	
	/**
	 * 方法描述：管理用户session存入
	 * 创建人：吴璇璇
	 * 创建时间：2018年5月4日 下午4:02:12
	 * @param user
	 */
	public static void setAdminUser(AdminUser user){
		Subject subject=SecurityUtils.getSubject();
		subject.getSession().setAttribute(sessionKeyadmin, user);
		setSessionTime(sessionTime);
	}
	
	/**
	 * 方法描述：获取管理用户
	 * 创建人：吴璇璇
	 * 创建时间：2018年5月4日 下午4:04:57
	 * @return
	 */
	public static AdminUser getAdminUser(){
		Subject subject=SecurityUtils.getSubject();
		return (AdminUser)subject.getSession().getAttribute(sessionKeyadmin);
	}
	


	
	/**
	 * 方法描述：设置session过期时间
	 * 创建人：吴璇璇
	 * 创建时间：2018年5月4日 下午4:00:38
	 * @param lon
	 */
	public static void setSessionTime(Long lon){
		SecurityUtils.getSubject().getSession().setTimeout(lon);
	}
	
	/**
	 * 方法描述：退出登录
	 * 创建人：吴璇璇
	 * 创建时间：2018年5月4日 下午4:06:37
	 */
	public static void loginOut(){
		Subject subject=SecurityUtils.getSubject();
		subject.logout();
	}
	

}
