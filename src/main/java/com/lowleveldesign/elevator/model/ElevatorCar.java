package com.lowleveldesign.elevator.model;

import com.lowleveldesign.elevator.enums.ElevatorDirection;

public class ElevatorCar {

    int elevatorId;
    int currFloor;

    public void setElevatorDirection(ElevatorDirection elevatorDirection) {
        this.elevatorDirection = elevatorDirection;
    }

    public int getCurrFloor() {
        return currFloor;
    }

    public int getNextFloorStoppage() {
        return nextFloorStoppage;
    }

    public ElevatorDirection getElevatorDirection() {
        return elevatorDirection;
    }

    public Door getDoor() {
        return door;
    }

    int nextFloorStoppage;
    ElevatorDirection elevatorDirection;
    Door door;

    public ElevatorCar(int elevatorId){
        this.elevatorId = elevatorId;
        currFloor = 0;
        elevatorDirection = ElevatorDirection.IDLE;
        door = new Door();
    }

    public void showDisplay(){
        System.out.println("elevator: " + elevatorId + " Current Floor: " + currFloor + " going: " + elevatorDirection);
    }

    public void setCurrFloor(int currFloor) {
        this.currFloor = currFloor;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public void moveElevator(int destinationFloor) {

        //this is dump object, so if command has come, to go particular direction and particular floor, it just move
        //no matter what its current state and floor.

        this.nextFloorStoppage = destinationFloor;
        if (this.currFloor == destinationFloor) {
            door.openDoor(elevatorId);
            return;
        }

        int startFloor = this.currFloor;
        door.closeDoor(elevatorId);

        if (nextFloorStoppage >= startFloor) {
            elevatorDirection = ElevatorDirection.UP;
            showDisplay();

            //+1 i am doing bcoz, floor start from 0,1,2.... so if anyone goes from 1st floor to 2nd, so only 1 floor
            //lift has to move, not 2.

            for (int i = startFloor + 1; i <= nextFloorStoppage; i++) {
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    throw new RuntimeException("Exception while sleeping thread: " + e);
                }
                setCurrFloor(i);
                showDisplay();
            }
        } else {
            elevatorDirection = ElevatorDirection.DOWN;
            showDisplay();

            for (int i = startFloor - 1; i >= nextFloorStoppage; i--) {
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    throw new RuntimeException("Exception while sleeping thread: " + e);
                }
                setCurrFloor(i);
                showDisplay();
            }
        }

        door.closeDoor(elevatorId);

        }
    }