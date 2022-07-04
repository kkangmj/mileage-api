package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

  @Override
  @Query(
      value =
          "SELECT COUNT(reviewImage) FROM ReviewImage reviewImage WHERE reviewImage.is_deleted = false")
  long count();
}
