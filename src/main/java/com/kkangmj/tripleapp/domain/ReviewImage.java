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
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "review_image")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE review_image SET is_deleted = true WHERE seq = ?")
@Where(clause = "is_deleted=false")
public class ReviewImage implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "INT UNSIGNED")
  private Long seq;

  @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
  private UUID uuid;

  private boolean is_deleted = Boolean.FALSE;

  @ManyToOne
  @JoinColumn(
      name = "review_id",
      referencedColumnName = "uuid",
      nullable = false,
      columnDefinition = "BINARY(16)")
  private Review review;

  public void setReview(Review review) {
    this.review = review;
  }

  @Builder
  public ReviewImage(UUID uuid, Review review) {
    this.uuid = uuid;
    this.review = review;
  }
}
