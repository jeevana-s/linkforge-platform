package com.linkforge.cache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class UrlCache {
  private static final Duration TTL = Duration.ofHours(1);
  private final StringRedisTemplate redis;
  public UrlCache(StringRedisTemplate r){ this.redis = r; }
  private String k(String code){ return "url:"+code; }
  public String get(String code){ return redis.opsForValue().get(k(code)); }
  public void put(String code, String original){ redis.opsForValue().set(k(code), original, TTL); }
  public void evict(String code){ redis.delete(k(code)); }
}
