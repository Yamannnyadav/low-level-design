package com.lowleveldesign.parkinglot.model;

import com.lowleveldesign.parkinglot.parkinglot.ParkingLevel;

import java.time.LocalDateTime;

public class Ticket {

    private LocalDateTime entryTime;
    private final Vehicle vehicle;
    private final ParkingLevel parkingLevel;
    private final ParkingSpot parkingSpot;

    public Ticket(Vehicle vehicle, ParkingLevel parkingLevel, ParkingSpot parkingSpot) {
        this.vehicle = vehicle;
        this.parkingLevel = parkingLevel;
        this.parkingSpot = parkingSpot;
        this.entryTime = LocalDateTime.now();
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public ParkingLevel getParkingLevel() {
        return parkingLevel;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }
}
