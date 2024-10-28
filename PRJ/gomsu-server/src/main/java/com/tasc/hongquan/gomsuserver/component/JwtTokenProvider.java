package com.tasc.hongquan.gomsuserver.component;

import com.tasc.hongquan.gomsuserver.DTO.UserDetailCustom;
import com.tasc.hongquan.gomsuserver.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final String JWT_SECRET = "f7b8d67d81d267c18913f3d309a16d8329f346fc42a3607b8b93e2a1af10036b247990a7f3055a6cf71c11b69ab91c763609bca4ee240786b23a123ef75db7fccdf30f35f75c1edefea4c965c3b56d988adcb8facb63be67af34fb660f09643a35b5ff7661ffc40d43026b601b85593bb24036dc4f01625a1a40e0a34b2371fe5ffe0bca14e1c6edaaa8d5156b814035302d8e3bbdb8321c9af32801b872c359059d81a373d2e30c223751d20dbc218ce600b01e6611b6cad485b16f0a90ff88a7c6f7e50cfb9bc88ec05b9f039a1afa78071be4abf1620afb9dbab268662f11cbc036fb58bc3b1a50c99782f5ef76bde23688f8805413b06d903571c02e249d";
    private final long JWT_EXPIRATION = 3600L; // 1h

    public String generatorToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    public UUID getUUIDFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.getSubject());
    }
}
