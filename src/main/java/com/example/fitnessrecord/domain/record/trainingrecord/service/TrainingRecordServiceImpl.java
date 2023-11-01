package com.example.fitnessrecord.domain.record.trainingrecord.service;

import com.example.fitnessrecord.domain.record.setrecord.dto.SetRecordDto;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecordRepository;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordDto;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordListResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.dto.TrainingRecordResponse;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecordRepository;
import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import com.example.fitnessrecord.global.util.PageConstant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingRecordServiceImpl implements TrainingRecordService {

  private final TrainingRecordRepository trainingRecordRepository;
  private final SetRecordRepository setRecordRepository;
  private final UserRepository userRepository;

  @Override
  public TrainingRecordDto addTrainingRecord(Long userId, LocalDate date) {
    if (Objects.isNull(date)) {
      date = LocalDate.now();
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));

    TrainingRecord saved =
        trainingRecordRepository.save(TrainingRecord.builder()
            .user(user)
            .date(date)
            .lastModifiedDate(LocalDate.now())
            .build()
        );

    return TrainingRecordDto.fromEntity(saved);
  }


  @Override
  public TrainingRecordResponse getTrainingRecordInfo(Long trainingRecordId, String username) {
    TrainingRecord trainingRecord = trainingRecordRepository.findById(trainingRecordId)
        .orElseThrow(() -> new MyException(ErrorCode.TRAINING_RECORD_NOT_FOUND));

    if (!trainingRecord.getUser().getEmail().equals(username)) {
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }

    List<SetRecord> list = setRecordRepository.findAllByTrainingRecordId(trainingRecord.getId());

    return new TrainingRecordResponse(
        TrainingRecordDto.fromEntity(trainingRecord),
        list.stream().map(SetRecordDto::fromEntity).collect(Collectors.toList())
    );
  }

  @Override
  public TrainingRecordListResponse getTrainingRecordList(
      Long userId, int page, LocalDate start, LocalDate end) {
    //d2(end)값이 들어오지 않았을 때 : d1으로 들어온 날의 데이터를 반환
    if (Objects.isNull(end)) {
      end = start;
    }

    Page<TrainingRecord> trainingRecords = this.getTrainingRecords(userId, page, start, end);

    List<SetRecord> setRecords =
        setRecordRepository.findAllByUserIdAndDateBetween(userId, start, end);

    TrainingRecordListResponse trainingRecordListResponse =
        this.createResponse(trainingRecords, start, end, page);

    trainingRecordListResponse.setList(
        this.getTrainingRecordResponseList(trainingRecords, setRecords));

    return trainingRecordListResponse;
  }


  private Page<TrainingRecord> getTrainingRecords(
      Long userId, int page, LocalDate start, LocalDate end) {
    //사용자에게 페이지는 1부터 시작하지만, 프로그램에서는 0부터 시작하기 때문에 page에 1을 빼줌
    PageRequest pageRequest = PageRequest.of(page - 1, PageConstant.DEFAULT_PAGE_SIZE);

    return trainingRecordRepository.findAllByUserIdAndDateBetween(userId, pageRequest, start, end);
  }

  private TrainingRecordListResponse createResponse(
      Page<TrainingRecord> trainingRecords, LocalDate start, LocalDate end, int page) {

    return TrainingRecordListResponse.builder()
        .start(start)
        .end(end)
        .currentPage(page)
        .totalPages(trainingRecords.getTotalPages())
        .totalElements((int) trainingRecords.getTotalElements())
        .list(new ArrayList<>())
        .build();
  }

  private List<TrainingRecordResponse> getTrainingRecordResponseList(
      Page<TrainingRecord> trainingRecords, List<SetRecord> setRecords) {
    List<TrainingRecordResponse> list = new ArrayList<>();

    for (TrainingRecord trainingRecord : trainingRecords) {

      List<SetRecordDto> setRecordList = setRecords.stream()
          .filter(cur -> cur.getTrainingRecord().equals(trainingRecord))
          .map(SetRecordDto::fromEntity)
          .collect(Collectors.toList());

      list.add(
          new TrainingRecordResponse(TrainingRecordDto.fromEntity(trainingRecord), setRecordList));
    }

    return list;
  }

  @Override
  public boolean hasAuthority(Long userId, Long trainingRecordId) {
    TrainingRecord trainingRecord = trainingRecordRepository.findById(trainingRecordId)
        .orElseThrow(() -> new MyException(ErrorCode.TRAINING_RECORD_NOT_FOUND));

    return trainingRecord.getUser().getId().equals(userId);

  }

}
