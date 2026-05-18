package com.linkforge.kafka;
import com.linkforge.config.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
@Component
public class ClickEventProducer {
  private final KafkaTemplate<String,Object> kafka;
  public ClickEventProducer(KafkaTemplate<String,Object> k){ this.kafka=k; }
  public void publish(ClickEventDto e){ kafka.send(KafkaTopics.CLICKS, String.valueOf(e.urlId), e); }
}
