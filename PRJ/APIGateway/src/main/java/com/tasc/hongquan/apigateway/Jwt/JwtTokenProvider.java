package com.tasc.hongquan.apigateway.Jwt;

import com.tasc.hongquan.apigateway.models.CustomUserDetails;
import com.tasc.hongquan.apigateway.services.TokenService;
import com.tasc.hongquan.apigateway.services.TokenServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private String JWT_SECRET = "3E817CFD882DD4866F287E42DBA1887E42DBA1887E42DBA18";
    private final long JWT_EXPIRATION = 3600* 24 * 1000;
    private final TokenService tokenService;

    public JwtTokenProvider(TokenServiceImpl tokenServiceImpl) {
        this.tokenService = tokenServiceImpl;
    }

    public String generateToken(CustomUserDetails userDetails) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("userId", userDetails.getUserId())
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
        return claims.get("role", String.class);
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET).build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", String.class);
    }

    public Date getExpirationDateFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET).build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    public boolean validateToken(String authToken) {
        try {
            // Phân tích và lấy claims từ token
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET).build()
                    .parseClaimsJws(authToken)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                System.err.println("Token is expired");

                return false;
            }
            return true;
        } catch (ExpiredJwtException ex) {
            // Token is expired
            System.err.println("Token is expired: " + ex.getMessage());

            return false;
        } catch (Exception ex) {
            System.err.println("Err : " + ex);
            return false;
        }
    }

}