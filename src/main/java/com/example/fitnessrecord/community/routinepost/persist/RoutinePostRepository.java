package com.example.fitnessrecord.community.routinepost.persist;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutinePostRepository extends JpaRepository<RoutinePost, Long> {

  boolean existsById(Long id);

  Page<RoutinePost> findAllByOrderByCreateDateTimeDesc(Pageable pageable);

  @Query("SELECT p FROM RoutinePost p WHERE p.createDateTime >= :twentyFourHoursAgo ORDER BY p.likes DESC limit 10")
  List<RoutinePost> findTop10ByLikesInLast24Hours(@Param("twentyFourHoursAgo") LocalDateTime twentyFourHoursAgo);
}
