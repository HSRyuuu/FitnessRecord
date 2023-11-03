package com.example.fitnessrecord.domain.routine.routine.service;

import com.example.fitnessrecord.domain.routine.routine.dto.AddRoutineResult;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.routine.routine.persist.RoutineRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoutineServiceImpl implements RoutineService {

  private final UserRepository userRepository;
  private final RoutineRepository routineRepository;

  @Override
  public AddRoutineResult addRoutine(Long userId, String routineName) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));

    Routine saved = routineRepository.save(Routine.builder()
        .user(user)
        .routineName(routineName)
        .lastModifiedTime(LocalDateTime.now())
        .build());

    return AddRoutineResult.fromEntity(saved);
  }
}
