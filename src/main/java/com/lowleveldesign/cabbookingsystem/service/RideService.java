package com.lowleveldesign.cabbookingsystem.service;

import com.lowleveldesign.cabbookingsystem.enums.CabType;
import com.lowleveldesign.cabbookingsystem.enums.PaymentMode;
import com.lowleveldesign.cabbookingsystem.exceptions.NoDriverAvailableException;
import com.lowleveldesign.cabbookingsystem.exceptions.ResourceNotFoundException;
import com.lowleveldesign.cabbookingsystem.model.Driver;
import com.lowleveldesign.cabbookingsystem.model.Location;
import com.lowleveldesign.cabbookingsystem.model.Payment;
import com.lowleveldesign.cabbookingsystem.model.Ride;
import com.lowleveldesign.cabbookingsystem.model.Rider;
import com.lowleveldesign.cabbookingsystem.observer.RideObserver;
import com.lowleveldesign.cabbookingsystem.strategy.ridematching.DriverMatchingStrategy;
import com.lowleveldesign.cabbookingsystem.strategy.pricing.PricingStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RideService {

    private final RiderService riderService;
    private final DriverService driverService;
    private final PaymentService paymentService;
    private final DriverMatchingStrategy matchingStrategy;
    private final PricingStrategy pricingStrategy;

    private final ConcurrentHashMap<String, Ride> rides = new ConcurrentHashMap<>();
    private final List<RideObserver> observers = new CopyOnWriteArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public RideService(RiderService riderService, DriverService driverService,
                       PaymentService paymentService, DriverMatchingStrategy matchingStrategy,
                       PricingStrategy pricingStrategy) {
        this.riderService = riderService;
        this.driverService = driverService;
        this.paymentService = paymentService;
        this.matchingStrategy = matchingStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    public void addObserver(RideObserver observer) {
        observers.add(observer);
    }

    public double estimateFare(Location pickup, Location drop, CabType cabType) {
        return pricingStrategy.calculateFare(cabType, pickup.distanceTo(drop));
    }

    /** Creates a ride, then tries candidates nearest-first until one reservation succeeds. */
    public Ride requestRide(String riderId, Location pickup, Location drop, CabType cabType) {
        Rider rider = riderService.getRider(riderId);
        double distanceKm = pickup.distanceTo(drop);
        double fare = pricingStrategy.calculateFare(cabType, distanceKm);

        Ride ride = new Ride("RIDE-" + idCounter.getAndIncrement(), rider, pickup, drop,
                cabType, distanceKm, fare);
        rides.put(ride.getId(), ride);
        notifyObservers(ride);

        List<Driver> candidates =
                matchingStrategy.findCandidates(pickup, cabType, driverService.getAllDrivers());

        for (Driver driver : candidates) {
            if (driver.tryReserve()) {          // atomic: only one rider can win this driver
                ride.assignDriver(driver);
                notifyObservers(ride);
                return ride;
            }
        }

        ride.cancel();
        notifyObservers(ride);
        throw new NoDriverAvailableException(
                "No " + cabType + " driver available near " + pickup + " for " + ride.getId());
    }

    public void startRide(String rideId) {
        Ride ride = getRide(rideId);
        ride.start();
        notifyObservers(ride);
    }

    public Payment completeRide(String rideId, PaymentMode paymentMode) {
        Ride ride = getRide(rideId);
        ride.complete();

        Driver driver = ride.getDriver();
        driver.updateLocation(ride.getDrop()); // driver is now at the drop point
        driver.release();                      // and available for the next ride
        notifyObservers(ride);

        return paymentService.pay(ride, paymentMode);
    }

    public void cancelRide(String rideId) {
        Ride ride = getRide(rideId);
        ride.cancel(); // throws if already started/completed
        Driver driver = ride.getDriver();
        if (driver != null) {
            driver.release();
        }
        notifyObservers(ride);
    }

    public Ride getRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new ResourceNotFoundException("Ride not found: " + rideId);
        }
        return ride;
    }

    public List<Ride> getRideHistory(String riderId) {
        return rides.values().stream()
                .filter(r -> r.getRider().getId().equals(riderId))
                .sorted(Comparator.comparing(Ride::getRequestedAt))
                .collect(Collectors.toList());
    }

    private void notifyObservers(Ride ride) {
        for (RideObserver observer : observers) {
            observer.onRideUpdate(ride);
        }
    }
}
