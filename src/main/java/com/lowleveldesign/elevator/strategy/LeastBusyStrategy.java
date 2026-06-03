package com.lowleveldesign.elevator.strategy;

import com.lowleveldesign.elevator.controller.ElevatorController;
import com.lowleveldesign.elevator.enums.ElevatorDirection;

import java.util.List;

public class LeastBusyStrategy implements ElevatorSelectionStrategy{

    @Override
    public ElevatorController selectElevator(List<ElevatorController> controllers,
                                             int requestFloor,
                                             ElevatorDirection direction) {

        ElevatorController best = null;
        int minLoad = Integer.MAX_VALUE;

        for(ElevatorController elevatorController : controllers){
            int load = elevatorController.getMinUpPQ().size() + elevatorController.getMaxDownPQ().size();
            if(load < minLoad){
                minLoad = load;
                best = elevatorController;
            }
        }

        return best;
    }


    }
