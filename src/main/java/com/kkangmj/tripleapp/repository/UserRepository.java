package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findUserById(UUID id);
}
