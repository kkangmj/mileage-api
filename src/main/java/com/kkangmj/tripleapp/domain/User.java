package com.kkangmj.tripleapp.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "INT UNSIGNED")
  private Long seq;

  @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
  private UUID id;

  @Column
  @ColumnDefault("0")
  private int contentPoint;

  @Column
  @ColumnDefault("0")
  private int bonusPoint;
}
