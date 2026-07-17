package com.barjor.todo_api.todoapp.security;

import com.barjor.todo_api.todoapp.user.application.UserService;
import com.barjor.todo_api.todoapp.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/authenticate")
public class AuthenticationController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtConfigurationProperties properties;

    public AuthenticationController(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder,
            JwtConfigurationProperties properties
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.properties = properties;
    }

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        return userService.getUserByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .map(user -> {
                    String token = generateToken(user);
                    return ResponseEntity.ok(new AuthenticationResponse(
                            token,
                            user.getId(),
                            user.getName(),
                            user.getEmail()
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private String generateToken(User user) {
        Instant now = Instant.now();
        long expiry = properties.getExpirationSeconds();

        org.springframework.security.oauth2.jwt.JwsHeader jwsHeader = 
                org.springframework.security.oauth2.jwt.JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getEmail())
                .claim("userId", user.getId().toString())
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public record AuthenticationRequest(String email, String password) {}

    public record AuthenticationResponse(
            String token,
            UUID userId,
            String name,
            String email
    ) {}
}
