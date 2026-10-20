# DESIGN CAB BOOKING SYSTEM (UBER)

## Problem Statement

Design a Cab Booking system where riders can request a ride between two
locations, the system matches them with a nearby available driver, tracks the
ride through its lifecycle (assigned, started, completed, cancelled), calculates
the fare, and collects payment, while remaining correct under many concurrent
requests (a driver must never be assigned to two riders at once).

---

# Functional Requirements

* Rider should be able to register and request a ride by giving pickup and drop
  locations and a preferred cab type (MINI, SEDAN, SUV).
* Rider should be able to see an estimated fare before booking.
* System should match the rider with the nearest AVAILABLE driver of the requested
  cab type within a configurable radius.
* Driver should be able to go online/offline and update their location.
* Ride should move through a defined lifecycle:
  REQUESTED -> DRIVER_ASSIGNED -> IN_PROGRESS -> COMPLETED, with cancellation
  allowed only before the ride starts.
* System should calculate fare from distance and cab type, with support for
  surge pricing.
* System should collect payment on completion using the chosen payment mode
  (CASH, CARD).
* System should notify the rider on every ride status change.
* Rider should be able to view their ride history.

---

# Non-Functional Requirements

* The system should be thread-safe: a driver must never be assigned to two rides
  concurrently, and conflicting actions on one ride (start vs cancel) must not
  both succeed.
* The design should be extensible: new matching algorithms, pricing models,
  payment modes and notification channels should be addable without modifying
  existing classes.
* Driver matching should be low latency (in-memory; production would use a
  geo-index such as geohash/QuadTree instead of scanning all drivers).
* The fare must be locked at request time (upfront pricing) so later surge
  changes do not alter an accepted ride.

---

# Core Entities

* Rider
* Driver
* Cab
* Location
* Ride
* Payment
* DriverMatchingStrategy (NearestDriver, ...)
* PricingStrategy (DistanceBased, Surge, ...)
* PaymentProcessor (Cash, Card, ...)
* RideObserver (ConsoleNotification, ...)

---

# Entities and Their Relationships

## Location (immutable)

double latitude;
double longitude;
double distanceTo(Location other);

---

## Rider

String id;
String name;
String phone;

---

## Cab

String licensePlate;
CabType cabType;

---

## Driver

String id;
String name;
Cab cab;
Location location; // volatile
DriverStatus status; // AVAILABLE / ON_TRIP / OFFLINE, guarded by driver's monitor

boolean tryReserve(); // atomic AVAILABLE -> ON_TRIP, prevents double assignment
void release();
boolean goOnline();
boolean goOffline();

---

## Ride

String id;
Rider rider;
Driver driver; // null until assigned
Location pickup;
Location drop;
CabType cabType;
double distanceKm;
double fare; // locked at request time
RideStatus status;

Owns its state machine: assignDriver(), start(), complete(), cancel().
Each transition is synchronized and validated.

---

## Payment

String id;
String rideId;
double amount;
PaymentMode mode;
PaymentStatus status;

---

## Enums

CabType: MINI(50, 10), SEDAN(80, 14), SUV(120, 18) // baseFare, perKmRate
DriverStatus: AVAILABLE, ON_TRIP, OFFLINE
RideStatus: REQUESTED, DRIVER_ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
PaymentMode: CASH, CARD
PaymentStatus: SUCCESS, FAILED

---

## DriverMatchingStrategy (interface) - Strategy

List<Driver> findCandidates(Location pickup, CabType type, Collection<Driver> all);

Implementation: NearestDriverMatchingStrategy (available + same cab type + within
radius, sorted by distance). It only ranks; reservation is done by the caller.

---

## PricingStrategy (interface) - Strategy

double calculateFare(CabType type, double distanceKm);

Implementations:
* DistanceBasedPricingStrategy - baseFare + perKmRate * distance
* SurgePricingStrategy - Decorator applying a runtime-adjustable multiplier

---

## PaymentProcessor (interface) - Strategy

boolean process(Rider rider, double amount);

Implementations: CashPaymentProcessor, CardPaymentProcessor.

---

## RideObserver (interface) - Observer

void onRideUpdate(Ride ride);

Implementation: ConsoleNotificationObserver.

---

## Services

* RiderService - register/lookup riders.
* DriverService - register drivers, update location, online/offline.
* PaymentService - maps PaymentMode -> PaymentProcessor, records Payment.
* RideService - orchestrator: estimateFare, requestRide, startRide,
  completeRide, cancelRide, getRideHistory. Notifies observers on each change.

---

# Booking Flow (requestRide)

1. Look up rider, compute distance and fare (locked).
2. Create Ride in REQUESTED state, notify observers.
3. Ask MatchingStrategy for ranked candidate drivers.
4. For each candidate in order, call driver.tryReserve(); first success wins.
5. ride.assignDriver(driver), notify observers.
6. If no candidate could be reserved: cancel ride, throw NoDriverAvailableException.

---

# Relationships Summary

* Rider (1) -requests-> (many) Ride
* Driver (1) -serves-> (many) Ride (one at a time)
* Driver (1) -drives-> (1) Cab
* Ride (1) -has-> (0..1) Payment
* RideService (1) -uses-> (1) DriverMatchingStrategy, (1) PricingStrategy
* RideService (1) -notifies-> (many) RideObserver
* PaymentService (1) -delegates to-> (many) PaymentProcessor

---

# Design Patterns Used

* **Strategy** - matching, pricing and payment are swappable.
* **Decorator** - SurgePricingStrategy wraps any PricingStrategy.
* **Observer** - RideObserver for status notifications.
* **State machine** - Ride validates every transition.
* **Dependency Injection** - RideService receives all collaborators via constructor.

---

# Concurrency Notes

* Driver.tryReserve() is an atomic check-and-set under the driver's monitor, so
  racing riders cannot get the same driver; losers fall through to the next
  candidate, or get NoDriverAvailableException.
* Ride transitions are synchronized, so start vs cancel cannot both succeed.
* Registries use ConcurrentHashMap; observers use CopyOnWriteArrayList.

---

# Possible Extensions

* Geo-index (geohash / QuadTree) for O(log n) nearby-driver lookup.
* Driver accept/reject with timeout, then fall through to the next candidate.
* Ratings, wallet payments, ride-sharing (pool), scheduled rides.