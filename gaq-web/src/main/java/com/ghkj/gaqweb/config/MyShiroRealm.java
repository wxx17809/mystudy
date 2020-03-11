package com.ghkj.gaqweb.config;

import com.ghkj.gaqcommons.untils.SessionUtil;
import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqservice.service.AdminUserService;
import com.ghkj.gaqservice.service.RoleAndPermissService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import javax.annotation.Resource;
import java.util.List;


/**
 * @version 1.0
 * @ClassName : MyShiroRealm
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/11/12 15:11
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private RoleAndPermissService roleAndPermissService;

    /**
     * 为当前登录的Subject授予角色和权限（权限相关）
     * 权限认证，即登录过后，每个身份不一定，对应的所能看的页面也不一样
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        AdminUser marketAdminUser = SessionUtil.getAdminUser();
        if(marketAdminUser==null){
            return authorizationInfo;
        }
        //根据用户组获取权限点集合
        List<String> list = roleAndPermissService.findRole(marketAdminUser.getRoleId());
        //权限加载
        authorizationInfo.addStringPermissions(list);
        return authorizationInfo;
    }

    /**
     * 验证当前登录的Subject（身份认证）
     * 身份认证。即登录通过账号和密码验证登陆人的身份信息。
     * @return info
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        SessionUtil.loginOut();
        //1、获取用户信息
        UsernamePasswordToken token=(UsernamePasswordToken)authToken;
        //2、获取username
        String username=token.getUsername();
        //3、调用数据库，查找是否存在该用户
        AdminUser user= adminUserService.findByUserName(username);
        if(user==null){
            throw new UnknownAccountException("用户不存在!");
        }
        if("1".equals(user.getIsDisabled())){
            throw new LockedAccountException("用户被注销!");
        }
        AuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),getName());
        return info;

    }



}
