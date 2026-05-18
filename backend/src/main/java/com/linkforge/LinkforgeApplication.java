package com.linkforge;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class LinkforgeApplication {
  public static void main(String[] args) { SpringApplication.run(LinkforgeApplication.class, args); }
}
