package com.kkangmj.tripleapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.kkangmj.tripleapp.domain.Place;
import com.kkangmj.tripleapp.domain.Review;
import com.kkangmj.tripleapp.domain.User;
import com.kkangmj.tripleapp.dto.EventAction;
import com.kkangmj.tripleapp.dto.EventRequestDto;
import com.kkangmj.tripleapp.dto.EventType;
import com.kkangmj.tripleapp.repository.PlaceRepository;
import com.kkangmj.tripleapp.repository.ReviewRepository;
import com.kkangmj.tripleapp.repository.UserPointRepository;
import com.kkangmj.tripleapp.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
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
    assertThat(reviewRepository.findByUuid(reviewId).get().getContent()).isEqualTo("Hello");
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
    assertThat(reviewRepository.findByUuid(reviewId).get().getContent()).isEqualTo("Hello");
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
    assertThat(reviewRepository.findByUuid(reviewId).get().getContent()).isEqualTo("Hello");
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
    assertThat(reviewRepository.findByUuid(reviewId).get().getContent()).isEqualTo("Hello");
  }
}
