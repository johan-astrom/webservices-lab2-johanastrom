package com.johanastrom.gatewayservicejwt.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {
    public Jws<Claims> getAllClaimsFromToken(String authToken){
        return Jwts.parser()
                .setSigningKey("32-chars-long-secret-pass-string".getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(authToken);
    }
}
