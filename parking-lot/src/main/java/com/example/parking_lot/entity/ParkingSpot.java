package com.example.parking_lot.entity;

import jakarta.persistence.*;

@Entity
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SpotSize size;

    private boolean available;

    private int spotNumber;

    public ParkingSpot() {}

    public ParkingSpot(Long id, SpotSize size, boolean available, int spotNumber) {
        this.id = id;
        this.size = size;
        this.available = available;
        this.spotNumber = spotNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SpotSize getSize() {
        return size;
    }

    public void setSize(SpotSize size) {
        this.size = size;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(int spotNumber) {
        this.spotNumber = spotNumber;
    }
}
