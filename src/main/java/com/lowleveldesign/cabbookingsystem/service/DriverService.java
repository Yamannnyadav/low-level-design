package com.lowleveldesign.cabbookingsystem.service;

import com.lowleveldesign.cabbookingsystem.exceptions.ResourceNotFoundException;
import com.lowleveldesign.cabbookingsystem.model.Cab;
import com.lowleveldesign.cabbookingsystem.model.Driver;
import com.lowleveldesign.cabbookingsystem.model.Location;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DriverService {

    private final ConcurrentHashMap<String, Driver> drivers = new ConcurrentHashMap<>(); //driverId vs Driver
    private final AtomicInteger idCounter = new AtomicInteger(1);

    /** Registers a driver and puts them online immediately. */
    public Driver registerDriver(String name, Cab cab, Location location) {
        Driver driver = new Driver("D-" + idCounter.getAndIncrement(), name, cab, location);
        driver.goOnline();
        drivers.put(driver.getId(), driver);
        return driver;
    }

    public Driver getDriver(String driverId) {
        Driver driver = drivers.get(driverId);
        if (driver == null) {
            throw new ResourceNotFoundException("Driver not found: " + driverId);
        }
        return driver;
    }

    public void updateLocation(String driverId, Location location) {
        getDriver(driverId).updateLocation(location);
    }

    public boolean goOnline(String driverId) {
        return getDriver(driverId).goOnline();
    }

    public boolean goOffline(String driverId) {
        return getDriver(driverId).goOffline();
    }

    public Collection<Driver> getAllDrivers() {
        return drivers.values();
    }
}
