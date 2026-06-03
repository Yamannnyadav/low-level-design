package com.lowleveldesign.elevator.strategy;

import com.lowleveldesign.elevator.controller.ElevatorController;
import com.lowleveldesign.elevator.enums.ElevatorDirection;

import java.util.List;

public interface ElevatorSelectionStrategy {

    ElevatorController selectElevator(List<ElevatorController> elevatorControllerList, int reqFloor, ElevatorDirection elevatorDirection);
}
