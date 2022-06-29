package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {}
