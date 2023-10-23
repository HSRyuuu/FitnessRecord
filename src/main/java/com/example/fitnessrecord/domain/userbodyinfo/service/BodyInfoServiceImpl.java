package com.example.fitnessrecord.domain.userbodyinfo.service;


import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;
import com.example.fitnessrecord.domain.userbodyinfo.persist.BodyInfo;
import com.example.fitnessrecord.domain.userbodyinfo.persist.BodyInfoRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BodyInfoServiceImpl implements BodyInfoService {

  private final BodyInfoRepository bodyInfoRepository;
  private final UserRepository userRepository;

  @Override
  public BodyInfoDto addBodyInfo(Long userId, BodyInfoInput input) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));

    Optional<BodyInfo> findBodyInfo =
        bodyInfoRepository.findByUserAndDate(user,  LocalDate.now());

    BodyInfo bodyInfo;
    if(findBodyInfo.isPresent()){
      bodyInfo = findBodyInfo.get();
      BodyInfoInput.updateBodyInfo(bodyInfo, input);
    }else{
      bodyInfo = BodyInfoInput.toEntity(input);
      bodyInfo.setUser(user);
    }

    BodyInfo saved = bodyInfoRepository.save(bodyInfo);

    return BodyInfoDto.fromEntity(saved);
  }

  @Override
  public List<BodyInfoDto> bodyInfoListByPeriod(Long userId, LocalDate start, LocalDate end) {
    List<BodyInfo> findList = bodyInfoRepository.findByUserIdAndDateBetween(userId, start, end);
    if(findList.isEmpty()){
      throw new MyException(ErrorCode.BODY_INFO_DATA_NOT_FOUND);
    }

    return findList.stream().map(bodyInfo -> BodyInfoDto.fromEntity(bodyInfo)).collect(Collectors.toList());
  }

  @Override
  public BodyInfoDto deleteByDate(Long userId, LocalDate date) {
    BodyInfo bodyInfo = bodyInfoRepository.findByUserIdAndDate(userId, date)
        .orElseThrow(() -> new MyException(ErrorCode.BODY_INFO_DATA_NOT_FOUND));

    bodyInfoRepository.delete(bodyInfo);

    return BodyInfoDto.fromEntity(bodyInfo);
  }

}
