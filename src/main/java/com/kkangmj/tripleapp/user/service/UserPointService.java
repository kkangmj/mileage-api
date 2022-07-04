package com.kkangmj.tripleapp.user.service;

import com.kkangmj.tripleapp.error.ErrorCode;
import com.kkangmj.tripleapp.error.exception.NotFoundException;
import com.kkangmj.tripleapp.user.domain.UserPoint;
import com.kkangmj.tripleapp.user.dto.UserPointResponseDto;
import com.kkangmj.tripleapp.user.repository.UserPointRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
  private final UserPointRepository userPointRepository;

  @Transactional
  public UserPointResponseDto getUserPoint(UUID id) {

    UserPoint userPoint =
        userPointRepository
            .findByUserIdQuery(id)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    return UserPointResponseDto.from(userPoint);
  }







}
