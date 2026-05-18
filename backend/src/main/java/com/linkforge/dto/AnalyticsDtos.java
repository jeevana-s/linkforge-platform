package com.linkforge.dto;
import java.util.List;
import java.util.Map;
public class AnalyticsDtos {
  public record LinkAnalytics(long totalClicks, long uniqueClicks,
                              Map<String,Long> byBrowser, Map<String,Long> byCountry,
                              Map<String,Long> byDevice, List<TimeBucket> timeline){}
  public record TimeBucket(String date, long clicks){}
  public record Summary(long totalUrls, long totalClicks, long activeUrls, List<TopLink> topLinks){}
  public record TopLink(String shortCode, String originalUrl, long clicks){}
}
