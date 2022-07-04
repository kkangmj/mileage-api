package com.kkangmj.tripleapp;

import com.kkangmj.tripleapp.repository.ReviewRepositorySupport;
import com.kkangmj.tripleapp.repository.ReviewRepositorySupportImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

  @PersistenceContext private EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(entityManager);
  }

  @Bean
  public ReviewRepositorySupport reviewRepositorySupport() {
    return new ReviewRepositorySupportImpl(jpaQueryFactory());
  }
}
