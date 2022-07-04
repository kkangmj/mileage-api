package com.kkangmj.tripleapp.review.repository;

import com.kkangmj.tripleapp.place.domain.Place;
import com.kkangmj.tripleapp.review.domain.Review;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositorySupport {

  @Query(
      value =
          "SELECT review FROM Review review WHERE review.is_deleted = false AND review.uuid = :reviewId")
  Optional<Review> findByReviewUuid(@Param("reviewId") UUID reviewId);

  @Query(
      value =
          "SELECT review FROM Review review WHERE review.is_deleted = false AND review.place = :place")
  List<Review> findReviewByPlace(Place place);

  @Override
  @Query(value = "SELECT COUNT(review) FROM Review review WHERE review.is_deleted = false")
  long count();
}
