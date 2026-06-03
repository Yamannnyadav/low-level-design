package com.lowleveldesign.elevator.service;

import com.lowleveldesign.elevator.controller.ElevatorController;

public class InternalDispatcher {

    private static InternalDispatcher INSTANCE = new InternalDispatcher();

    private InternalDispatcher(){

    }

    public static InternalDispatcher getInstance(){
        return INSTANCE;
    }

    public void submitInternalRequest(int destFloor, ElevatorController controller){
        controller.submitRequest(destFloor);
    }
}
