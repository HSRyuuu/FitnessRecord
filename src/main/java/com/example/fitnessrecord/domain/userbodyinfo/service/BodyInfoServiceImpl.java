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
import java.util.Optional;
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
        bodyInfoRepository.findByUserAndCreateDate(user,  LocalDate.now());

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

}
