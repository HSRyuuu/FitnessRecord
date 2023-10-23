package com.example.fitnessrecord.domain.userbodyinfo.persist;

import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyInfoRepository extends JpaRepository<BodyInfo, Long> {
  Optional<BodyInfo> findByUserIdAndDate(Long userId, LocalDate date);
  Optional<BodyInfo> findByUserAndDate(User user, LocalDate date);

  List<BodyInfo> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}
