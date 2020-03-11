package com.ghkj.gaqservice.service.impl;

import com.ghkj.gaqcommons.untils.DateTimeUtil;
import com.ghkj.gaqcommons.untils.SessionUtil;
import com.ghkj.gaqcommons.untils.TokenUtils;
import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqservice.service.AdminUserService;
import com.ghkj.gaqservice.service.LoginService;
import com.nimbusds.jose.JOSEException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName : LoginServiceImpl
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/11/12 15:48
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public Map<String, Object> login(String userName, String password) {
        Map<String, Object> dataMap = new HashMap<>();
        if (userName == null || userName.isEmpty()) {
            dataMap.put("msg", "用户名为空");
            return dataMap;
        }
        // 1、获取Subject实例对象
        Subject subject = SecurityUtils.getSubject();
        // 2、将用户名和密码封装到UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        // 3、认证
        try {
            // 传到MyAuthorizingRealm类中的方法进行认证
            subject.login(token);
            token.setRememberMe(true);
            dataMap.put("success",true);
            dataMap.put("msg","登录成功");
            AdminUser adminUser=adminUserService.findByUserName(userName);
            String tokens = TokenTest(adminUser.getId());
            dataMap.put("token",tokens);
            SessionUtil.setAdminUser(adminUser);
        } catch (UnknownAccountException e) {
            dataMap.put("success", false);
            dataMap.put("msg", "用户不存在!");
        }catch (LockedAccountException e){
            dataMap.put("success", false);
            dataMap.put("msg", "用户被注销");
        }catch (AuthenticationException e) {//身份认证异常
            dataMap.put("status", false);
            dataMap.put("msg", "用户名或密码不正确!");
        }catch(Exception e){
            dataMap.put("success",false);
            dataMap.put("msg","登录失败，请联系管理员！");
        }
        return dataMap;
    }


    //生成token的业务逻辑
    public static String TokenTest(Long id) {
        //获取生成token
        Map<String, Object> map = new HashMap<>();
        //建立载荷，这些数据根据业务，自己定义。
        map.put("uid", id);
        //生成时间
        map.put("sta", DateTimeUtil.NowTime());
        //过期时间
        map.put("exp", DateTimeUtil.expriredDate());
        try {
            String token = TokenUtils.creatToken(map);
            System.out.println("token="+token);
            return token;
        } catch (JOSEException e) {
            System.out.println("生成token失败");
            e.printStackTrace();
        }
        return null;

    }

}
