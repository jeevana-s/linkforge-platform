package com.linkforge.kafka;
import java.io.Serializable;
public class ClickEventDto implements Serializable {
  public Long urlId; public String ip, country, browser, device, referer;
  public ClickEventDto(){}
  public ClickEventDto(Long id, String ip, String c, String b, String d, String r){
    this.urlId=id; this.ip=ip; this.country=c; this.browser=b; this.device=d; this.referer=r;
  }
}
