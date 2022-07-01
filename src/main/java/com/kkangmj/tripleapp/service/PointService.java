package com.kkangmj.tripleapp.service;

import com.kkangmj.tripleapp.domain.User;
import com.kkangmj.tripleapp.dto.UserPointResponseDto;
import com.kkangmj.tripleapp.error.ErrorCode;
import com.kkangmj.tripleapp.error.exception.NotFoundException;
import com.kkangmj.tripleapp.repository.UserPointRepository;
import com.kkangmj.tripleapp.repository.UserRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
  private final UserPointRepository userPointRepository;
  private final UserRepository userRepository;

  @Transactional
  public UserPointResponseDto getUserPoint(UUID id) {

    User user =
        userRepository
            .findByUuid(id)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    return UserPointResponseDto.from(userPointRepository.findByUser(user).get());
  }
}
