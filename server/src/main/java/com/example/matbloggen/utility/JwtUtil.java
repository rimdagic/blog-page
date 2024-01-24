package com.example.matbloggen.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    String variableName = "SECRET_KEY";
    String secretKey = System.getenv(variableName);

    public String createToken(String subject, String email) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .withClaim("email", email)
                .sign(algorithm);
    }

    public String extractUserId(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);

        return decodedJWT.getSubject();
    }
}