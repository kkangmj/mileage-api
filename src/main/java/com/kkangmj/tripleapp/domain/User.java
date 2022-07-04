package com.kkangmj.tripleapp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "seq", columnDefinition = "INT UNSIGNED")
  private Long seq;

  @Column(name = "uuid", columnDefinition = "BINARY(16)", nullable = false, unique = true)
  private UUID uuid;

  @OneToMany(
      mappedBy = "user",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
      orphanRemoval = true)
  private List<UserPoint> userPoints = new ArrayList<>();

  @Builder
  public User(UUID uuid) {
    this.uuid = uuid;
    UserPoint userPoint = UserPoint.builder().build();
    userPoint.setUserId(this);
    this.userPoints = new ArrayList<>(List.of(userPoint));
  }
}
