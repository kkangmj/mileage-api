package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {}
