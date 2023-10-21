package com.example.fitnessrecord.domain.userbodyinfo.service;


import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.persist.UserRepository;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoDto;
import com.example.fitnessrecord.domain.userbodyinfo.dto.BodyInfoInput;
import com.example.fitnessrecord.domain.userbodyinfo.persist.BodyInfo;
import com.example.fitnessrecord.domain.userbodyinfo.persist.BodyInfoRepository;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BodyInfoServiceImpl implements BodyInfoService{

  private final BodyInfoRepository bodyInfoRepository;
  private final UserRepository userRepository;

  @Override
  public BodyInfoDto addBodyInfo(Long userId, BodyInfoInput input) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new MyException(ErrorCode.USER_NOT_FOUND));

    BodyInfo bodyInfo = BodyInfoInput.toEntity(input);
    bodyInfo.setUser(user);

    BodyInfo saved = bodyInfoRepository.save(bodyInfo);


    return BodyInfoDto.fromEntity(saved);
  }
}
