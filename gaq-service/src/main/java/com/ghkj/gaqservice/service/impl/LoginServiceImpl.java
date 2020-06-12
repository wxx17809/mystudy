package com.ghkj.gaqservice.service.impl;
import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqservice.service.AdminUserService;
import com.ghkj.gaqservice.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${accessTokenExpireTime}")
    private Long expiration;

    @Override
    public Map<String, Object> login(String userName, String password) {
        Map<String, Object> dataMap = new HashMap<>();
        if (userName == null || userName.isEmpty()) {
            dataMap.put("msg", "用户名为空");
            return dataMap;
        }
        AdminUser adminUser = null;
        try {
            adminUser = adminUserService.selectone(userName, password);
            dataMap.put("msg","登录成功");
        } catch (Exception e) {
            e.printStackTrace();
        }


        return dataMap;
    }

}
