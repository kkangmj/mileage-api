package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.QReview;
import com.kkangmj.tripleapp.domain.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositorySupportImpl implements ReviewRepositorySupport {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Review> findReviewByCreatedAtBefore(LocalDateTime createdAt, UUID placeId) {

    QReview qReview = QReview.review;

    return jpaQueryFactory
        .selectFrom(qReview)
        .where(
            qReview
                .is_deleted
                .eq(false)
                .and(qReview.createdAt.before(createdAt))
                .and(qReview.place.uuid.eq(placeId)))
        .limit(1L)
        .fetch();
  }

  @Override
  public List<Review> findReviewByIsDeletedAndLastModifiedAt(LocalDateTime createdAt, UUID placeId) {

    QReview qReview = QReview.review;

    return jpaQueryFactory
        .selectFrom(qReview)
        .where(
            qReview
                .is_deleted
                .eq(true)
                .and(qReview.createdAt.before(createdAt))
                .and(qReview.lastModifiedAt.after(createdAt))
                .and(qReview.place.uuid.eq(placeId)))
        .limit(1L)
        .fetch();
  }
}
