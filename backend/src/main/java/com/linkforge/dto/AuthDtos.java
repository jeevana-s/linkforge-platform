package com.linkforge.dto;
import jakarta.validation.constraints.*;
public class AuthDtos {
  public record RegisterRequest(@Email @NotBlank String email, @NotBlank @Size(min=6) String password, String name){}
  public record LoginRequest(@Email @NotBlank String email, @NotBlank String password){}
  public record RefreshRequest(@NotBlank String refreshToken){}
  public record TokenResponse(String accessToken, String refreshToken, String email, String role){}
}
