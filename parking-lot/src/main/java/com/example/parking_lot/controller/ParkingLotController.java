package com.example.parking_lot.controller;

import com.example.parking_lot.entity.ParkingSpot;
import com.example.parking_lot.entity.ParkingTransaction;
import com.example.parking_lot.entity.SpotSize;
import com.example.parking_lot.entity.VehicleType;
import com.example.parking_lot.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ParkingLotController {
    private final ParkingLotService parkingLotService;

    @Autowired
    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostMapping("/entry")
    public ResponseEntity<?> checkIn(@RequestBody Map<String, String> request) {
        try {
            String licensePlate = request.get("licensePlate");
            VehicleType vehicleType = VehicleType.valueOf(request.get("vehicleType"));
            ParkingTransaction tx = parkingLotService.checkIn(licensePlate, vehicleType);
            return ResponseEntity.ok(tx);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid vehicle type provided.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PostMapping("/exit")
    public ResponseEntity<?> checkOut(@RequestBody Map<String, String> request) {
        try {
            String licensePlate = request.get("licensePlate");
            ParkingTransaction tx = parkingLotService.checkOut(licensePlate);
            return ResponseEntity.ok(tx);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<?> getAvailableSpots() {
        try {
            List<ParkingSpot> spots = parkingLotService.getAvailableSpots();
            if (spots.isEmpty()) {
                return ResponseEntity.ok("No parking spots are currently available.");
            }
            long small = spots.stream().filter(s -> s.getSize() == SpotSize.SMALL).count();
            long medium = spots.stream().filter(s -> s.getSize() == SpotSize.MEDIUM).count();
            long large = spots.stream().filter(s -> s.getSize() == SpotSize.LARGE).count();
            String message = String.format("Available spots - Small: %d, Medium: %d, Large: %d", small, medium, large);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
