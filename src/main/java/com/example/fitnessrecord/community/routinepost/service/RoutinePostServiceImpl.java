package com.example.fitnessrecord.community.routinepost.service;

import com.example.fitnessrecord.community.routinepost.dto.AddRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.dto.DeleteRoutinePostResult;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostDto;
import com.example.fitnessrecord.community.routinepost.dto.RoutinePostResult;
import com.example.fitnessrecord.community.routinepost.dto.UpdateRoutinePostInput;
import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
import com.example.fitnessrecord.community.routinepost.persist.RoutinePostRepository;
import com.example.fitnessrecord.domain.routine.element.dto.RoutineElementDto;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElement;
import com.example.fitnessrecord.domain.routine.element.persist.RoutineElementRepository;
import com.example.fitnessrecord.domain.routine.routine.dto.RoutineDto;
import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import com.example.fitnessrecord.domain.routine.routine.persist.RoutineRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import com.example.fitnessrecord.global.redis.lock.DistributedLock;
import com.example.fitnessrecord.global.redis.views.ViewRecordRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RoutinePostServiceImpl implements RoutinePostService {

  private final RoutinePostRepository routinePostRepository;
  private final RoutineRepository routineRepository;
  private final RoutineElementRepository routineElementRepository;
  private final ViewRecordRepository viewRecordRepository;
  private final UserRepository userRepository;

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
  public RoutinePostDto getRoutinePost(Long id, Long userId) {
    RoutinePost routinePost = routinePostRepository.findById(id)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND));

    RoutinePost saved = routinePostRepository.save(routinePost);

    return RoutinePostDto.fromEntity(
        saved, this.getRoutineElementDtoList(routinePost.getRoutine().getId()));
  }

  @Override
  @DistributedLock(key = "T(java.lang.String).format('RoutinePost%d', #routinePostId)")
  public boolean addView(Long userId, Long routinePostId) {
    RoutinePost routinePost = routinePostRepository.findById(routinePostId)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND));

    if (!viewRecordRepository.existsViewRecord(userId, routinePostId)) {
      viewRecordRepository.addViewRecord(userId, routinePostId);
      routinePost.addViews();
      return true;
    }

    return false;
  }

  @Override
  @DistributedLock(key = "T(java.lang.String).format('Likes%d', #routinePostId)")
  public void addLikes(Long routinePostId) {
    RoutinePost routinePost = routinePostRepository.findById(routinePostId)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND));
    routinePost.addLikes();
    routinePostRepository.save(routinePost);
  }

  @Override
  @DistributedLock(key = "T(java.lang.String).format('Likes%d', #routinePostId)")
  public void cancelLikes(Long routinePostId) {
    RoutinePost routinePost = routinePostRepository.findById(routinePostId)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND));
    routinePost.cancelLikes();
    routinePostRepository.save(routinePost);
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

  @Override
  public DeleteRoutinePostResult deleteRoutinePost(Long id, Long userId) {
    RoutinePost routinePost = routinePostRepository.findById(id)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND));

    this.validateRoutineAuthority(routinePost.getUser(), userId);

    DeleteRoutinePostResult result =
        DeleteRoutinePostResult.fromEntity(routinePost);

    routinePostRepository.delete(routinePost);

    return result;
  }

  @Override
  public RoutineDto quoteAndSaveRoutine(Long routinePostId, Long userId) {
    RoutinePost routinePost = routinePostRepository.findById(routinePostId)
        .orElseThrow(() -> new MyException(ErrorCode.ROUTINE_POST_NOT_FOUND));
    Routine routine = routinePost.getRoutine();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));

    Routine myRoutine = routineRepository.save(new Routine(routine, user));
    routineElementRepository.saveAll(this.duplicateRoutineElements(routine.getList(), myRoutine));

    return RoutineDto.fromEntity(myRoutine, this.getRoutineElementDtoList(myRoutine.getId()));
  }

  private List<RoutineElement> duplicateRoutineElements(
      List<RoutineElement> list, Routine routine) {
    List<RoutineElement> newList = new ArrayList<>();
    for (RoutineElement routineElement : list) {
      newList.add(
          RoutineElement.builder()
              .routine(routine)
              .orderNumber(routineElement.getOrderNumber())
              .trainingName(routineElement.getTrainingName())
              .bodyPart(routineElement.getBodyPart())
              .reps(routineElement.getReps())
              .build());
    }
    return newList;
  }


  private List<RoutineElementDto> getRoutineElementDtoList(Long routineId) {
    List<RoutineElement> findList =
        routineElementRepository.findAllByRoutineIdOrderByOrderNumber(routineId);

    return findList.stream()
        .map(RoutineElementDto::fromEntity)
        .collect(Collectors.toList());
  }

  private void validateRoutineAuthority(User user, Long userId) {
    if (!user.getId().equals(userId)) {
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }
  }
}
