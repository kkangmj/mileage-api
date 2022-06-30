package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {}
