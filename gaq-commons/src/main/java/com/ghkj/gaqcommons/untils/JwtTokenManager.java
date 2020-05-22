package com.ghkj.gaqcommons.untils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import com.ghkj.gaqentity.AdminUser;
import com.ghkj.gaqentity.TokenUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenManager implements Serializable {
    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_USERID = "id";
    private static final String CLAIM_KEY_LOGINIP = "ip";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * 生成token.
     *
     * @return
     */
    public String generateToken(AdminUser tokenUser, Long expirationSeconds) throws Exception {
        expiration = expirationSeconds;
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, tokenUser.getUsername());
        claims.put(CLAIM_KEY_USERID, String.valueOf(tokenUser.getId()));
        String ip = InetAddress.getLocalHost().getHostAddress();
        claims.put(CLAIM_KEY_LOGINIP, ip);
        claims.put(CLAIM_KEY_CREATED, DateTimeUtil.NowTime());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())//过期时间
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 通过token 获取用户名.
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成token 时间 = 当前时间+ expiration（properties中配置的失效时间）
     *
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 判断token 失效时间是否到了
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 获取设置的token 失效时间.
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public static void main(String[] args) throws Exception {
        // 获取本地IP地址
        String ip = InetAddress.getLocalHost().getHostAddress();
        // 获取本地计算机名
        String name = InetAddress.getLocalHost().getHostName();
        //输出控制台
        System.out.println("IP地址：" + ip);
        System.out.println("本地计算机名：" + name);
        System.out.println("======" + DateTimeUtil.NowTime());

    }

    /**
     * Token失效校验.
     *
     * @param token     token字符串
     * @param loginInfo 用户信息
     * @return
     */
    public Boolean validateToken(String token, AdminUser loginInfo) {
        //1.校验签名是否正确
        //2.token是否过期
        //......
        TokenUser tokenUser = getTokenUserFromToken(token);
        Integer userId = tokenUser.getId();
        return (
                userId.equals(loginInfo.getId())
                        && !isTokenExpired(token));
    }

    /**
     * 根据token获取信息
     *
     * @param token
     * @return
     */
    public TokenUser getTokenUserFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        Object id = claims.get(CLAIM_KEY_USERID);
        Integer userId = Integer.valueOf((String) id);
        TokenUser tokenUser = new TokenUser();
        tokenUser.setLastIp((String) claims.get(CLAIM_KEY_LOGINIP));
        tokenUser.setId(userId);
        tokenUser.setUsername((String) claims.get(CLAIM_KEY_USERNAME));
        tokenUser.setToken(token);
        return tokenUser;
    }

    //验证token
    public boolean verity(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        try {
            jwtVerifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;

    }


}
