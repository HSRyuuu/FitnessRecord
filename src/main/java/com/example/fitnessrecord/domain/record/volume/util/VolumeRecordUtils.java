package com.example.fitnessrecord.domain.record.volume.util;

import com.example.fitnessrecord.domain.record.setrecord.persist.SetRecord;
import com.example.fitnessrecord.domain.record.volume.persist.VolumeRecord;
import com.example.fitnessrecord.domain.training.common.type.BodyPart;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VolumeRecordUtils {
  /**
   * SetRecord list의 요소들을 volume으로 변환하여 Map에 저장 후 반환
   */
  public static Map<BodyPart, Double> getVolumeMapBySetRecords(List<SetRecord> setRecords) {
    return setRecords.stream()
        .collect(Collectors.toMap(
            item -> item.getBodyPart(), //key Mapper
            item -> item.getReps() * item.getWeight(), //value Mapper
            (v1, v2) -> v1 + v2) //key가 이미 존재할 때 merge 방법
        );
  }

  /**
   * 전달받은 volumeMap의 volume 값들을 엔티티에 세팅
   */
  public static VolumeRecord setVolumeRecordFieldsFromMap(VolumeRecord volumeRecord,
      Map<BodyPart, Double> volumeMap) {

    volumeRecord.setChest(volumeMap.getOrDefault(BodyPart.CHEST, 0.0));
    volumeRecord.setBack(volumeMap.getOrDefault(BodyPart.BACK, 0.0));
    volumeRecord.setLegs(volumeMap.getOrDefault(BodyPart.LEGS, 0.0));
    volumeRecord.setShoulder(volumeMap.getOrDefault(BodyPart.SHOULDER, 0.0));
    volumeRecord.setBiceps(volumeMap.getOrDefault(BodyPart.BICEPS, 0.0));
    volumeRecord.setTriceps(volumeMap.getOrDefault(BodyPart.TRICEPS, 0.0));
    volumeRecord.setAbs(volumeMap.getOrDefault(BodyPart.ABS, 0.0));
    volumeRecord.setEtc(volumeMap.getOrDefault(BodyPart.ETC, 0.0));

    return volumeRecord;
  }

}
