package com.example.memory.Tool;

import io.jsonwebtoken.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final long EXPIRATION_TIME = 2 * 60 * 60 * 1000;
    // 密钥开发中应放在配置文件，且足够复杂
    private static final String SECRET_KEY = "your1secret1key11234567890abcdefghijklmnopqrstuvwxyz";

    // 生成 JWT 令牌
    public static String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        SecretKey key = getSecretKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 验证 JWT 并返回用户名
    public static String verifyToken(String token) {
        try {
            SecretKey key = getSecretKey();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("username",String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // 生成加密密钥
    private static SecretKey getSecretKey() {
        // 对密钥进行 Base64 编码（确保符合 HS256 算法要求）
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }
}