package com.example.fitnessrecord.community.likes.persist;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

  boolean existsByUserIdAndRoutinePostId(Long userId, Long routinePostId);

  Optional<Likes> findByUserIdAndRoutinePostId(Long userId, Long routinePostId);
}
