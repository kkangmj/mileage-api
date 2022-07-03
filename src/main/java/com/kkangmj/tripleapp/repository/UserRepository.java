package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query(value = "SELECT u FROM User u WHERE u.uuid = :id")
  Optional<User> findByUuid(@Param("id") UUID id);
}
