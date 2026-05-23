package com.lowleveldesign.parkinglot.parkinglot;

import com.lowleveldesign.parkinglot.model.Ticket;
import com.lowleveldesign.parkinglot.model.Vehicle;

public class EntranceGate {

    public Ticket enter(ParkingBuilding parkingBuilding, Vehicle vehicle){
        return parkingBuilding.allocate(vehicle);
    }
}
