package com.example.fitnessrecord.global.redis.redisson;

import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedissonLockService {

  private final RedissonClient redissonClient;

  private static final long waitTime = 5L;
  private static final long leaseTime = 3L;
  private static final TimeUnit timeUnit = TimeUnit.SECONDS;

  public RLock getLock(String lockName){
    RLock rLock = redissonClient.getLock(lockName);

    try {
      boolean available = rLock.tryLock(waitTime, leaseTime, timeUnit);
      if(!available){
        throw new MyException(ErrorCode.LOCK_NOT_AVAILABLE);
      }
      log.info("LOCK is available rLock:{} ",rLock.getName());
      return rLock;
    }catch (InterruptedException e){//락을 얻으려고 시도하다가 인터럽트를 받았을 때 발생
      throw new MyException(ErrorCode.LOCK_INTERRUPTED_ERROR);
    }
  }

  public void unLock(RLock rLock){
    try{
      rLock.unlock();
      log.info("unlock complete: {}", rLock.getName());
    }catch (IllegalMonitorStateException e){//락이 이미 종료되었을때? 발생
      throw new MyException(ErrorCode.UNLOCKING_A_LOCK_WHICH_IS_NOT_LOCKED);
    }
  }

}
