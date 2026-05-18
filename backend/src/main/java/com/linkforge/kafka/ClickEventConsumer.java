package com.linkforge.kafka;
import com.linkforge.config.KafkaTopics;
import com.linkforge.entity.ClickEvent;
import com.linkforge.repository.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ClickEventConsumer {
  private final UrlRepository urls; private final ClickEventRepository clicks;
  public ClickEventConsumer(UrlRepository u, ClickEventRepository c){ this.urls=u; this.clicks=c; }

  @KafkaListener(topics = KafkaTopics.CLICKS, groupId = "linkforge-analytics")
  public void onClick(ClickEventDto e){
    urls.findById(e.urlId).ifPresent(url -> {
      ClickEvent ev = ClickEvent.builder().url(url).ip(e.ip).country(e.country)
        .browser(e.browser).device(e.device).referer(e.referer).build();
      clicks.save(ev);
      url.setClickCount(url.getClickCount()+1);
      urls.save(url);
    });
  }
}
