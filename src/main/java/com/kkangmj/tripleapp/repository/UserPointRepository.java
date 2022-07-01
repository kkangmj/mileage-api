package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.User;
import com.kkangmj.tripleapp.domain.UserPoint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, Long> {
  Optional<UserPoint> findByUser(User user);
}
