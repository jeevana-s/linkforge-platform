package com.linkforge.controller;
import com.linkforge.dto.UrlDtos.*;
import com.linkforge.entity.User;
import com.linkforge.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/urls")
public class UrlController {
  private final UrlService urls; private final QrService qr;
  public UrlController(UrlService u, QrService q){ this.urls=u; this.qr=q; }

  @PostMapping
  public UrlResponse create(@Valid @RequestBody CreateUrlRequest req, @AuthenticationPrincipal User user){
    return urls.create(req, user);
  }
  @GetMapping
  public Page<UrlResponse> list(@AuthenticationPrincipal User user,
      @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size){
    return urls.list(user, page, size);
  }
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id, @AuthenticationPrincipal User user){ urls.delete(id, user); }

  @GetMapping("/{id}/qr")
  public ResponseEntity<byte[]> qr(@PathVariable Long id, @AuthenticationPrincipal User user) throws Exception {
    var url = urls.getOrThrow(id, user);
    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
      .body(qr.png(urls.toDto(url).shortUrl(), 320));
  }
}
