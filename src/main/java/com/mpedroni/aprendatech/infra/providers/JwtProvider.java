package com.mpedroni.aprendatech.infra.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtProvider {
    @Value("${application.security.jwt.secret}")
    private String secret;

    @Value("${application.security.jwt.expiration-in-minutes}")
    private int expirationInMinutes;

    public String generate(String username, String role) {
        var now = Instant.now();
        var expiresAt = now.plus(expirationInMinutes, ChronoUnit.MINUTES);

        return JWT.create()
                .withIssuer("aprendatech")
                .withClaim("username", username)
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(secret));
    }

    public String verify(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getSubject();
    }
}
