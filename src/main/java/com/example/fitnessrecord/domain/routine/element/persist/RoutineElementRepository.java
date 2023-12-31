package com.example.fitnessrecord.domain.routine.element.persist;

import com.example.fitnessrecord.domain.routine.routine.persist.Routine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineElementRepository extends JpaRepository<RoutineElement, Long> {
  List<RoutineElement> findAllByRoutineIdOrderByOrderNumber(Long routineId);
  void deleteAllByRoutine(Routine routine);
}
