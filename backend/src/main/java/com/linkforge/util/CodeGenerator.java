package com.linkforge.util;
import java.security.SecureRandom;
public class CodeGenerator {
  private static final String A = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final SecureRandom R = new SecureRandom();
  public static String generate(int len){
    var sb = new StringBuilder(len);
    for (int i=0;i<len;i++) sb.append(A.charAt(R.nextInt(A.length())));
    return sb.toString();
  }
}
