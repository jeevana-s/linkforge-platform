package com.linkforge.service;
import com.linkforge.dto.AuthDtos.*;
import com.linkforge.entity.User;
import com.linkforge.exception.ApiException;
import com.linkforge.repository.UserRepository;
import com.linkforge.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository users; private final PasswordEncoder enc; private final JwtService jwt;
  public AuthService(UserRepository u, PasswordEncoder e, JwtService j){ this.users=u; this.enc=e; this.jwt=j; }

  public TokenResponse register(RegisterRequest r){
    if (users.existsByEmail(r.email())) throw new ApiException(HttpStatus.CONFLICT,"Email already registered");
    User u = User.builder().email(r.email()).password(enc.encode(r.password())).name(r.name()).build();
    users.save(u);
    return tokens(u);
  }
  public TokenResponse login(LoginRequest r){
    User u = users.findByEmail(r.email()).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED,"Invalid credentials"));
    if (!enc.matches(r.password(), u.getPassword())) throw new ApiException(HttpStatus.UNAUTHORIZED,"Invalid credentials");
    return tokens(u);
  }
  public TokenResponse refresh(String refreshToken){
    try {
      var claims = jwt.parse(refreshToken).getPayload();
      if (!"refresh".equals(claims.get("type"))) throw new ApiException(HttpStatus.UNAUTHORIZED,"Invalid token");
      User u = users.findByEmail(claims.getSubject()).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED,"User not found"));
      return tokens(u);
    } catch (ApiException e){ throw e; }
    catch (Exception e){ throw new ApiException(HttpStatus.UNAUTHORIZED,"Invalid refresh token"); }
  }
  private TokenResponse tokens(User u){
    return new TokenResponse(jwt.generateAccess(u.getEmail(), u.getRole().name()),
      jwt.generateRefresh(u.getEmail()), u.getEmail(), u.getRole().name());
  }
}
