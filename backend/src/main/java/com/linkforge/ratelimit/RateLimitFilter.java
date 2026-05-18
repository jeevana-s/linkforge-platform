package com.linkforge.ratelimit;
import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component @Order(1)
public class RateLimitFilter implements Filter {
  @Value("${app.ratelimit.capacity}") long capacity;
  @Value("${app.ratelimit.refill-tokens}") long refill;
  @Value("${app.ratelimit.refill-period-seconds}") long period;
  private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

  private Bucket bucket(String ip){
    return buckets.computeIfAbsent(ip, k -> Bucket.builder()
      .addLimit(Bandwidth.builder().capacity(capacity)
        .refillGreedy(refill, Duration.ofSeconds(period)).build())
      .build());
  }
  @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest r = (HttpServletRequest) req;
    String ip = r.getHeader("X-Forwarded-For");
    if (ip == null) ip = r.getRemoteAddr();
    if (!bucket(ip).tryConsume(1)) {
      HttpServletResponse hr = (HttpServletResponse) res;
      hr.setStatus(429);
      hr.getWriter().write("{\"error\":\"Too many requests\"}");
      return;
    }
    chain.doFilter(req, res);
  }
}
