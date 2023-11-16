package com.example.fitnessrecord.community.routinecomment.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineCommentRepository extends JpaRepository<RoutineComment, Long> {

}
