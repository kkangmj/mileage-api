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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE photo SET is_deleted = true WHERE seq = ?")
@Where(clause = "is_deleted=false")
public class Photo implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "INT UNSIGNED")
  private Long seq;

  @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
  private UUID id;

  private boolean is_deleted = Boolean.FALSE;

  @ManyToOne
  @JoinColumn(
      name = "review_id",
      referencedColumnName = "id",
      nullable = false,
      columnDefinition = "BINARY(16)")
  private Review review;

  public void setReview(Review review) {
    this.review = review;
  }

  @Builder
  public Photo(UUID id, Review review) {
    this.id = id;
    this.review = review;
  }
}
