### **Parking Lot Design**

**Problem Statement:**
We need to design a parking lot system where user can park/unpark the vehicle at a designated parking spot, generates ticket, find available parking spots based on
vehicle type.

**Requirements:**

Functional Requirements:
1. Find available parking spots.
2. Park/Unpark vehicle.
3. Generate parking ticket.
4. Charge user per hour.
5. Support multiple vehicle type.

Non-Functional Requirements:
1. Thread safe slot assignment for multiple vehicles entering concurrently.
2. Extensible for new vehicle type and charging system.

**Process Flow Chart**

Entry Process Flow:
- User enters in the parking lot.
- Parking Lot manager finds an available spot based on vehicle type.
- Assigned parking lot and ticket generated with timestamp.

Exit Process Flow:
- User exists parking lot.
- Fare is calculated on hour basis for number of hours car was parked in the lot.
- Payment is made based on total fare.
- Parking lot manager makes the lot available for other vehicles.

**Core entities & their relationships:**

1. Vehicle
2. Parking Lot
3. Parking Level
4. Entry Gate
5. Exit Gate
6. Ticket
7. Payment

Class Breakdown:

Vehicle (abstract)
- vehicleId
- vehicleType
- otherDetails like vehicleNum, model etc
+ getters and setters
Subclasses: Enum VehicleType

VehicleType (enum)
- TWO_WHEELER
- FOUR_WHEELER

ParkingSpot (this is the space where vehicle is actually parked)
- parkingSpotId
- isFree

ParkingSpotManager (abstract class)
- ParkingSpot park() follows ParkingSpotLookupStrategy
- unpark()
- hasFreeSpots()
Subclasses: TwoWheelerSpotManager, FourWheelerSpotManager

TwoWheelerSpotManager
- List<ParkingSpot> spots
- ParkingSpotLookupStrategy parkingStrategy
- implements all methods of ParkingSpotManager

FourWheelerSpotManager
- List<ParkingSpot> spots
- ParkingSpotLookupStrategy parkingStrategy
- implements all methods of ParkingSpotManager

ParkingSpotLookupStrategy (interface)
- ParkingSpot selectSpot(List<ParkingSpot> parkingSpots)
Subclasses: RandomSpotLookupStrategy, NearToEntranceSpotLookupStrategy

ParkingLevel
- Map<VehicleType, ParkingSpotManager> vehicleVsParkingManagerMap
- levelNum
- boolean hasAvailiability()
- ParkingSpot park(VehicleType type)
- void unpark(VehicleType type, ParkingSpot spot)

ParkingBuilding
- List<ParkingLevel> parkingLevels
+ allocateTicket
+ releseSpot()

ParkingLot
- ParkingBuilding building
- EntranceGate entryGate

Ticket: 
- ticketId
- timestamp
- Vehicle
- Parking Spot
- ParkingLevel
- other details like date etc
+ getters and setters
Subclasses: 




