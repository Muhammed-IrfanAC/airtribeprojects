# Smart Parking Lot Backend System

## Overview

This project is a Spring Boot backend for a smart parking lot system. It manages vehicle entry and exit, allocates parking spaces based on vehicle type and availability, and calculates parking fees. The system is designed to support multiple vehicle types and is easily extensible for future features.

---

## Features

- **Parking Spot Allocation:** Assigns available spots based on vehicle size and availability.
- **Check-In and Check-Out:** Records entry and exit times for each vehicle.
- **Parking Fee Calculation:** Calculates fees based on duration and vehicle type.
- **Real-Time Availability Update:** Updates spot availability as vehicles enter and exit.
- **Error Handling:** All API endpoints return meaningful error messages and HTTP status codes.
- **Concurrent Access:** Designed to handle multiple vehicles entering and exiting at the same time.
- **Extensible Architecture:** Built with a layered architecture (Controller, Service, Repository, Entity) for easy maintenance and future enhancements.

---

## System Architecture

### Layers

- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains business logic (spot allocation, fee calculation, etc.).
- **Repository:** Manages database access (Spring Data JPA).
- **Entity:** Maps to database tables (ParkingSpot, Vehicle, ParkingTransaction).

---

## Data Model

### Entities

- **ParkingSpot**
    - `id` (Long)
    - `floorNumber` (int)
    - `spotNumber` (int)
    - `size` (enum: MOTORCYCLE, CAR, BUS)
    - `isAvailable` (boolean)

- **Vehicle**
    - `licensePlate` (String)
    - `type` (enum: MOTORCYCLE, CAR, BUS)

- **ParkingTransaction**
    - `id` (Long)
    - `vehicle` (Vehicle reference)
    - `parkingSpot` (ParkingSpot reference)
    - `entryTime` (Timestamp)
    - `exitTime` (Timestamp, nullable)
    - `fee` (BigDecimal, nullable)

---

## Key Business Logic

- **Spot Allocation:** On entry, find and lock the nearest available spot matching vehicle size. Prevent double allocation using database transactions or locking.
- **Check-In:** Create a ParkingTransaction, mark spot as unavailable.
- **Check-Out:** Calculate fee, update transaction, mark spot as available.
- **Fee Calculation:** Based on duration and vehicle type (configurable rates).

---

## API Endpoints

- `POST /api/entry` — Vehicle entry, returns assigned spot.
- `POST /api/exit` — Vehicle exit, returns fee.
- `GET /api/availability` — Query available spots by floor and size.

All endpoints must handle errors (e.g., no available spot, invalid vehicle, transaction not found) with appropriate HTTP status codes and messages.

---

## API Usage Examples

### 1. Vehicle Entry
**POST /api/entry**

Request body:
```
{
  "licensePlate": "KA01AB1234",
  "vehicleType": "CAR"
}
```
Response (success):
```
{
  "id": 1,
  "vehicle": { ... },
  "spot": { ... },
  "entryTime": "2025-08-03T12:34:56",
  "exitTime": null,
  "fee": 0.0
}
```
Response (error, no spot):
```
{
  "code": 409,
  "message": "No available spot"
}
```

### 2. Vehicle Exit
**POST /api/exit**

Request body:
```
{
  "licensePlate": "KA01AB1234"
}
```
Response (success):
```
{
  "id": 1,
  "vehicle": { ... },
  "spot": { ... },
  "entryTime": "2025-08-03T12:34:56",
  "exitTime": "2025-08-03T14:00:00",
  "fee": 40.0
}
```
Response (error, not found):
```
{
  "code": 404,
  "message": "Vehicle not found"
}
```

### 3. Availability
**GET /api/availability**

Response (success):
```
Available spots - Small: 3, Medium: 2, Large: 1
```
Response (no spots):
```
No parking spots are currently available.
```

---

## Error Response Format
All errors are returned as JSON objects:
```
{
  "code": <HTTP status code>,
  "message": "<error message>"
}
```

---

## Conclusion

This project provides a robust, maintainable, and scalable Spring Boot backend for a smart parking lot, with clear separation of concerns and error handling, and is designed with future extensibility in mind.
