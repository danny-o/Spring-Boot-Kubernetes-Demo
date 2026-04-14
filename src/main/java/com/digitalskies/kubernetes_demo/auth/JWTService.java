package com.digitalskies.kubernetes_demo.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTService {

    @Value("${jwt.secret}")
    String jwtSecret;

    SecretKey secretKey;

    long ACCESS_TOKEN_EXPIRY = 15 * 60 * 1000L;

    long REFRESH_TOKEN_EXPIRY = 30 * 24* 60 * 60 * 1000L;



    @PostConstruct
    void init(){
        this.secretKey= Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
    }

    String generateToken(
       String userId,
       String type,
       long expiry
    ){
        var now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userId)
                .claim("type",type)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiry))
                .signWith(secretKey)
                .compact();
    }

    public String generateAccessToken(String userId){
        return generateToken(userId,"access",ACCESS_TOKEN_EXPIRY);

    }

    public String generateRefreshToken(String userId){
        return generateToken(userId,"refresh",REFRESH_TOKEN_EXPIRY);
    }

    public Boolean validateAccessToken(String rawToken){

        var claims=parseJWTToken(rawToken);

        if(claims==null){
            return false;
        }

        var tokenType = claims.get("type");

        if(tokenType==null){
            return false;
        }

        return ((String)tokenType).equalsIgnoreCase("access");

    }

    public Boolean validateRefreshToken(String rawToken){

        var claims=parseJWTToken(rawToken);

        if(claims==null){
            return false;
        }

        var tokenType = claims.get("type");

        if(tokenType==null){
            return false;
        }

        return ((String)tokenType).equalsIgnoreCase("refresh");

    }

    public String getUsernameFromToken(String token){
        var claims=parseJWTToken(token);

        if(claims==null){
            throw new IllegalArgumentException("Invalid token");
        }
        return claims.getSubject();
    }

    private Claims parseJWTToken(String token){
        String rawToken = token.replaceFirst("^Bearer ","");

        try {
            return Jwts.parser().verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(rawToken)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }

    }

}
