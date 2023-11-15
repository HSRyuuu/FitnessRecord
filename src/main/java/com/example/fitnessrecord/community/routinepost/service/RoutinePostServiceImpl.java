package com.example.fitnessrecord.community.routinepost.service;

import com.example.fitnessrecord.community.routinepost.dto.AddRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostResult;
import com.example.fitnessrecord.community.routinepost.dto.UpdateRoutinePostInput;
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
import com.example.fitnessrecord.global.redis.redisson.lock.DistributedLock;
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
  public RoutinePostResult addRoutinePost(AddRoutinePostInput input, Long userId) {
    Routine routine = routineRepository.findById(input.getRoutineId())
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_NOT_FOUND));
    User user = routine.getUser();

    this.validateRoutineAuthority(user, userId);

    RoutinePost routinePost = AddRoutinePostInput.toEntity(input, user, routine);

    RoutinePost saved = routinePostRepository.save(routinePost);

    return RoutinePostResult.fromEntity(saved, this.getRoutineElementDtoList(routine.getId()));
  }

  @Override
  @DistributedLock(key = "T(java.lang.String).format('RoutinePost%d', #id)")
  public RoutinePostDto getRoutinePost(Long id) {
    RoutinePost routinePost = routinePostRepository.findById(id)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND));
    routinePost.addViews();

    RoutinePost saved = routinePostRepository.save(routinePost);

    return RoutinePostDto.fromEntity(
        saved, this.getRoutineElementDtoList(routinePost.getRoutine().getId()));
  }

  @Override
  public RoutinePostResult updateRoutinePost(Long id, UpdateRoutinePostInput input, Long userId) {
    RoutinePost routinePost = routinePostRepository.findById(id)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND));

    this.validateRoutineAuthority(routinePost.getUser(), userId);

    routinePost.update(input.getTitle(), input.getContent());
    RoutinePost saved = routinePostRepository.save(routinePost);

    return RoutinePostResult.fromEntity(
        saved, this.getRoutineElementDtoList(routinePost.getRoutine().getId()));
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
