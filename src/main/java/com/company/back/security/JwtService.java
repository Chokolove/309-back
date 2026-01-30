package com.company.back.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.company.back.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

  private final String secret = "VERY_LONG_RANDOM_SECRET_CHANGE_ME";

  public String generateToken(User user) {
    return Jwts.builder()
        .setSubject(user.getId().toString())
        .claim("email", user.getEmail())
        .setIssuedAt(new Date())
        .setExpiration(Date.from(
            Instant.now().plus(1, ChronoUnit.HOURS)
        ))
        .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
        .compact();
  }

  public UUID extractUserId(String token) {
    return UUID.fromString(
        Jwts.parserBuilder()
            .setSigningKey(secret.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject()
    );
  }
}
