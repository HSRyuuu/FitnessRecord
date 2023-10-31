package com.example.fitnessrecord.domain.record.volume.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolumeRecordRepository extends JpaRepository<VolumeRecord, Long> {
  boolean existsByTrainingRecordId(Long trainingRecordId);
}
