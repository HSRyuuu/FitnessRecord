package com.example.fitnessrecord.domain.training.basic.persist;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicTrainingRepository extends JpaRepository<BasicTraining, Long> {
  boolean existsByTrainingName(String trainingName);
  Optional<BasicTraining> findByTrainingName(String trainingName);
}
