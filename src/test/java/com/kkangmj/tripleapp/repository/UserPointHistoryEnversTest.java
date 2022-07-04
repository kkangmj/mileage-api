package com.kkangmj.tripleapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kkangmj.tripleapp.TestConfig;
import com.kkangmj.tripleapp.user.domain.User;
import com.kkangmj.tripleapp.user.domain.UserPoint;
import com.kkangmj.tripleapp.user.repository.UserPointRepository;
import com.kkangmj.tripleapp.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserPointHistoryEnversTest {

  @Autowired private UserRepository userRepository;
  @Autowired private UserPointRepository userPointRepository;
  @PersistenceUnit private EntityManagerFactory entityManagerFactory;
  private EntityManager entityManager;

  @BeforeEach
  void beforeEach() {
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
  }

  @AfterEach
  void afterEach() {
    userRepository.deleteAll();
    entityManager.getTransaction().commit();
  }

  @Test
  @DisplayName("유저의 포인트 변경 이력 리스트 조회")
  void getPointHistoryListOfUser() {
    // given
    UUID userId1 = UUID.randomUUID();
    userRepository.save(User.builder().uuid(userId1).build());
    UUID userId2 = UUID.randomUUID();
    userRepository.save(User.builder().uuid(userId2).build());

    UserPoint userPoint1 = userPointRepository.findByUserIdQuery(userId1).get();
    Long seqOfUserPoint = userPoint1.getSeq();

    // when
    userPoint1.updatePoints(1, 2);
    userPointRepository.save(userPoint1);

    userPoint1.updatePoints(3, 2);
    userPointRepository.save(userPoint1);

    entityManager.flush();

    AuditReader auditReader = AuditReaderFactory.get(entityManager);
    List list =
        auditReader
            .createQuery()
            .forRevisionsOfEntity(UserPoint.class, false)
            .add(AuditEntity.property("seq").eq(seqOfUserPoint))
            .getResultList();

    // then
    assertThat(list.size()).isEqualTo(3);
  }
}
