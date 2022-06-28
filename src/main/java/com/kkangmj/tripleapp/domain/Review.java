package com.kkangmj.tripleapp.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "INT UNSIGNED")
  private Long seq;

  @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
  private UUID id;

  @Column(length = 1000, nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(
      name = "place_id",
      referencedColumnName = "id",
      nullable = false,
      columnDefinition = "BINARY(16)")
  private Place place;

  @ManyToOne
  @JoinColumn(
      name = "user_id",
      referencedColumnName = "id",
      nullable = false,
      columnDefinition = "BINARY(16)")
  private User user;
}
