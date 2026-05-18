package com.linkforge.kafka;

import com.linkforge.entity.ClickEvent;
import com.linkforge.repository.ClickEventRepository;
import com.linkforge.repository.UrlRepository;
import org.springframework.stereotype.Component;

@Component
public class ClickEventProducer {

  private final UrlRepository urls;
  private final ClickEventRepository clicks;

  public ClickEventProducer(UrlRepository u, ClickEventRepository c) {
    this.urls = u;
    this.clicks = c;
  }

  public void publish(ClickEventDto e) {

    urls.findById(e.urlId).ifPresent(url -> {

      ClickEvent ev = ClickEvent.builder()
          .url(url)
          .ip(e.ip)
          .country(e.country)
          .browser(e.browser)
          .device(e.device)
          .referer(e.referer)
          .build();

      clicks.save(ev);

      url.setClickCount(url.getClickCount() + 1);

      urls.save(url);
    });
  }
}
