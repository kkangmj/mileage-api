package com.kkangmj.tripleapp.repository;

import com.kkangmj.tripleapp.domain.Place;
import com.kkangmj.tripleapp.domain.UserPoint;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

  Optional<Place> findByUuid(UUID placeId);
}
