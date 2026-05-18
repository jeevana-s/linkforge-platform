package com.linkforge.controller;
import com.linkforge.dto.AuthDtos.*;
import com.linkforge.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
  private final AuthService svc;
  public AuthController(AuthService s){ this.svc=s; }
  @PostMapping("/register") public TokenResponse register(@Valid @RequestBody RegisterRequest r){ return svc.register(r); }
  @PostMapping("/login") public TokenResponse login(@Valid @RequestBody LoginRequest r){ return svc.login(r); }
  @PostMapping("/refresh") public TokenResponse refresh(@Valid @RequestBody RefreshRequest r){ return svc.refresh(r.refreshToken()); }
}
