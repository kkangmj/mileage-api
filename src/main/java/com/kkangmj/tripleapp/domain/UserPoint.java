package com.kkangmj.tripleapp.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class UserPoint implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "seq", columnDefinition = "INT UNSIGNED")
  private Long seq;

  @Column
  @ColumnDefault("0")
  private int contentPoint;

  @Column
  @ColumnDefault("0")
  private int bonusPoint;

  @ManyToOne
  @JoinColumn(
      name = "user_id",
      referencedColumnName = "uuid",
      nullable = false,
      columnDefinition = "BINARY(16)")
  private User user;

  @Builder
  public UserPoint(UUID userId, int contentPoint, int bonusPoint) {
    this.contentPoint = contentPoint;
    this.bonusPoint = bonusPoint;
  }

  public void setUserId(User user) {
    this.user = user;
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
