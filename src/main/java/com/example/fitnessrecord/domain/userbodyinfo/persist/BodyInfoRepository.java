package com.example.fitnessrecord.domain.userbodyinfo.persist;

import com.example.fitnessrecord.domain.user.persist.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyInfoRepository extends JpaRepository<BodyInfo, Long> {
  Optional<BodyInfo> findByUserAndCreateDate(User user, LocalDate date);
}
