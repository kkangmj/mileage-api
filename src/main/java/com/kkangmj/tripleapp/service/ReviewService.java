package com.kkangmj.tripleapp.service;

import com.kkangmj.tripleapp.domain.Place;
import com.kkangmj.tripleapp.domain.Review;
import com.kkangmj.tripleapp.domain.ReviewImage;
import com.kkangmj.tripleapp.domain.User;
import com.kkangmj.tripleapp.domain.UserPoint;
import com.kkangmj.tripleapp.dto.EventRequestDto;
import com.kkangmj.tripleapp.dto.EventResponseDto;
import com.kkangmj.tripleapp.dto.ReviewResponseDto;
import com.kkangmj.tripleapp.error.ErrorCode;
import com.kkangmj.tripleapp.error.exception.InvalidArgumentException;
import com.kkangmj.tripleapp.error.exception.NotFoundException;
import com.kkangmj.tripleapp.repository.PlaceRepository;
import com.kkangmj.tripleapp.repository.ReviewRepository;
import com.kkangmj.tripleapp.repository.UserPointRepository;
import com.kkangmj.tripleapp.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService implements EventService {

  private final UserPointRepository userPointRepository;
  private final ReviewRepository reviewRepository;
  private final PlaceRepository placeRepository;
  private final UserRepository userRepository;

  @Override
  public EventResponseDto handleEvent(EventRequestDto eventRequestDto) {

    switch (eventRequestDto.getEventAction()) {
      case ADD:
        createReview(
            UUID.fromString(eventRequestDto.getReviewId()),
            eventRequestDto.getContent(),
            convertToUuidList(eventRequestDto.getAttachedPhotoIds()),
            UUID.fromString(eventRequestDto.getUserId()),
            UUID.fromString(eventRequestDto.getPlaceId()));
        break;
      case MOD:
        updateReview(
            UUID.fromString(eventRequestDto.getReviewId()),
            eventRequestDto.getContent(),
            convertToUuidList(eventRequestDto.getAttachedPhotoIds()),
            UUID.fromString(eventRequestDto.getUserId()));
        break;
      case DELETE:
        deleteReview(
            UUID.fromString(eventRequestDto.getReviewId()),
            UUID.fromString(eventRequestDto.getUserId()),
            UUID.fromString(eventRequestDto.getPlaceId()));
        break;
      default:
        throw new InvalidArgumentException(ErrorCode.INVALID_PARAMETER);
    }

    return ReviewResponseDto.of(
        UUID.fromString(eventRequestDto.getUserId()),
        UUID.fromString(eventRequestDto.getReviewId()));
  }

  @Transactional
  protected void createReview(
      UUID reviewId, String content, List<UUID> attachedPhotoIds, UUID userId, UUID placeId) {

    User user =
        userRepository
            .findByUuid(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    Place place =
        placeRepository
            .findByUuid(placeId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PLACE_NOT_FOUND));

    int contentPoint = 0;
    if (content.length() > 0) {
      contentPoint++;
    }
    if (attachedPhotoIds.size() > 0) {
      contentPoint++;
    }

    int bonusPoint = 0;
    if (reviewRepository.findReviewByPlace(place).size() == 0) {
      bonusPoint++;
    }

    Review review =
        Review.builder().uuid(reviewId).content(content).place(place).user(user).build();
    reviewRepository.save(review.savePhotos(convertToReviewImageList(attachedPhotoIds)));

    updateUserPoint(user.getUserPoints().get(0), contentPoint, bonusPoint);
  }

  @Transactional
  protected void updateReview(
      UUID reviewId, String content, List<UUID> attachedPhotoIds, UUID userId) {

    Review review =
        reviewRepository
            .findByReviewUuid(reviewId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));

    int contentPoint = 0;
    int bonusPoint = 0;
    if (review.getReviewImages().size() > 0 && attachedPhotoIds.size() == 0) {
      contentPoint--;
    }
    if (review.getReviewImages().size() == 0 && attachedPhotoIds.size() > 0) {
      contentPoint++;
    }

    review.savePhotos(convertToReviewImageList(attachedPhotoIds));
    review.updateContent(content);
    reviewRepository.save(review);

    UserPoint userPoint =
        userPointRepository
            .findByUserIdQuery(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    updateUserPoint(userPoint, contentPoint, bonusPoint);
  }

  @Transactional
  protected void deleteReview(UUID reviewId, UUID userId, UUID placeId) {

    Review review =
        reviewRepository
            .findByReviewUuid(reviewId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));

    int contentPoint = 0;
    if (review.getContent().length() > 0) {
      contentPoint--;
    }
    if (review.getReviewImages().size() > 0) {
      contentPoint--;
    }

    int bonusPoint = 0;
    if (reviewRepository.findReviewByCreatedAtBefore(review.getCreatedAt(), placeId).size() == 0
        && reviewRepository
                .findReviewByIsDeletedAndLastModifiedAt(review.getCreatedAt(), placeId)
                .size()
            == 0) {
      bonusPoint--;
    }

    reviewRepository.delete(review);

    UserPoint userPoint =
        userPointRepository
            .findByUserIdQuery(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    updateUserPoint(userPoint, contentPoint, bonusPoint);
  }

  private List<UUID> convertToUuidList(List<String> list) {
    return list.stream().map(UUID::fromString).collect(Collectors.toList());
  }

  private List<ReviewImage> convertToReviewImageList(List<UUID> list) {
    return list.stream()
        .map(it -> ReviewImage.builder().uuid(it).build())
        .collect(Collectors.toList());
  }

  @Transactional
  protected void updateUserPoint(UserPoint userPoint, int contentPoint, int bonusPoint) {
    userPoint.updatePoints(
        userPoint.getContentPoint() + contentPoint, userPoint.getBonusPoint() + bonusPoint);
    userPointRepository.save(userPoint);
  }
}
