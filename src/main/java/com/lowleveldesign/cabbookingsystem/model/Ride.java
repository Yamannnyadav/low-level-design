package com.lowleveldesign.cabbookingsystem.model;

import com.lowleveldesign.cabbookingsystem.enums.CabType;
import com.lowleveldesign.cabbookingsystem.enums.RideStatus;
import com.lowleveldesign.cabbookingsystem.exceptions.InvalidRideStateException;

import java.time.LocalDateTime;

public class Ride {
    private final String id;
    private final Rider rider;
    private final Location pickup;
    private final Location drop;
    private final CabType cabType;
    private final double distanceKm;
    private final double fare; // locked at request time (upfront pricing)
    private final LocalDateTime requestedAt = LocalDateTime.now();

    private RideStatus status = RideStatus.REQUESTED;
    private Driver driver;

    public Ride(String id, Rider rider, Location pickup, Location drop,
                CabType cabType, double distanceKm, double fare) {
        this.id = id;
        this.rider = rider;
        this.pickup = pickup;
        this.drop = drop;
        this.cabType = cabType;
        this.distanceKm = distanceKm;
        this.fare = fare;
    }

    public synchronized void assignDriver(Driver driver) {
        requireStatus(RideStatus.REQUESTED, "assign a driver to");
        this.driver = driver;
        this.status = RideStatus.DRIVER_ASSIGNED;
    }

    public synchronized void start() {
        requireStatus(RideStatus.DRIVER_ASSIGNED, "start");
        this.status = RideStatus.IN_PROGRESS;
    }

    public synchronized void complete() {
        requireStatus(RideStatus.IN_PROGRESS, "complete");
        this.status = RideStatus.COMPLETED;
    }

    public synchronized void cancel() {
        if (status != RideStatus.REQUESTED && status != RideStatus.DRIVER_ASSIGNED) {
            throw new InvalidRideStateException("Cannot cancel ride " + id + " in state " + status);
        }
        this.status = RideStatus.CANCELLED;
    }

    private void requireStatus(RideStatus expected, String action) {
        if (status != expected) {
            throw new InvalidRideStateException(
                    "Cannot " + action + " ride " + id + " in state " + status + " (expected " + expected + ")");
        }
    }

    public synchronized RideStatus getStatus() { return status; }
    public synchronized Driver getDriver() { return driver; }

    public String getId() { return id; }
    public Rider getRider() { return rider; }
    public Location getPickup() { return pickup; }
    public Location getDrop() { return drop; }
    public CabType getCabType() { return cabType; }
    public double getDistanceKm() { return distanceKm; }
    public double getFare() { return fare; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
}
