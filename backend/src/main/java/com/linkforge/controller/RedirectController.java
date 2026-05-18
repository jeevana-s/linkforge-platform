package com.linkforge.controller;
import com.linkforge.service.RedirectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
public class RedirectController {
  private final RedirectService svc;
  public RedirectController(RedirectService s){ this.svc=s; }

  @GetMapping("/{code:[A-Za-z0-9_-]{3,32}}")
  public ResponseEntity<Void> redirect(@PathVariable String code,
      @RequestParam(required=false) String password, HttpServletRequest req){
    String target = svc.resolveAndTrack(code, password, req);
    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(target)).build();
  }
}
