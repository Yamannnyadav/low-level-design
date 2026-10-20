package com.lowleveldesign.cabbookingsystem.execute;

import com.lowleveldesign.cabbookingsystem.enums.CabType;
import com.lowleveldesign.cabbookingsystem.enums.PaymentMode;
import com.lowleveldesign.cabbookingsystem.exceptions.InvalidRideStateException;
import com.lowleveldesign.cabbookingsystem.exceptions.NoDriverAvailableException;
import com.lowleveldesign.cabbookingsystem.model.Cab;
import com.lowleveldesign.cabbookingsystem.model.Location;
import com.lowleveldesign.cabbookingsystem.model.Payment;
import com.lowleveldesign.cabbookingsystem.model.Ride;
import com.lowleveldesign.cabbookingsystem.model.Rider;
import com.lowleveldesign.cabbookingsystem.observer.ConsoleNotificationObserver;
import com.lowleveldesign.cabbookingsystem.service.DriverService;
import com.lowleveldesign.cabbookingsystem.service.PaymentService;
import com.lowleveldesign.cabbookingsystem.service.RideService;
import com.lowleveldesign.cabbookingsystem.service.RiderService;
import com.lowleveldesign.cabbookingsystem.strategy.ridematching.NearestDriverMatchingStrategy;
import com.lowleveldesign.cabbookingsystem.strategy.pricing.DistanceBasedPricingStrategy;
import com.lowleveldesign.cabbookingsystem.strategy.pricing.SurgePricingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecuteCabBookingSystem {

    public static void main(String[] args) throws Exception {

        // ---------- Wiring (constructor injection, no globals) ----------
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        PaymentService paymentService = new PaymentService();
        SurgePricingStrategy pricing = new SurgePricingStrategy(new DistanceBasedPricingStrategy(), 1.0);
        RideService rideService = new RideService(riderService, driverService, paymentService,
                new NearestDriverMatchingStrategy(10.0), pricing);
        rideService.addObserver(new ConsoleNotificationObserver());

        // ---------- Data ----------
        Location cyberHub = new Location(28.4950, 77.0890);
        Location mgRoad = new Location(28.4800, 77.0800);
        Location airport = new Location(28.5562, 77.1000);

        driverService.registerDriver("Ravi", new Cab("HR26AB1111", CabType.MINI), new Location(28.4940, 77.0880));
        driverService.registerDriver("Sunil", new Cab("HR26CD2222", CabType.SEDAN), new Location(28.4960, 77.0900));
        driverService.registerDriver("Amit", new Cab("HR26EF3333", CabType.SEDAN), new Location(28.4850, 77.0850));
        driverService.registerDriver("Vikram", new Cab("HR26GH4444", CabType.SUV), new Location(28.4900, 77.0870));

        Rider alice = riderService.registerRider("Alice", "9000000001");
        Rider bob = riderService.registerRider("Bob", "9000000002");

        // ---------- Scenario 1: happy path ----------
        System.out.println("=== Scenario 1: Alice books a SEDAN (full lifecycle) ===");
        System.out.printf("Estimated fare: %.2f%n", rideService.estimateFare(cyberHub, airport, CabType.SEDAN));
        Ride ride1 = rideService.requestRide(alice.getId(), cyberHub, airport, CabType.SEDAN);
        rideService.startRide(ride1.getId());
        Payment payment = rideService.completeRide(ride1.getId(), PaymentMode.CARD);
        System.out.printf("Distance: %.2f km | Fare: %.2f | Payment %s via %s%n",
                ride1.getDistanceKm(), ride1.getFare(), payment.getStatus(), payment.getMode());

        // ---------- Scenario 2: cancellation ----------
        System.out.println("\n=== Scenario 2: Bob books a MINI, then cancels ===");
        Ride ride2 = rideService.requestRide(bob.getId(), cyberHub, mgRoad, CabType.MINI);
        rideService.cancelRide(ride2.getId());
        try {
            rideService.startRide(ride2.getId());
        } catch (InvalidRideStateException e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }

        // ---------- Scenario 3: surge pricing ----------
        System.out.println("\n=== Scenario 3: Surge pricing ===");
        System.out.printf("Normal estimate (SUV): %.2f%n", rideService.estimateFare(cyberHub, airport, CabType.SUV));
        pricing.setMultiplier(2.0);
        System.out.printf("2.0x surge estimate (SUV): %.2f%n", rideService.estimateFare(cyberHub, airport, CabType.SUV));
        pricing.setMultiplier(1.0);

        // ---------- Scenario 4: concurrency ----------
        System.out.println("\n=== Scenario 4: 5 riders race for 2 available SEDAN drivers ===");
        List<Rider> racers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            racers.add(riderService.registerRider("Racer-" + i, "900000010" + i));
        }

        ExecutorService pool = Executors.newFixedThreadPool(5);
        CountDownLatch startGun = new CountDownLatch(1);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        for (Rider racer : racers) {
            pool.submit(() -> {
                try {
                    startGun.await();
                    rideService.requestRide(racer.getId(), cyberHub, mgRoad, CabType.SEDAN);
                    success.incrementAndGet();
                } catch (NoDriverAvailableException e) {
                    failed.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        startGun.countDown();
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Booked: " + success.get() + " | No driver: " + failed.get()
                + " (expected 2 and 3 - no driver is ever double-assigned)");

        System.out.println("\nAlice's ride history: " + rideService.getRideHistory(alice.getId()).size() + " ride(s)");
    }
}
