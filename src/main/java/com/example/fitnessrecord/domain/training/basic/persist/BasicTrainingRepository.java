package com.example.fitnessrecord.domain.training.basic.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicTrainingRepository extends JpaRepository<BasicTraining, Long> {

}
