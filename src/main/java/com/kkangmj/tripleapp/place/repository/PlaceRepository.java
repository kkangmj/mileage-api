package com.kkangmj.tripleapp.place.repository;

import com.kkangmj.tripleapp.place.domain.Place;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

  Optional<Place> findByUuid(UUID placeId);
}
