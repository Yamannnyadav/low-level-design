package com.lowleveldesign.elevator.service;

import com.lowleveldesign.elevator.controller.ElevatorController;
import com.lowleveldesign.elevator.enums.ElevatorDirection;
import com.lowleveldesign.elevator.strategy.ElevatorScheduler;

public class ExternalDispatcher {

    ElevatorScheduler elevatorScheduler;

    public ExternalDispatcher(ElevatorScheduler elevatorScheduler) {
        this.elevatorScheduler = elevatorScheduler;
    }

    public void submitExternalRequest(int floor, ElevatorDirection direction){
        ElevatorController elevatorController = elevatorScheduler.assignElevator(floor, direction);
        elevatorController.submitRequest(floor);
    }
}
