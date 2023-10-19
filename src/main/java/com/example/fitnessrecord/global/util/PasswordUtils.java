package com.example.fitnessrecord.global.util;


import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {

  public static boolean equalsPlainText(String pw1, String pw2) {
    if (!pw1.equals(pw2)) {
      return false;
    }
    return true;
  }

  public static boolean equalsPlainTextAndHashed(String plainText, String hashed) {
    if (plainText == null || plainText.length() < 1) {
      return false;
    }
    if (hashed == null || hashed.length() < 1) {
      return false;
    }

    return BCrypt.checkpw(plainText, hashed);
  }

  public static String encPassword(String plainText) {
    if (plainText == null || plainText.length() < 1) {
      return "";
    }
    return BCrypt.hashpw(plainText, BCrypt.gensalt());
  }
}
