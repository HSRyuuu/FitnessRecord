package com.example.fitnessrecord.domain.record.trainingrecord.persist;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long> {

  List<TrainingRecord> findAllByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}
