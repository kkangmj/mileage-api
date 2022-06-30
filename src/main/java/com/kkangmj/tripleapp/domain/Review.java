package com.kkangmj.tripleapp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="review")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE review SET is_deleted = true WHERE seq = ?")
@Where(clause = "is_deleted=false")
public class Review implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "INT UNSIGNED")
  private Long seq;

  @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
  private UUID id;

  @Column(length = 1000, nullable = false)
  private String content;

  private boolean is_deleted = Boolean.FALSE;

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

  @OneToMany(
      mappedBy = "review",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
      orphanRemoval = true)
  private List<ReviewImage> reviewImages = new ArrayList<>();

  @Builder
  public Review(Long seq, UUID id, String content, Place place, User user) {
    this.seq = seq;
    this.id = id;
    this.content = content;
    this.place = place;
    this.user = user;
  }

  public Review updatePhotos(List<ReviewImage> reviewImages) {
    this.reviewImages = reviewImages;
    for (ReviewImage reviewImage : reviewImages) {
      reviewImage.setReview(this);
    }
    return this;
  }
}
