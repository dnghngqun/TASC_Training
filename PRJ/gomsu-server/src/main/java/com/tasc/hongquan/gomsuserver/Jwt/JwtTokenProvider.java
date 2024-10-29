package com.tasc.hongquan.gomsuserver.Jwt;

import com.tasc.hongquan.gomsuserver.models.CustomUserDetails;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private String JWT_SECRET = "3E817CFD882DD4866F287E42DBA1887E42DBA1887E42DBA18";
    private final long JWT_EXPIRATION = 3600000L;// 1h

    public String generateToken(CustomUserDetails userDetails) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().stream()
                        .findFirst()
                        .map(GrantedAuthority::getAuthority)
                        .orElse(null))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET).build()
                .parseClaimsJws(token)
                .getPayload();
        return claims.getSubject();
    }

    public String getRoleFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET).build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class); // Lấy vai trò từ claims
    }


    public boolean validateToken(String authToken) {
        try {
            // Phân tích và lấy claims từ token
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET).build()
                    .parseClaimsJws(authToken)
                    .getBody();

            // Kiểm tra xem token đã hết hạn hay chưa
            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                // Token đã hết hạn
                return false;
            }

            return true; // Token hợp lệ
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            // Token đã hết hạn
            System.err.println("Token đã hết hạn: " + ex.getMessage());
            return false;
        } catch (io.jsonwebtoken.SignatureException ex) {
            // Chữ ký không hợp lệ
            System.err.println("Chữ ký không hợp lệ: " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            // Lỗi chung
            System.err.println("Lỗi khi xác thực token: " + ex.getMessage());
            return false;
        }
    }

}
