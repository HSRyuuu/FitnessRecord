package com.example.fitnessrecord.domain.training.custom.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomTrainingRepository extends JpaRepository<CustomTraining, Long> {
  boolean existsByTrainingName(String trainingName);
  Page<CustomTraining> findByUserId(Long userId, Pageable pageable);
}
