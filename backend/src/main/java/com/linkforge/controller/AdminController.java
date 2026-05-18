package com.linkforge.controller;
import com.linkforge.entity.*;
import com.linkforge.repository.*;
import com.linkforge.cache.UrlCache;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/admin")
public class AdminController {
  private final UserRepository users; private final UrlRepository urls; private final UrlCache cache;
  public AdminController(UserRepository u, UrlRepository ur, UrlCache c){ this.users=u; this.urls=ur; this.cache=c; }

  @GetMapping("/users")
  public Page<User> listUsers(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size){
    return users.findAll(PageRequest.of(page,size));
  }
  @PostMapping("/users/{id}/disable")
  public void disable(@PathVariable Long id){ users.findById(id).ifPresent(u -> { u.setEnabled(false); users.save(u); }); }

  @GetMapping("/urls")
  public Page<Url> listUrls(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size){
    return urls.findAll(PageRequest.of(page,size));
  }
  @PostMapping("/urls/{id}/block")
  public void block(@PathVariable Long id){
    urls.findById(id).ifPresent(u -> { u.setBlocked(true); urls.save(u); cache.evict(u.getShortCode()); });
  }

  @GetMapping("/stats")
  public Map<String,Object> stats(){
    long totalUrls = urls.count();
    long totalUsers = users.count();
    return Map.of("totalUrls", totalUrls, "totalUsers", totalUsers);
  }
}
