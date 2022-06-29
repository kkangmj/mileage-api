package com.kkangmj.tripleapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kkangmj.tripleapp.domain.Photo;
import com.kkangmj.tripleapp.domain.Place;
import com.kkangmj.tripleapp.domain.Review;
import com.kkangmj.tripleapp.domain.User;
import com.kkangmj.tripleapp.repository.PhotoRepository;
import com.kkangmj.tripleapp.repository.PlaceRepository;
import com.kkangmj.tripleapp.repository.ReviewRepository;
import com.kkangmj.tripleapp.repository.UserRepository;
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
  @Autowired private PhotoRepository photoRepository;

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
    Photo photo1 = Photo.builder().id(UUID.randomUUID()).build();
    Photo photo2 = Photo.builder().id(UUID.randomUUID()).build();
    Review review =
        Review.builder().id(UUID.randomUUID()).content("content").place(place).user(user).build();

    // when
    Photo[] photos = {photo1, photo2};
    List<Photo> photoList = new ArrayList<>(Arrays.asList(photos));
    reviewRepository.save(review.updatePhotos(photoList));

    // then
    assertThat(reviewRepository.count()).isEqualTo(1);
    assertThat(photoRepository.count()).isEqualTo(2);
  }

  @Test
  @DisplayName("Review 수정하기")
  void updateReviewWithPhoto() {
    // given - photo가 포함된 리뷰 생성
    Photo photo1 = Photo.builder().id(UUID.randomUUID()).build();
    Photo photo2 = Photo.builder().id(UUID.randomUUID()).build();
    Review review =
        Review.builder().id(UUID.randomUUID()).content("content").place(place).user(user).build();

    Photo[] photos1 = {photo1, photo2};
    List<Photo> photoList1 = new ArrayList<>(Arrays.asList(photos1));
    reviewRepository.save(review.updatePhotos(photoList1));

    // when - photo 수정
    Photo photo3 = Photo.builder().id(UUID.randomUUID()).build();
    Photo photo4 = Photo.builder().id(UUID.randomUUID()).build();

    Photo[] photos2 = {photo1, photo3, photo4};
    List<Photo> photoList2 = new ArrayList<>(Arrays.asList(photos2));

    Review updatedReview =
        Review.builder()
            .seq(review.getSeq())
            .id(review.getId())
            .content("content")
            .place(place)
            .user(user)
            .build();

    reviewRepository.save(updatedReview.updatePhotos(photoList2));

    // then
    assertThat(reviewRepository.count()).isEqualTo(1);
    assertThat(photoRepository.count()).isEqualTo(3);
  }

  @Test
  @DisplayName("Review soft delete")
  void deleteReviewAndPhoto() {
    // given
    Review review =
        Review.builder().id(UUID.randomUUID()).content("content").place(place).user(user).build();
    Photo photo1 = Photo.builder().id(UUID.randomUUID()).build();
    Photo photo2 = Photo.builder().id(UUID.randomUUID()).build();

    Photo[] photos = {photo1, photo2};
    List<Photo> photoList = new ArrayList<>(Arrays.asList(photos));
    reviewRepository.save(review.updatePhotos(photoList));

    // when
    reviewRepository.delete(review);

    // then
    assertThat(reviewRepository.count()).isEqualTo(0);
    assertThat(photoRepository.count()).isEqualTo(0);
  }
}
