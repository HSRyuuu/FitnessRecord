package com.example.fitnessrecord.global.redis.views;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisViewsRepository implements ViewsRepository{

  private final RedissonClient redissonClient;
  private static final String ROUTINE_POST_VIEW_MAP = "RoutinePostViewMap";
  private static final String KEY_ROUTINE_POST_HEADER = "ROUTINEPOST";
  private static final String KEY_USER_HEADER = "USER";
  private static final Long expireTime = 60L * 30; //30분

  @Override
  public void addViewRecord(Long userId, Long routinePostId) {
    RMap<String, String> viewMap = redissonClient.getMap(ROUTINE_POST_VIEW_MAP);
    viewMap.put(getKeyString(userId, routinePostId), "view");
    viewMap.expire(expireTime, TimeUnit.SECONDS);
  }

  @Override
  public boolean existsViewRecord(Long userId, Long routinePostId) {
    RMap<String, String> viewMap = redissonClient.getMap(ROUTINE_POST_VIEW_MAP);
    return viewMap.containsKey(getKeyString(userId, routinePostId));
  }

  private String getKeyString(Long userId, Long routinePostId){
    return new StringBuilder()
        .append(KEY_USER_HEADER)
        .append(userId)
        .append(KEY_ROUTINE_POST_HEADER)
        .append(routinePostId)
        .toString();
  }
}
