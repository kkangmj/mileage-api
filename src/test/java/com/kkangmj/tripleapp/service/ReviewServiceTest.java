package com.kkangmj.tripleapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.kkangmj.tripleapp.place.domain.Place;
import com.kkangmj.tripleapp.review.domain.Review;
import com.kkangmj.tripleapp.user.domain.User;
import com.kkangmj.tripleapp.event.controller.EventAction;
import com.kkangmj.tripleapp.event.dto.EventRequestDto;
import com.kkangmj.tripleapp.event.controller.EventType;
import com.kkangmj.tripleapp.place.repository.PlaceRepository;
import com.kkangmj.tripleapp.review.repository.ReviewRepository;
import com.kkangmj.tripleapp.user.repository.UserPointRepository;
import com.kkangmj.tripleapp.user.repository.UserRepository;
import com.kkangmj.tripleapp.review.service.ReviewService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ReviewServiceTest {

  @Autowired private ReviewService reviewService;
  @Autowired private UserRepository userRepository;
  @Autowired private UserPointRepository userPointRepository;
  @Autowired private ReviewRepository reviewRepository;
  @Autowired private PlaceRepository placeRepository;

  @BeforeEach
  @AfterEach
  void beforeAndAfterEach() {
    userRepository.deleteAll();
    reviewRepository.deleteAll();
    placeRepository.deleteAll();
  }

  @Test
  @DisplayName("content만 존재하는 리뷰 생성 및 포인트 증감 확인 - 보너스 포인트 O")
  @Transactional
  public void createReviewWithContent() {
    // given
    UUID reviewId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID placeId = UUID.randomUUID();

    User user = User.builder().uuid(userId).build();
    Place place = Place.builder().uuid(placeId).build();

    userRepository.save(user);
    placeRepository.save(place);

    EventRequestDto eventRequestDto =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.ADD,
            reviewId.toString(),
            "Hello",
            new ArrayList<>(),
            userId.toString(),
            placeId.toString());

    // when
    reviewService.handleEvent(eventRequestDto);

    // then
    assertThat(userPointRepository.findByUserIdQuery(userId).get().getContentPoint()).isEqualTo(1);
    assertThat(userPointRepository.findByUserIdQuery(userId).get().getBonusPoint()).isEqualTo(1);
    assertThat(reviewRepository.findByReviewUuid(reviewId).get().getContent()).isEqualTo("Hello");
  }

  @Test
  @DisplayName("content와 image 모두 존재하는 리뷰 생성 및 포인트 증감 확인 - 보너스 포인트 X")
  @Transactional
  public void createReviewWithContentAndImage() {
    // given
    UUID reviewId = UUID.randomUUID();
    UUID reviewId2 = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    UUID placeId = UUID.randomUUID();

    User user = User.builder().uuid(userId).build();
    User user2 = User.builder().uuid(userId2).build();
    Place place = Place.builder().uuid(placeId).build();
    Review review = Review.builder().uuid(reviewId2).content("d").user(user2).place(place).build();

    review.savePhotos(new ArrayList<>());

    userRepository.save(user);
    userRepository.save(user2);
    placeRepository.save(place);
    reviewRepository.save(review);

    EventRequestDto eventRequestDto =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.ADD,
            reviewId.toString(),
            "Hello",
            new ArrayList<>(List.of("2ec06b05-dc72-4b13-8323-953fc20f78bf")),
            userId.toString(),
            placeId.toString());

    // when
    reviewService.handleEvent(eventRequestDto);

    // then
    assertThat(userPointRepository.findByUserIdQuery(userId).get().getContentPoint()).isEqualTo(2);
    assertThat(userPointRepository.findByUserIdQuery(userId).get().getBonusPoint()).isEqualTo(0);
    assertThat(reviewRepository.findByReviewUuid(reviewId).get().getContent()).isEqualTo("Hello");
  }

  @Test
  @DisplayName("리뷰 수정 - 사진 추가")
  @Transactional
  public void updateReviewAddPhoto() {
    // given
    UUID reviewId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID placeId = UUID.randomUUID();

    User user = User.builder().uuid(userId).build();
    Place place = Place.builder().uuid(placeId).build();

    userRepository.save(user);
    placeRepository.save(place);

    EventRequestDto eventRequestDto1 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.ADD,
            reviewId.toString(),
            "Hi",
            new ArrayList<>(),
            userId.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto1);

    // when
    EventRequestDto eventRequestDto2 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.MOD,
            reviewId.toString(),
            "Hello",
            new ArrayList<>(List.of("2ec06b05-dc72-4b13-8323-953fc20f78bf")),
            userId.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto2);

    // then
    assertThat(userPointRepository.findByUserIdQuery(userId).get().getContentPoint()).isEqualTo(2);
    assertThat(reviewRepository.findByReviewUuid(reviewId).get().getContent()).isEqualTo("Hello");
  }

  @Test
  @DisplayName("리뷰 수정 - 사진 삭제")
  @Transactional
  public void updateReviewRemovePhoto() {
    // given
    UUID reviewId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID placeId = UUID.randomUUID();

    User user = User.builder().uuid(userId).build();
    Place place = Place.builder().uuid(placeId).build();

    userRepository.save(user);
    placeRepository.save(place);

    EventRequestDto eventRequestDto1 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.ADD,
            reviewId.toString(),
            "Hi",
            new ArrayList<>(List.of("240a0658-dc5f-4878-9381-ebb7b2667775")),
            userId.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto1);

    // when
    EventRequestDto eventRequestDto2 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.MOD,
            reviewId.toString(),
            "Hello",
            new ArrayList<>(),
            userId.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto2);

    // then
    assertThat(userPointRepository.findByUserIdQuery(userId).get().getContentPoint()).isEqualTo(1);
    assertThat(reviewRepository.findByReviewUuid(reviewId).get().getContent()).isEqualTo("Hello");
  }

  @Test
  @DisplayName("리뷰 삭제 - 특정 장소의 첫 리뷰어인 경우")
  @Transactional
  public void deleteReview1() {
    // given
    UUID reviewId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID placeId = UUID.randomUUID();

    User user = User.builder().uuid(userId).build();
    Place place = Place.builder().uuid(placeId).build();

    userRepository.save(user);
    placeRepository.save(place);

    EventRequestDto eventRequestDto1 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.ADD,
            reviewId.toString(),
            "Hi",
            new ArrayList<>(List.of("240a0658-dc5f-4878-9381-ebb7b2667775")),
            userId.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto1);

    // when
    EventRequestDto eventRequestDto2 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.DELETE,
            reviewId.toString(),
            "Hello",
            new ArrayList<>(),
            userId.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto2);

    // then
    assertThat(userPointRepository.findByUserIdQuery(userId).get().getContentPoint()).isEqualTo(0);
    assertThat(userPointRepository.findByUserIdQuery(userId).get().getBonusPoint()).isEqualTo(0);
  }

  @Test
  @DisplayName("리뷰 삭제 - 특정 장소의 첫 리뷰어가 아닌 경우")
  @Transactional
  public void deleteReview2() throws InterruptedException {
    // given
    UUID reviewId1 = UUID.randomUUID();
    UUID reviewId2 = UUID.randomUUID();
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    UUID placeId = UUID.randomUUID();

    User user1 = User.builder().uuid(userId1).build();
    User user2 = User.builder().uuid(userId2).build();
    Place place = Place.builder().uuid(placeId).build();

    userRepository.save(user1);
    userRepository.save(user2);
    placeRepository.save(place);

    EventRequestDto eventRequestDto1 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.ADD,
            reviewId1.toString(),
            "Hi",
            new ArrayList<>(),
            userId1.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto1);

    Thread.sleep(1000L);

    EventRequestDto eventRequestDto2 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.ADD,
            reviewId2.toString(),
            "Hi",
            new ArrayList<>(),
            userId2.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto2);

    Thread.sleep(1000L);

    EventRequestDto eventRequestDto3 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.DELETE,
            reviewId1.toString(),
            "Hi",
            new ArrayList<>(),
            userId1.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto3);

    Thread.sleep(1000L);

    // when
    EventRequestDto eventRequestDto4 =
        new EventRequestDto(
            EventType.REVIEW,
            EventAction.DELETE,
            reviewId2.toString(),
            "Hi",
            new ArrayList<>(),
            userId2.toString(),
            placeId.toString());

    reviewService.handleEvent(eventRequestDto4);

    // then
    assertThat(userPointRepository.findByUserIdQuery(userId2).get().getContentPoint()).isEqualTo(0);
    assertThat(userPointRepository.findByUserIdQuery(userId2).get().getBonusPoint()).isEqualTo(0);
  }
}
