package com.linkforge.dto;
import jakarta.validation.constraints.*;
import java.time.Instant;
public class UrlDtos {
  public record CreateUrlRequest(
    @NotBlank @Size(max=2048) String originalUrl,
    @Size(max=32) String customAlias,
    Instant expiresAt,
    String password,
    Boolean oneTime
  ){}
  public record UrlResponse(Long id, String originalUrl, String shortCode, String shortUrl,
                            Instant expiresAt, boolean oneTime, boolean blocked,
                            long clickCount, Instant createdAt){}
  public record VerifyPasswordRequest(@NotBlank String password){}
}
