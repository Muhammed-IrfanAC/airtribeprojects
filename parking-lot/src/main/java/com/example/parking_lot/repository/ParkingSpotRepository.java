package com.example.parking_lot.repository;

import com.example.parking_lot.entity.ParkingSpot;
import com.example.parking_lot.entity.SpotSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ParkingSpot s WHERE s.available = true AND s.size = :size ORDER BY s.spotNumber ASC LIMIT 1")
    Optional<ParkingSpot> findFirstAvailableSpotForUpdate(@Param("size") SpotSize size);
}
