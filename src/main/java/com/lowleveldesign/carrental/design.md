# CAR RENTAL SYSTEM

**Problem Statement**:
We need to design a car rental system where user can rent a car on hourly basis.

**Functional Requirements**:
- User should be able to see pool of cars available for rent.
- User should be able to choose a store based on location.
- User should be able to rent a car.
- User should be able to start and end the trip.
- User should be able to make payments for reservations.


**Non-Function Requirements**:
- User should be able to view pool of cars with low latency.
- System should be scalable enough to fulfil huge number of requests.
- System should be consistent (means one car shouldn't be allotted to 2 different users on the same day).


**Core Entities**:
- User
- Vehicle
- Vehicle Store
- Reservation
- Payment
- Location
- Bill


**Entities and their relationship:**

User
- id
- name
- location
- bookingDetails

Vehicle
- number
- id
- type (two or four wheeler)
- vehicleStatus
- hourlyRate

VehicleStore
- id
- location
- inventoryManager
- BillManager billManager
- ReservationManager reservationManager
- PaymentManager paymentManager

Reservation
- id
- vehicleId
- startDate
- userId
- endDate
- enum reservationStatus
- reservationType

Payment
- id
- billId
- amount
- PaymentMode paymentMode
- Date paymentDate

Bill
- amount
- id
- reservationId
- boolean billPaid

Location
- city
- longitude
- latitude


**Patterns Used**:
- Strategy pattern will be used to charge user based on hourly reservation.