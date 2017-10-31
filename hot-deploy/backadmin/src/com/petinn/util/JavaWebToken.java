package com.petinn.util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import org.ofbiz.base.util.Debug;
import java.util.*;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.spec.SecretKeySpec;

public class JavaWebToken {
    public static final String module = JavaWebToken.class.getName();

    //该方法使用HS256算法和Secret:bankgl生成signKey
    private static Key getKeyInstance() {
        //We will sign our JavaWebToken with our ApiKey secret
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("bankgl");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    //使用HS256签名算法和生成的signingKey最终的Token,claims中是有效载荷
    public static String createJavaWebToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();
    }

    //解析Token，同时也能验证Token，当验证失败返回null
    public static Map<String, Object> parserJavaWebToken(String jwt) {
        try {
            Map<String, Object> jwtClaims =
                    Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
            return jwtClaims;
        } catch (Exception e) {
            Debug.logError("json web token verify failed", module);
            return null;
        }
    }
/**
    String token = request.getParameter("token");
    if(JavaWebToken.parserJavaWebToken(token) != null){
        //表示token合法
        return true;
    }else{
        //token不合法或者过期
        return false;
    }
 **/
}