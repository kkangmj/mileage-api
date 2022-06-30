package com.kkangmj.tripleapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kkangmj.tripleapp.domain.ReviewImage;
import com.kkangmj.tripleapp.domain.Place;
import com.kkangmj.tripleapp.domain.Review;
import com.kkangmj.tripleapp.domain.User;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ReviewRepositoryTest {
  @Autowired private ReviewRepository reviewRepository;
  @Autowired private PlaceRepository placeRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private ReviewImageRepository reviewImageRepository;

  private Place place;
  private User user;

  @BeforeEach
  void beforeEach() {
    place = Place.builder().id(UUID.randomUUID()).build();
    user = User.builder().id(UUID.randomUUID()).build();
    placeRepository.save(place);
    userRepository.save(user);
  }

  @Test
  @DisplayName("Review 생성하기")
  void createReviewWithPhoto() {
    // given
    ReviewImage reviewImage1 = ReviewImage.builder().id(UUID.randomUUID()).build();
    ReviewImage reviewImage2 = ReviewImage.builder().id(UUID.randomUUID()).build();
    Review review =
        Review.builder().id(UUID.randomUUID()).content("content").place(place).user(user).build();

    // when
    ReviewImage[] reviewImages = {reviewImage1, reviewImage2};
    List<ReviewImage> reviewImageList = new ArrayList<>(Arrays.asList(reviewImages));
    reviewRepository.save(review.updatePhotos(reviewImageList));

    // then
    assertThat(reviewRepository.count()).isEqualTo(1);
    assertThat(reviewImageRepository.count()).isEqualTo(2);
  }

  @Test
  @DisplayName("Review 수정하기")
  void updateReviewWithPhoto() {
    // given - photo가 포함된 리뷰 생성
    ReviewImage reviewImage1 = ReviewImage.builder().id(UUID.randomUUID()).build();
    ReviewImage reviewImage2 = ReviewImage.builder().id(UUID.randomUUID()).build();
    Review review =
        Review.builder().id(UUID.randomUUID()).content("content").place(place).user(user).build();

    ReviewImage[] photos1 = {reviewImage1, reviewImage2};
    List<ReviewImage> reviewImageList1 = new ArrayList<>(Arrays.asList(photos1));
    reviewRepository.save(review.updatePhotos(reviewImageList1));

    // when - photo 수정
    ReviewImage reviewImage3 = ReviewImage.builder().id(UUID.randomUUID()).build();
    ReviewImage reviewImage4 = ReviewImage.builder().id(UUID.randomUUID()).build();

    ReviewImage[] photos2 = {reviewImage1, reviewImage3, reviewImage4};
    List<ReviewImage> reviewImageList2 = new ArrayList<>(Arrays.asList(photos2));

    Review updatedReview =
        Review.builder()
            .seq(review.getSeq())
            .id(review.getId())
            .content("content")
            .place(place)
            .user(user)
            .build();

    reviewRepository.save(updatedReview.updatePhotos(reviewImageList2));

    // then
    assertThat(reviewRepository.count()).isEqualTo(1);
    assertThat(reviewImageRepository.count()).isEqualTo(3);
  }

  @Test
  @DisplayName("Review soft delete")
  void deleteReviewAndPhoto() {
    // given
    Review review =
        Review.builder().id(UUID.randomUUID()).content("content").place(place).user(user).build();
    ReviewImage reviewImage1 = ReviewImage.builder().id(UUID.randomUUID()).build();
    ReviewImage reviewImage2 = ReviewImage.builder().id(UUID.randomUUID()).build();

    ReviewImage[] reviewImages = {reviewImage1, reviewImage2};
    List<ReviewImage> reviewImageList = new ArrayList<>(Arrays.asList(reviewImages));
    reviewRepository.save(review.updatePhotos(reviewImageList));

    // when
    reviewRepository.delete(review);

    // then
    assertThat(reviewRepository.count()).isEqualTo(0);
    assertThat(reviewImageRepository.count()).isEqualTo(0);
  }
}
