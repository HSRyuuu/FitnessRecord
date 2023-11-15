package com.example.fitnessrecord.global.redis.token;

public interface TokenRepository {
  void addRefreshToken(String key, String value);
  String getRefreshToken(String key);
  boolean deleteRefreshToken(String key);
  void addBlackListAccessToken(String token);
  boolean existsBlackListAccessToken(String token);
}
