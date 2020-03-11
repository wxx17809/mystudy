package com.ghkj.gaqcommons.untils;



import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import net.minidev.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/12/4 14:55
 */
public class TokenUtils {

    /**
     * 共用密钥--保存在服务器，客户端是不会知道密钥的，以防止被攻击
     */
    /**
     * 1.创建一个32-byte的密匙
     */

    private static final byte[] secret = "geiwodiangasfdjsikolkjikolkijswe".getBytes();


    /**
     * 生成token
     * https://www.jianshu.com/p/75208a68c3b9
     * @param payloadMap
     * @return
     * @throws JOSEException
     */
    public static String creatToken(Map<String,Object> payloadMap) throws JOSEException {

        //3.先建立一个头部Header
        /**
         * JWSHeader参数：1.加密算法法则,2.类型，3.。。。。。。。
         * 一般只需要传入加密算法法则就可以。
         * 这里则采用HS256
         * JWSAlgorithm类里面有所有的加密算法法则，直接调用。
         */
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        //建立一个载荷Payload
        Payload payload = new Payload(new JSONObject(payloadMap));
        //将头部和载荷结合在一起
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        //建立一个密匙
        JWSSigner jwsSigner = new MACSigner(secret);
        //签名
        jwsObject.sign(jwsSigner);
        //生成token
        return jwsObject.serialize();
    }




    /**
     * 解析token
     * @param token
     */
    public static int ValidToken(String token) {
         int i=-1;
        //解析token
        try {
            if (token != null) {
                Map<String, Object> validMap = TokenUtils.valid(token);
                i = (int) validMap.get("Result");
                if (i == 0) {
                    System.out.println("token解析成功");
                    JSONObject jsonObject = (JSONObject) validMap.get("data");
                    System.out.println("uid是" + jsonObject.get("uid"));
                    System.out.println("sta是"+jsonObject.get("sta"));
                    System.out.println("exp是"+jsonObject.get("exp"));
                } else if (i == 2) {
                    System.out.println("token已经过期");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 验证token
     * @param token
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    public static Map<String,Object> valid(String token) throws ParseException, JOSEException {

        //解析token
        JWSObject jwsObject = JWSObject.parse(token);
        //获取到载荷
        Payload payload=jwsObject.getPayload();
        //建立一个解锁密匙
        JWSVerifier jwsVerifier = new MACVerifier(secret);
        Map<String, Object> resultMap = new HashMap<>();
        //判断token
        if (jwsObject.verify(jwsVerifier)) {
            resultMap.put("Result", 0);
            //载荷的数据解析成json对象。
            JSONObject jsonObject = payload.toJSONObject();
            resultMap.put("data", jsonObject);
            //判断token是否过期
            if (jsonObject.containsKey("exp")) {
                Long expTime = Long.valueOf(jsonObject.get("exp").toString());
                Long nowTime = Long.valueOf(DateTimeUtil.NowTime());
                //判断是否过期
                if (nowTime > expTime) {
                    //已经过期
                    resultMap.clear();
                    resultMap.put("Result", 2);
                }
            }
        }else {
            resultMap.put("Result", 1);
        }
        return resultMap;

    }

}
