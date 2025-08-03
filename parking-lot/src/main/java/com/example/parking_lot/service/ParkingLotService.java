package com.example.parking_lot.service;

import com.example.parking_lot.entity.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingLotService {
    Optional<ParkingSpot> allocateSpot(VehicleType vehicleType);
    ParkingTransaction checkIn(String licensePlate, VehicleType vehicleType);
    ParkingTransaction checkOut(String licensePlate);
    List<ParkingSpot> getAvailableSpots();
    double calculateFee(ParkingTransaction transaction);
}

