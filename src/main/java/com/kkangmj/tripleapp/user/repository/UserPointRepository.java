package com.kkangmj.tripleapp.user.repository;

import com.kkangmj.tripleapp.user.domain.UserPoint;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, Long> {
  @Query(value = "SELECT p FROM UserPoint p WHERE p.user.uuid = :userId")
  Optional<UserPoint> findByUserIdQuery(@Param("userId") UUID userId);
}
