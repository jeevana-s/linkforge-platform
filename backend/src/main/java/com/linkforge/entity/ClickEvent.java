package com.linkforge.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="click_events", indexes={
  @Index(name="idx_click_url_time", columnList="url_id,clickedAt"),
  @Index(name="idx_click_country", columnList="country")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClickEvent {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="url_id", nullable=false) private Url url;
  private String ip;
  private String country;
  private String browser;
  private String device;
  private String referer;
  @Column(nullable=false) private Instant clickedAt = Instant.now();
}
