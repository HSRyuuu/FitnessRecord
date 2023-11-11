package com.example.fitnessrecord.global.redis.repository;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTokenRepository implements TokenRepository {

  private final RedissonClient redissonClient;
  private static final String REFRESH_TOKEN_MAP = "RefreshTokenMap";
  private static final String ACCESS_TOKEN_DENIED_MAP = "AccessTokenDeniedMap";


  @Override
  public void addRefreshToken(String key, String value) {
    RMap<String, String> tokenMap = redissonClient.getMap(REFRESH_TOKEN_MAP);
    tokenMap.put(key, value);
  }

  @Override
  public String getRefreshToken(String key) {
    RMap<String, String> tokenMap = redissonClient.getMap(REFRESH_TOKEN_MAP);
    return tokenMap.get(key);
  }

  @Override
  public boolean deleteRefreshToken(String key) {
    RMap<String, String> tokenMap = redissonClient.getMap(REFRESH_TOKEN_MAP);
    boolean result = tokenMap.containsKey(key);
    tokenMap.remove(key);

    return result;
  }

  @Override
  public void addBlackListAccessToken(String token){
    RMap<String, String> map = redissonClient.getMap(ACCESS_TOKEN_DENIED_MAP);
    map.put(token, "logged out");
  }

  @Override
  public boolean existsBlackListAccessToken(String token){
    RMap<String, String> map = redissonClient.getMap(ACCESS_TOKEN_DENIED_MAP);
    return map.containsKey(token);
  }
}
