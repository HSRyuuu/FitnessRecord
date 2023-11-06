package com.example.fitnessrecord.global.redis.xxx;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@Slf4j
//@Aspect
//@Component
@RequiredArgsConstructor
public class DistributedLockAop {

  private static final String REDISSON_LOCK_PREFIX = "LOCK:";

  private final RedissonClient redissonClient;
  private final AopForTransaction aopForTransaction;

  @Around("@annotation(com.example.fitnessrecord.global.redis.xxx.DistributedLock)")
  public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

    String key = REDISSON_LOCK_PREFIX +
        CustomSpringELParser.getDynamicValue(
            signature.getParameterNames(),
            joinPoint.getArgs(),
            distributedLock.key());
    log.info("lock on [method:{}] [key:{}]", method, key);

    RLock rLock = redissonClient.getLock(key);
    log.info("RLock: {}", rLock);

    try {
      boolean available =
          rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(),
              distributedLock.timeUnit());
      if (!available) {
        return false;
      }
      return aopForTransaction.proceed(joinPoint);

    } catch (InterruptedException e) {
      throw new InterruptedException();
    } finally {
      try {
        rLock.unlock();
      } catch (IllegalMonitorStateException e) {
        log.info("Redisson Local Already UnLock [serviceName:{}] [key:{}]", method.getName(), key);
      }
    }
  }

}
