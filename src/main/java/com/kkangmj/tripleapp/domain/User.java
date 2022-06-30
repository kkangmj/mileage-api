package com.kkangmj.tripleapp.domain;

import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "INT UNSIGNED")
  private Long seq;

  @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
  private UUID id;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
  private UserPoint point;

  @Builder
  public User(UUID id) {
    this.id = id;
    UserPoint userPoint = UserPoint.builder().build();
    userPoint.setUserId(id);
    this.point = userPoint;
  }
}
