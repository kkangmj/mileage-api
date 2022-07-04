package com.kkangmj.tripleapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kkangmj.tripleapp.TestConfig;
import com.kkangmj.tripleapp.domain.Place;
import com.kkangmj.tripleapp.domain.Review;
import com.kkangmj.tripleapp.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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

@DataJpaTest
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ReviewRepositorySupportTest {
  @Autowired private ReviewRepository reviewRepository;
  @Autowired private PlaceRepository placeRepository;
  @Autowired private UserRepository userRepository;

  private Place place;
  private User user;
  private UUID placeId;
  private UUID userId;

  @BeforeEach
  void beforeEach() {
    placeId = UUID.randomUUID();
    userId = UUID.randomUUID();
    place = Place.builder().uuid(placeId).build();
    user = User.builder().uuid(userId).build();
    placeRepository.save(place);
    userRepository.save(user);
  }

  @Test
  @DisplayName("현재 place에 있는 리뷰 중 먼저 생성된 리뷰가 있는지 확인하기")
  void findReviewByCreatedAtBeforeTest() throws InterruptedException {
    // given
    Review review1 =
        Review.builder().uuid(UUID.randomUUID()).content("A").place(place).user(user).build();
    reviewRepository.save(review1);
    Thread.sleep(1000L);

    Review review2 =
        Review.builder().uuid(UUID.randomUUID()).content("B").place(place).user(user).build();
    reviewRepository.save(review2);
    Thread.sleep(1000L);

    UUID reviewId3 = UUID.randomUUID();
    Review review3 = Review.builder().uuid(reviewId3).content("C").place(place).user(user).build();
    reviewRepository.save(review3);
    Thread.sleep(1000L);

    Review review4 =
        Review.builder().uuid(UUID.randomUUID()).content("D").place(place).user(user).build();
    reviewRepository.save(review4);
    Thread.sleep(1000L);

    LocalDateTime createdAt3 = reviewRepository.findByReviewUuid(reviewId3).get().getCreatedAt();

    // when
    List<Review> list = reviewRepository.findReviewByCreatedAtBefore(createdAt3, placeId);

    // then
    assertThat(list.size()).isGreaterThan(0);
  }

  @Test
  @DisplayName("place에 있는 삭제된 리뷰 중 먼저 생성된 리뷰가 있는지 확인하기")
  void findReviewByIsDeletedAndLastModifiedAtTest() throws InterruptedException {
    // given
    Review review1 =
        Review.builder().uuid(UUID.randomUUID()).content("A").place(place).user(user).build();
    reviewRepository.save(review1);

    Review review2 =
        Review.builder().uuid(UUID.randomUUID()).content("B").place(place).user(user).build();
    reviewRepository.save(review2);
    Thread.sleep(1000L);

    UUID reviewId3 = UUID.randomUUID();
    Review review3 = Review.builder().uuid(reviewId3).content("C").place(place).user(user).build();
    reviewRepository.save(review3);
    Thread.sleep(1000L);

    reviewRepository.delete(review1);
    reviewRepository.delete(review2);

    LocalDateTime createdAt3 = reviewRepository.findByReviewUuid(reviewId3).get().getCreatedAt();

    // when
    List<Review> list =
        reviewRepository.findReviewByIsDeletedAndLastModifiedAt(createdAt3, placeId);

    // then
    assertThat(list.size()).isGreaterThan(0);
  }
}
