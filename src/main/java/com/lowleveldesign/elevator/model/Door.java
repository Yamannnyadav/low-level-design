package com.lowleveldesign.elevator.model;

import com.lowleveldesign.elevator.enums.DoorState;

public class Door {

    DoorState doorState;

    Door(){
        doorState = DoorState.DOOR_CLOSED;
    }

    public void openDoor(int id){
        doorState = DoorState.DOOR_OPENED;
        System.out.println("Opening the Elevator door for elevator id: " + id);
    }

    public void closeDoor(int id){
        doorState = DoorState.DOOR_CLOSED;
        System.out.println("Closing the Elevator door for elevator id: " + id);
    }
}
