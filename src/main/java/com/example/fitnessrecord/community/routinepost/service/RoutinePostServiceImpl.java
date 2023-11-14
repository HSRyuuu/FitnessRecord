package com.example.fitnessrecord.community.routinepost.service;

import com.example.fitnessrecord.community.routinepost.dto.AddRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
import com.example.fitnessrecord.community.routinepost.persist.RoutinePostRepository;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElementRepository;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.routine.routine.persist.RoutineRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoutinePostServiceImpl implements RoutinePostService {

  private final RoutinePostRepository routinePostRepository;
  private final RoutineRepository routineRepository;
  private final RoutineElementRepository routineElementRepository;

  @Override
  public RoutinePostDto addRoutinePost(AddRoutinePostInput input, Long userId) {
    Routine routine = routineRepository.findById(input.getRoutineId())
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_NOT_FOUND));
    User user = routine.getUser();

    this.validateRoutineAuthority(user, userId);

    RoutinePost routinePost = AddRoutinePostInput.toEntity(input, user, routine);

    RoutinePost saved = routinePostRepository.save(routinePost);

    return RoutinePostDto.fromEntity(saved, getRoutineElementDtoList(routine.getId()));
  }

  private List<RoutineElementDto> getRoutineElementDtoList(Long routineId){
    List<RoutineElement> findList =
        routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routineId);

    return findList.stream()
        .map(RoutineElementDto::fromEntity)
        .collect(Collectors.toList());
  }

  private void validateRoutineAuthority(User user, Long userId){
    if(!user.getId().equals(userId)){
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }
  }
}
