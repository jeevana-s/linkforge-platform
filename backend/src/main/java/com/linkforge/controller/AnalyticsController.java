package com.linkforge.controller;
import com.linkforge.dto.AnalyticsDtos.*;
import com.linkforge.entity.User;
import com.linkforge.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/analytics")
public class AnalyticsController {
  private final AnalyticsService analytics; private final UrlService urls;
  public AnalyticsController(AnalyticsService a, UrlService u){ this.analytics=a; this.urls=u; }

  @GetMapping("/{id}")
  public LinkAnalytics forLink(@PathVariable Long id, @AuthenticationPrincipal User user){
    urls.getOrThrow(id, user);
    return analytics.forUrl(id);
  }
  @GetMapping("/summary")
  public Summary summary(@AuthenticationPrincipal User user){ return analytics.summary(user); }
}
