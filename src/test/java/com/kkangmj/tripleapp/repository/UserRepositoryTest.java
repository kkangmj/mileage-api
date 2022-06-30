package com.kkangmj.tripleapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kkangmj.tripleapp.domain.User;
import com.kkangmj.tripleapp.domain.UserPoint;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

  @Autowired private UserRepository userRepository;
  @Autowired private UserPointRepository userPointRepository;

  @Test
  @DisplayName("초기 유저 생성하기 - 포인트도 생성")
  void initialSaveOfUser() {
    // given
    UUID userId = UUID.randomUUID();
    User user = User.builder().id(userId).build();

    // when
    userRepository.save(user);

    // then
    assertThat(userRepository.count()).isEqualTo(1);
    assertThat(userPointRepository.count()).isEqualTo(1);
    assertThat(userPointRepository.findById(userId)).isNotEmpty();
    assertThat(userPointRepository.findById(userId).get().getUserId()).isEqualTo(userId);
  }

  @Test
  @DisplayName("유저 uuid로 조회하기")
  void getUserByUUID() {
    // given
    UUID userId = UUID.randomUUID();
    User user1 = User.builder().id(userId).build();
    userRepository.save(user1);

    // when
    User user2 = userRepository.findUserById(userId);

    // then
    assertThat(user2).isSameAs(user1);
  }

  @Test
  @DisplayName("유저 포인트 변경하기")
  void updateUserPoint() {
    // given
    UUID userId = UUID.randomUUID();
    User user = User.builder().id(userId).build();
    userRepository.save(user);

    // when
    UserPoint userPoint1 = userPointRepository.findById(userId).get();
    userPoint1.updatePoints(1, 3);
    userPointRepository.save(userPoint1);

    UserPoint userPoint2 = userPointRepository.findById(userId).get();

    // then
    assertThat(userPoint2.getContentPoint()).isEqualTo(1);
    assertThat(userPoint2.getBonusPoint()).isEqualTo(3);
  }

  @Test
  @DisplayName("유저 삭제하기 - 포인트도 삭제")
  void deleteUser() {
    // given
    UUID userId = UUID.randomUUID();
    User user = User.builder().id(userId).build();
    userRepository.save(user);

    // when
    userRepository.deleteAll();

    // then
    assertThat(userRepository.count()).isEqualTo(0);
    assertThat(userPointRepository.count()).isEqualTo(0);
  }
}
