package com.lowleveldesign.elevator.strategy;

import com.lowleveldesign.elevator.controller.ElevatorController;
import com.lowleveldesign.elevator.enums.ElevatorDirection;

import java.util.List;

public class ElevatorScheduler {

    private final List<ElevatorController> elevatorControllerList;
    private ElevatorSelectionStrategy elevatorSelectionStrategy;

    public ElevatorScheduler(List<ElevatorController> elevatorControllerList, ElevatorSelectionStrategy elevatorSelectionStrategy) {
        this.elevatorControllerList = elevatorControllerList;
        this.elevatorSelectionStrategy = elevatorSelectionStrategy;
    }

    public void setElevatorSelectionStrategy(ElevatorSelectionStrategy elevatorSelectionStrategy) {
        this.elevatorSelectionStrategy = elevatorSelectionStrategy;
    }

    public ElevatorController assignElevator(int floor, ElevatorDirection direction){
        return elevatorSelectionStrategy.selectElevator(elevatorControllerList, floor, direction);
    }
}
