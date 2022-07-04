package com.kkangmj.tripleapp.helper;

import com.kkangmj.tripleapp.place.domain.Place;
import com.kkangmj.tripleapp.place.repository.PlaceRepository;
import com.kkangmj.tripleapp.review.repository.ReviewRepository;
import com.kkangmj.tripleapp.user.domain.User;
import com.kkangmj.tripleapp.user.repository.UserPointRepository;
import com.kkangmj.tripleapp.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelperService {
  private final UserRepository userRepository;
  private final PlaceRepository placeRepository;

  public void pong() {
    UUID userId1 = UUID.fromString("b888e83e-f154-4d5a-8d6c-cfd76db569d0");
    UUID userId2 = UUID.fromString("c1b69c15-cf58-4697-8767-207054f58797");
    UUID userId3 = UUID.fromString("68b07ea3-c5f1-475e-b85d-8cd9491e1dea");

    userRepository.save(User.builder().uuid(userId1).build());
    userRepository.save(User.builder().uuid(userId2).build());
    userRepository.save(User.builder().uuid(userId3).build());

    UUID placeId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");

    Place place = Place.builder().uuid(placeId).build();

    placeRepository.save(place);
  }
}
