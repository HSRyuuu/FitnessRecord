package com.example.fitnessrecord.global.redis.repository;

public interface TokenRepository {
  void addRefreshToken(String key, String value);
  String getRefreshToken(String key);
  boolean deleteRefreshToken(String key);
  void addBlackListAccessToken(String token);
  boolean existsBlackListAccessToken(String token);
}
