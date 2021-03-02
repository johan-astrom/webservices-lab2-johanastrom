package com.johanastrom.authenticationservice;

import com.johanastrom.authenticationservice.common.JwtConfig;
import com.johanastrom.authenticationservice.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserCredentials userCredentials){
        if (userRepository.findByUsername(userCredentials.getUserName())!=null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username " + userCredentials.getUserName() + " already exists.");
        }
        User user = new User();
        user.setUsername(userCredentials.getUserName());
        user.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
        user.setRoles("USER");
        userRepository.save(user);
    }

    @PostMapping("/auth")
    public TokenResponse authenticate(@RequestBody UserCredentials userCredentials){

        User user = userRepository.findByUsername(userCredentials.getUserName());
        if (user==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user");
        }

        String credentialsPassword = userCredentials.getPassword();
        String persistedPassword = user.getPassword();

        if (passwordEncoder.matches(credentialsPassword, persistedPassword)){
            List<GrantedAuthority> grantedAuthorities =
                    Arrays.stream(user.getRoles().split(","))
                    .map(s -> "ROLE_" + s)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            long now = System.currentTimeMillis();
            String token = Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(now))
                    .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000L) )
                    .claim("authorities", grantedAuthorities.stream()
                            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                    .compact();

            var tokenInfo = new TokenResponse();
            tokenInfo.access_token = token;
            tokenInfo.token_type = jwtConfig.getPrefix();
            tokenInfo.expires_in = jwtConfig.getExpiration();
            return tokenInfo;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user credentials");
    }
}
