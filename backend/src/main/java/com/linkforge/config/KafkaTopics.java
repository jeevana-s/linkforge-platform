package com.linkforge.config;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.*;
@Configuration
public class KafkaTopics {
  public static final String CLICKS = "linkforge.clicks";
  @Bean NewTopic clicks(){ return new NewTopic(CLICKS, 3, (short)1); }
}
