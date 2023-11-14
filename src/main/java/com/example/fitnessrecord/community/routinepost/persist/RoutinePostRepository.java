package com.example.fitnessrecord.community.routinepost.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutinePostRepository extends JpaRepository<RoutinePost, Long> {

}
