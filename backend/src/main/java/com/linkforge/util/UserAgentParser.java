package com.linkforge.util;
public class UserAgentParser {
  public static String browser(String ua){
    if (ua==null) return "Unknown";
    String s = ua.toLowerCase();
    if (s.contains("edg/")) return "Edge";
    if (s.contains("chrome")) return "Chrome";
    if (s.contains("firefox")) return "Firefox";
    if (s.contains("safari")) return "Safari";
    if (s.contains("opera") || s.contains("opr/")) return "Opera";
    return "Other";
  }
  public static String device(String ua){
    if (ua==null) return "Unknown";
    String s = ua.toLowerCase();
    if (s.contains("mobile") || s.contains("android") || s.contains("iphone")) return "Mobile";
    if (s.contains("tablet") || s.contains("ipad")) return "Tablet";
    return "Desktop";
  }
}
