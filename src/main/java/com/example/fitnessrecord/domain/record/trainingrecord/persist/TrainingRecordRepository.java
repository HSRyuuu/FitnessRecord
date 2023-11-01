package com.example.fitnessrecord.domain.record.trainingrecord.persist;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long> {

  Page<TrainingRecord> findAllByLastModifiedDate(LocalDate lastModifiedDate, Pageable pageable);

  Page<TrainingRecord> findAllByUserIdAndDateBetween(Long userId, Pageable pageable,
      LocalDate start, LocalDate end);
}
