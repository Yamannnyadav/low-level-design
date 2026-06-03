# **Elevator**

**Problem Description**: We need to design an elevator which can functionally go UP and DOWN as per user's requirment.

**Functional Requirements:**

- Take inputs from user to go UP and DOWN and best elevator that can serve the request will address the request.
- Once user enter elevator, he/she should be able to choose destination floor from inside.
- It should have a max capacity.
- It should follow strategy to serve requests.
- Elevators should remain idle when there are no requests.
- Elevator should display this to user: current floor and movement direction


**Non-Functional Requirements:**

- System should scale to support multiple elevators.

**Process Flow:**

- User provides elevator request for:
  UP direction
  DOWN direction
- The system selects the most suitable elevator based on the scheduling strategy.
- The selected elevator moves to the requested floor.
- Once the elevator arrives, the user enters the elevator.
- User selects the destination floor from inside the elevator.
- Elevator processes internal requests and moves accordingly.
- Elevator stops at the destination floor.
- User exits the elevator.
- Elevator becomes IDLE if there are no pending requests.


**Core entities & their relationships:**

- User
- Elevator Car: simple POJO class
- Elevator Controller
- Display
- Internal buttons
- External buttons
- Internal Dispatcher
- External Dispatcher: bridge between External Buttons and Elevator Controller
- Elevator Scheduler: Maintains the List of Elevator Controllers + also has Elevator Selection Strategy to choose the
  best Elevator to serve the request

Class Breakdown:

User
- int userId
- int elevatorId
- other details

Door
- enum DoorState
+ openDoor()
+ closeDoor()

ElevatorCar
- int elevatorId
- int currFloor
- Door door
- ElevatorDirection elevatorDir
+ moveElevator(Floor destFloor)

ElevatorController
- ElevatorCar elevatorCars
- PriorityQueue upMinPQ
- PriorityQueue downMaxPQ

ElevatorDispatcher
- List<ElevatorController> elevatorControllerList
- ElevatorSelectionStrategy elevatorSelectionStrategy
+ ElevatorSelectionStrategy setStrategy(ElevatorSelectionStrategy str)
+ ElevatorController assignElevator(int floor, ElevatorDirection elevatorDir)

ElevatorSelectionStrategy
