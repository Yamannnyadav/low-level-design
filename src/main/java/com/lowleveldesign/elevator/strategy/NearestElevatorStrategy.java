package com.lowleveldesign.elevator.strategy;

import com.lowleveldesign.elevator.controller.ElevatorController;
import com.lowleveldesign.elevator.enums.ElevatorDirection;

import java.util.List;

public class NearestElevatorStrategy implements ElevatorSelectionStrategy{

    @Override
    public ElevatorController selectElevator(List<ElevatorController> controllers,
                                             int requestFloor,
                                             ElevatorDirection direction) {

        int minDist = Integer.MAX_VALUE;
        ElevatorController best = null;

        //1. Pick the one which is going in same direction and minimum distance from the destination
        for(ElevatorController elevatorController : controllers){
            int nextFloorStoppage = elevatorController.getElevatorCar().getNextFloorStoppage();

            // Good candidate if moving same direction & not passed requested floor
            boolean isSameDirCandidate = elevatorController.getElevatorCar().getElevatorDirection() == direction &&
                    ((direction == ElevatorDirection.UP && nextFloorStoppage <= requestFloor) ||
                            (direction == ElevatorDirection.DOWN && nextFloorStoppage >= requestFloor));

            int dist = Math.abs(nextFloorStoppage - requestFloor);

            if(isSameDirCandidate && dist < minDist){
                minDist = dist;
                best = elevatorController;
            }
        }

        // fallback: if not able to choose, pick the idle one
        if(best == null){
            for(ElevatorController elevatorController : controllers){
                if(elevatorController.getElevatorCar().getElevatorDirection() == ElevatorDirection.IDLE){
                    best = elevatorController;
                    break;
                }
            }

            //reached here means, no list is going in same direction and no lift is IDLE too, then pick any lift
            if(best == null) {
                best = controllers.get(0);
            }

        }

        return best;

    }

}
