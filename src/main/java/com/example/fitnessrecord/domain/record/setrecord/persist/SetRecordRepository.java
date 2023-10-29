package com.example.fitnessrecord.domain.record.setrecord.persist;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetRecordRepository extends JpaRepository<SetRecord, Long> {
  List<SetRecord> findAllByTrainingRecordId(Long trainingRecordId);
}
