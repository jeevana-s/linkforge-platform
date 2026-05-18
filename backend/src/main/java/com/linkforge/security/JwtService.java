package com.linkforge.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
  @Value("${app.jwt.secret}") private String secret;
  @Value("${app.jwt.access-expiration-ms}") private long accessExp;
  @Value("${app.jwt.refresh-expiration-ms}") private long refreshExp;

  private SecretKey key(){ return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); }

  public String generateAccess(String subject, String role){
    return Jwts.builder().subject(subject).claims(Map.of("role",role,"type","access"))
      .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+accessExp))
      .signWith(key()).compact();
  }
  public String generateRefresh(String subject){
    return Jwts.builder().subject(subject).claims(Map.of("type","refresh"))
      .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+refreshExp))
      .signWith(key()).compact();
  }
  public Jws<Claims> parse(String token){
    return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
  }
  public String subject(String token){ return parse(token).getPayload().getSubject(); }
}
