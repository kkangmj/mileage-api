package com.kkangmj.tripleapp.history.service;

import com.kkangmj.tripleapp.common.dto.PageResponseDto;
import com.kkangmj.tripleapp.error.ErrorCode;
import com.kkangmj.tripleapp.error.exception.NotFoundException;
import com.kkangmj.tripleapp.history.dto.UserPointHistoryDto;
import com.kkangmj.tripleapp.user.domain.UserPoint;
import com.kkangmj.tripleapp.user.repository.UserPointRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {
  @PersistenceContext private EntityManager entityManager;
  private final UserPointRepository userPointRepository;
  private static final int PAGE_SIZE = 20;

  @Transactional
  public PageResponseDto<UserPointHistoryDto> getUserPointHistory(UUID id, int pageId) {

    Long seqOfUserPoint = getSeqOfUser(id);

    int totalPages = getTotalPagesWithUserSeq(seqOfUserPoint);
    List<Object[]> listOfHistory = getDataOfPageWithUserSeq(seqOfUserPoint, pageId);
    List<UserPointHistoryDto> listOfHistoryDto =
        listOfHistory.stream()
            .map(
                history -> {
                  UserPoint userPoint = (UserPoint) history[0];
                  DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) history[1];
                  return UserPointHistoryDto.of(
                      userPoint.getUser().getUuid(),
                      userPoint.getContentPoint(),
                      userPoint.getBonusPoint(),
                      revisionEntity.getRevisionDate());
                })
            .collect(Collectors.toList());

    return PageResponseDto.of(pageId, totalPages, listOfHistoryDto);
  }

  @Transactional
  public PageResponseDto<UserPointHistoryDto> getPointHistory(int pageId) {

    int totalPages = getTotalPages();
    List<Object[]> listOfHistory = getDataOfPage(pageId);
    List<UserPointHistoryDto> listOfHistoryDto =
        listOfHistory.stream()
            .map(
                history -> {
                  UserPoint userPoint = (UserPoint) history[0];
                  DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) history[1];
                  return UserPointHistoryDto.of(
                      userPoint.getUser().getUuid(),
                      userPoint.getContentPoint(),
                      userPoint.getBonusPoint(),
                      revisionEntity.getRevisionDate());
                })
            .collect(Collectors.toList());

    return PageResponseDto.of(pageId, totalPages, listOfHistoryDto);
  }

  private Long getSeqOfUser(UUID id) {

    UserPoint userPoint =
        userPointRepository
            .findByUserIdQuery(id)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    return userPoint.getSeq();
  }

  private int getTotalPagesWithUserSeq(Long seq) {

    AuditReader auditReader = AuditReaderFactory.get(entityManager);

    List<Object[]> listOfHistory =
        auditReader
            .createQuery()
            .forRevisionsOfEntity(UserPoint.class, false, false)
            .add(AuditEntity.property("seq").eq(seq))
            .getResultList();

    return (listOfHistory.size() + PAGE_SIZE - 1) / PAGE_SIZE;
  }

  private int getTotalPages() {

    AuditReader auditReader = AuditReaderFactory.get(entityManager);

    List<Object[]> listOfHistory =
        auditReader
            .createQuery()
            .forRevisionsOfEntity(UserPoint.class, false, false)
            .getResultList();

    return (listOfHistory.size() + PAGE_SIZE - 1) / PAGE_SIZE;
  }

  private List getDataOfPageWithUserSeq(Long seq, int pageId) {

    AuditReader auditReader = AuditReaderFactory.get(entityManager);
    int startPoint = (pageId - 1) * PAGE_SIZE;

    List listOfHistory =
        auditReader
            .createQuery()
            .forRevisionsOfEntity(UserPoint.class, false, false)
            .add(AuditEntity.property("seq").eq(seq))
            .setFirstResult(startPoint)
            .setMaxResults(PAGE_SIZE)
            .addOrder(AuditEntity.revisionNumber().desc())
            .getResultList();

    return listOfHistory;
  }

  private List getDataOfPage(int pageId) {

    AuditReader auditReader = AuditReaderFactory.get(entityManager);
    int startPoint = (pageId - 1) * PAGE_SIZE;

    List listOfHistory =
        auditReader
            .createQuery()
            .forRevisionsOfEntity(UserPoint.class, false, false)
            .setFirstResult(startPoint)
            .setMaxResults(PAGE_SIZE)
            .addOrder(AuditEntity.revisionNumber().desc())
            .getResultList();

    return listOfHistory;
  }
}
