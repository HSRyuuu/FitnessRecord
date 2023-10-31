package com.example.fitnessrecord.domain.record.weekly.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyRecordRepository extends JpaRepository<WeeklyRecord, Long> {
}
