package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.Review;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepositorySupport {
  List<Review> findReviewByCreatedAtBefore(LocalDateTime createdAt, UUID placeId);

  List<Review> findReviewByIsDeletedAndLastModifiedAt(LocalDateTime createdAt, UUID placeId);
}
