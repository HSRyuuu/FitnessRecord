package com.example.fitnessrecord.domain.record.setrecord.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetRecordRepository extends JpaRepository<SetRecord, Long> {

}
