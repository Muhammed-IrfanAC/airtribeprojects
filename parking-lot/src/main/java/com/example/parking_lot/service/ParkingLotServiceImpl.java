package com.example.parking_lot.service;

import com.example.parking_lot.entity.*;
import com.example.parking_lot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    private final ParkingSpotRepository spotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingTransactionRepository transactionRepository;

    @Autowired
    public ParkingLotServiceImpl(ParkingSpotRepository spotRepository, VehicleRepository vehicleRepository, ParkingTransactionRepository transactionRepository) {
        this.spotRepository = spotRepository;
        this.vehicleRepository = vehicleRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ParkingSpot> allocateSpot(VehicleType vehicleType) {
        SpotSize requiredSize = getRequiredSpotSize(vehicleType);
        return spotRepository.findFirstAvailableSpotForUpdate(requiredSize);
    }

    @Override
    @Transactional
    public ParkingTransaction checkIn(String licensePlate, VehicleType vehicleType) {
        Optional<ParkingSpot> spotOpt = allocateSpot(vehicleType);
        if (spotOpt.isEmpty()) throw new RuntimeException("No available spot");
        ParkingSpot spot = spotOpt.get();
        if (!spot.isAvailable()) throw new RuntimeException("Spot just got occupied. Please try again.");
        spot.setAvailable(false);
        spotRepository.save(spot);

        Vehicle vehicle = vehicleRepository.findAll().stream()
                .filter(v -> v.getLicensePlate().equals(licensePlate))
                .findFirst()
                .orElseGet(() -> {
                    Vehicle v = new Vehicle();
                    v.setLicensePlate(licensePlate);
                    v.setType(vehicleType);
                    return vehicleRepository.save(v);
                });

        ParkingTransaction tx = new ParkingTransaction();
        tx.setVehicle(vehicle);
        tx.setSpot(spot);
        tx.setEntryTime(LocalDateTime.now());
        tx.setFee(0.0);
        return transactionRepository.save(tx);
    }

    @Override
    @Transactional
    public ParkingTransaction checkOut(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findAll().stream()
                .filter(v -> v.getLicensePlate().equals(licensePlate))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        ParkingTransaction tx = transactionRepository.findAll().stream()
                .filter(t -> t.getVehicle().getId().equals(vehicle.getId()) && t.getExitTime() == null)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Active transaction not found"));
        tx.setExitTime(LocalDateTime.now());
        double fee = calculateFee(tx);
        tx.setFee(fee);
        transactionRepository.save(tx);

        ParkingSpot spot = tx.getSpot();
        spot.setAvailable(true);
        spotRepository.save(spot);
        return tx;
    }

    @Override
    public List<ParkingSpot> getAvailableSpots() {
        return spotRepository.findAll().stream().filter(ParkingSpot::isAvailable).toList();
    }

    @Override
    public double calculateFee(ParkingTransaction transaction) {
        if (transaction.getEntryTime() == null || transaction.getExitTime() == null) return 0.0;
        long hours = Duration.between(transaction.getEntryTime(), transaction.getExitTime()).toHours();
        if (hours == 0) hours = 1;
        double rate = switch (transaction.getSpot().getSize()) {
            case SMALL -> 10.0;
            case MEDIUM -> 20.0;
            case LARGE -> 30.0;
        };
        return hours * rate;
    }

    private SpotSize getRequiredSpotSize(VehicleType type) {
        return switch (type) {
            case BIKE -> SpotSize.SMALL;
            case CAR -> SpotSize.MEDIUM;
            case TRUCK -> SpotSize.LARGE;
        };
    }
}
