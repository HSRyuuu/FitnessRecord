package com.example.fitnessrecord.global.redis.views;

public interface ViewRecordRepository {

  void addViewRecord(Long userId, Long routinePostId);

  boolean existsViewRecord(Long userId, Long routinePostId);


}
