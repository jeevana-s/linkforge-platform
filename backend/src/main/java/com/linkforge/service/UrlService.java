package com.linkforge.service;
import com.linkforge.cache.UrlCache;
import com.linkforge.dto.UrlDtos.*;
import com.linkforge.entity.*;
import com.linkforge.exception.ApiException;
import com.linkforge.repository.UrlRepository;
import com.linkforge.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UrlService {
  private final UrlRepository repo; private final UrlCache cache; private final PasswordEncoder enc;
  @Value("${app.base-url}") private String baseUrl;
  public UrlService(UrlRepository r, UrlCache c, PasswordEncoder e){ this.repo=r; this.cache=c; this.enc=e; }

  public UrlResponse create(CreateUrlRequest req, User user){
    if (!req.originalUrl().matches("^https?://.+")) throw new ApiException(HttpStatus.BAD_REQUEST,"URL must start with http(s)://");
    String code = req.customAlias();
    if (code != null && !code.isBlank()){
      if (!code.matches("^[A-Za-z0-9_-]{3,32}$")) throw new ApiException(HttpStatus.BAD_REQUEST,"Invalid alias");
      if (repo.existsByShortCode(code)) throw new ApiException(HttpStatus.CONFLICT,"Alias taken");
    } else {
      do { code = CodeGenerator.generate(7); } while (repo.existsByShortCode(code));
    }
    Url url = Url.builder().originalUrl(req.originalUrl()).shortCode(code).user(user)
      .expiresAt(req.expiresAt())
      .passwordHash(req.password()!=null && !req.password().isBlank() ? enc.encode(req.password()) : null)
      .oneTime(Boolean.TRUE.equals(req.oneTime())).build();
    repo.save(url);
    if (url.getPasswordHash()==null && !url.isOneTime()) cache.put(code, url.getOriginalUrl());
    return toDto(url);
  }

  public Page<UrlResponse> list(User user, int page, int size){
    return repo.findByUser(user, PageRequest.of(page,size, Sort.by("createdAt").descending())).map(this::toDto);
  }

  public Url getOrThrow(Long id, User user){
    Url u = repo.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Not found"));
    if (!u.getUser().getId().equals(user.getId()) && user.getRole()!=User.Role.ADMIN)
      throw new ApiException(HttpStatus.FORBIDDEN,"Not yours");
    return u;
  }
  public void delete(Long id, User user){
    Url u = getOrThrow(id,user);
    cache.evict(u.getShortCode());
    repo.delete(u);
  }

  public UrlResponse toDto(Url u){
    return new UrlResponse(u.getId(), u.getOriginalUrl(), u.getShortCode(),
      baseUrl+"/"+u.getShortCode(), u.getExpiresAt(), u.isOneTime(), u.isBlocked(),
      u.getClickCount(), u.getCreatedAt());
  }
}
