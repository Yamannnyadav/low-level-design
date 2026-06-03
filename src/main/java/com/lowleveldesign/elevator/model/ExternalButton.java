package com.lowleveldesign.elevator.model;

import com.lowleveldesign.elevator.enums.ElevatorDirection;
import com.lowleveldesign.elevator.service.ExternalDispatcher;

public class ExternalButton {

    ExternalDispatcher externalDispatcher;

    public ExternalButton(ExternalDispatcher externalDispatcher) {
        this.externalDispatcher = externalDispatcher;
    }

    public void pressButton(int floor, ElevatorDirection elevatorDirection){
        externalDispatcher.submitExternalRequest(floor, elevatorDirection);
    }

}
