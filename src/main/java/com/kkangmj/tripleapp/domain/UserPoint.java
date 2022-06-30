package com.kkangmj.tripleapp.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "user_point")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class UserPoint {
  @Id
  @Column(columnDefinition = "BINARY(16)")
  private UUID userId;

  @Column
  @ColumnDefault("0")
  private int contentPoint;

  @Column
  @ColumnDefault("0")
  private int bonusPoint;

  @OneToOne
  @PrimaryKeyJoinColumn(
      name = "user_id",
      referencedColumnName = "id",
      columnDefinition = "BINARY(16)")
  private User user;

  @Builder
  public UserPoint(UUID userId, int contentPoint, int bonusPoint) {
    this.contentPoint = contentPoint;
    this.bonusPoint = bonusPoint;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void updateContentPoint(int contentPoint) {
    this.contentPoint = contentPoint;
  }

  public void updateBonusPoint(int bonusPoint) {
    this.bonusPoint = bonusPoint;
  }

  public void updatePoints(int contentPoint, int bonusPoint) {
    this.contentPoint = contentPoint;
    this.bonusPoint = bonusPoint;
  }
}
