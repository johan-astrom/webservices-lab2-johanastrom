package com.johanastrom.gatewayservicejwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        String authToken = authentication.getCredentials().toString();
        try{
            var claims = jwtUtil.getAllClaimsFromToken(authToken);
            if (claims==null){
                return Mono.empty();
            }
            Date expires = claims.getBody().getExpiration();
            if (expires.before(new Date(System.currentTimeMillis()))){
                return Mono.empty();
            }
            ArrayList<String> permissions = (ArrayList<String>) claims.getBody().get("authorities");
            var authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return Mono.just(new UsernamePasswordAuthenticationToken(claims.getBody().getSubject(), null, authorities));
        }catch (Exception e){
            return Mono.empty();
        }

    }
}
