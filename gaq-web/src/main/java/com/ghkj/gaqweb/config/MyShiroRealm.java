package com.ghkj.gaqweb.config;

import com.ghkj.gaqcommons.untils.jwtUtil.JWTUtil;
import com.ghkj.gaqcommons.untils.jwtUtil.JwtToken;
import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqentity.TokenUser;
import com.ghkj.gaqservice.service.AdminUserService;
import com.ghkj.gaqservice.service.RoleAndPermissService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @version 1.0
 * @ClassName : MyShiroRealm
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/11/12 15:11
 */
@Service
public class MyShiroRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RoleAndPermissService roleAndPermissService;

   /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }


    /**
     * 为当前登录的Subject授予角色和权限（权限相关）
     * 权限认证，即登录过后，每个身份不一定，对应的所能看的页面也不一样
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String account = JWTUtil.getClaim(principals.toString(), "account");
        AdminUser marketAdminUser=adminUserService.findByUserName(account);
        if(marketAdminUser==null){
            return authorizationInfo;
        }
        //根据用户组获取权限点集合
        List<String> list = roleAndPermissService.findRole(marketAdminUser.getRoleId().longValue());
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
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = null;
        try {
            //这里工具类没有处理空指针等异常这里处理一下(这里处理科学一些)
            username = JWTUtil.getClaim(token,"account");
        } catch (Exception e) {
            throw new AuthenticationException("heard的token拼写错误或者值为空");
        }
        if (username == null) {
            log.error("token无效(空''或者null都不行!)");
            throw new AuthenticationException("token无效");
        }
        AdminUser userBean = adminUserService.findByUserName(username);
        if (userBean == null) {
            log.error("用户不存在!)");
            throw new AuthenticationException("用户不存在!");
        }
        if (!JWTUtil.verify(token,userBean.getPassword())) {
            log.error("用户名或密码错误(token无效或者与登录者不匹配)!)");
            throw new AuthenticationException("用户名或密码错误(token无效或者与登录者不匹配)!");
        }
        return new SimpleAuthenticationInfo(token, token, "MyShiroRealm");

    }



}
