package com.lowleveldesign.cabbookingsystem.model;


import com.lowleveldesign.cabbookingsystem.enums.DriverStatus;

public class Driver {

    private final String id;
    private final String name;
    private final Cab cab;
    private volatile Location location;
    private DriverStatus status = DriverStatus.OFFLINE; // guarded by 'this'

    public Driver(String id, String name, Cab cab, Location location) {
        this.id = id;
        this.name = name;
        this.cab = cab;
        this.location = location;
    }

    /** Atomically move AVAILABLE -> ON_TRIP. Returns false if someone else got there first. */
    public synchronized boolean tryReserve() {
        if (status != DriverStatus.AVAILABLE) {
            return false;
        }
        status = DriverStatus.ON_TRIP;
        return true;
    }

    //this method to be called when driver is about to end a ride to make him available again.
    public synchronized void release() {
        if (status == DriverStatus.ON_TRIP) {
            status = DriverStatus.AVAILABLE;
        }
    }

    public synchronized boolean goOnline() {
        if (status != DriverStatus.OFFLINE) {
            return false;
        }
        status = DriverStatus.AVAILABLE;
        return true;
    }

    /** A driver cannot go offline in the middle of a trip. */
    public synchronized boolean goOffline() {
        if (status != DriverStatus.AVAILABLE) {
            return false;
        }
        status = DriverStatus.OFFLINE;
        return true;
    }

    public synchronized DriverStatus getStatus() { return status; }

    public void updateLocation(Location location) { this.location = location; }
    public Location getLocation() { return location; }
    public String getId() { return id; }
    public String getName() { return name; }
    public Cab getCab() { return cab; }
}
