package com.example.fitnessrecord.domain.record.volume.persist;

import com.example.fitnessrecord.domain.record.trainingrecord.persist.TrainingRecord;
import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolumeRecordRepository extends JpaRepository<VolumeRecord, Long> {

  boolean existsByTrainingRecord(TrainingRecord trainingRecord);

  Optional<VolumeRecord> findByTrainingRecord(TrainingRecord trainingRecord);

  Optional<VolumeRecord> findFirstByDateBetweenAndWeeklyRecordedYnIsFalse(LocalDate start,
      LocalDate end);

  List<VolumeRecord> findAllByUserAndDateBetween(User user, LocalDate start, LocalDate end);
}
