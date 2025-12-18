package Hongik_SafeMap_Server.util;

import Hongik_SafeMap_Server.global.properties.JwtProperties;
import Hongik_SafeMap_Server.vo.MemberStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenUtil {

    private final JwtProperties jwtProperties;

    // Access Token 생성
    public String generateAccessToken(String email, String name, MemberStatus status) {
        return createToken(email, name, status, JwtProperties.ACCESS_TOKEN_VALIDITY_TIME);
    }

    // Refresh Token 생성
    public String generateRefreshToken(String email, String name, MemberStatus status) {
        return createToken(email, name, status, JwtProperties.REFRESH_TOKEN_VALIDITY_TIME);
    }

    // 토큰 생성 내부 로직
    private String createToken(String email, String name, MemberStatus status, long validity) {
        Claims claims = Jwts.claims().setSubject(email); // Subject = Email
        claims.put("name", name);
        claims.put("status", status.name()); // ✅ enum -> String

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    // 토큰 유효성 검사 (만료 여부 등)
    public boolean validateToken(String token) {
        try {
            token = removePrefix(token);
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 이메일 추출
    public String getEmailFromToken(String token) {
        token = removePrefix(token);
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 상태 추출
    public String getStatusFromToken(String token) {
        token = removePrefix(token);
        Object status = Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .get("status");

        return status == null ? null : status.toString();
    }

    private String removePrefix(String token) {
        if (token != null && token.startsWith(JwtProperties.PREFIX)) {
            return token.substring(JwtProperties.PREFIX.length());
        }
        return token;
    }
}
