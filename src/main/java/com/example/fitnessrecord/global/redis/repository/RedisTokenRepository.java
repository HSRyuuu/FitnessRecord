package com.example.fitnessrecord.global.redis.repository;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTokenRepository {

  private final RedissonClient redissonClient;
  private static final String REFRESH_TOKEN_MAP = "RefreshTokenMap";


  public void addToken(String key, String value) {
    RMap<String, String> tokenMap = redissonClient.getMap(REFRESH_TOKEN_MAP);
    tokenMap.put(key, value);
  }

  public String getToken(String key) {
    RMap<String, String> tokenMap = redissonClient.getMap(REFRESH_TOKEN_MAP);
    return tokenMap.get(key);
  }

//  public void deleteToken(String key) {
//    RMap<String, String> tokenMap = redissonClient.getMap(REFRESH_TOKEN_MAP);
//    tokenMap.remove(key);
//  }
}
