package com.ghkj.gaqweb.config;

import com.alibaba.fastjson.JSON;
import com.ghkj.gaqcommons.untils.JwtTokenManager;
import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqservice.service.AdminUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


public class AuthorityIntercepter extends  HandlerInterceptorAdapter {

     @Autowired
     private AdminUserService adminUserService;
     @Autowired
     private JwtTokenManager jwtTokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("token");// 从 http 请求头中取出 token
        System.out.println("获取到token================"+token);
        // 执行认证 
        if (token == null || "".equals(token.trim())) {
            return responseData(response);
        }
        // 获取 token 中的 user id
        Integer userId = jwtTokenManager.getTokenUserFromToken(token).getId();
        if (userId == null) {
            return responseData(response);
        }
        //用户不存在的情况 属于作假token进行登录
        Map<String,Object> map = adminUserService.findOne(userId);
        AdminUser user=(AdminUser) map.get("jbbsUser");
        if (user == null) {
            return responseData(response);
        }
        // 验证 token  
        boolean flage = jwtTokenManager.verity(token);
        //用户存在但是token也不是根据我的规则产生的说明也是假的
        if (flage == false) {
            return responseData(response);
        }
        //将验证通过后的用户信息放到请求中,继续往下执行
        request.setAttribute("user", user);
        return true;
    }

    //响应数据
    public boolean responseData(HttpServletResponse response) throws IOException {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();
        json.put(104, "暂未登录！");
        String s = JSON.toJSONString(json);
        PrintWriter writer = response.getWriter();
        writer.println(s);
        return false;
    }

}

