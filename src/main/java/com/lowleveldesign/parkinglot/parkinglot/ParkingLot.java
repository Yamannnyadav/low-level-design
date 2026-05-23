package com.lowleveldesign.parkinglot.parkinglot;

import com.lowleveldesign.parkinglot.model.Ticket;
import com.lowleveldesign.parkinglot.model.Vehicle;
import com.lowleveldesign.parkinglot.payment.Payment;

public class ParkingLot {

    private final ParkingBuilding parkingBuilding;
    private final EntranceGate entranceGate;
    private final ExitGate exitGate;

    public ParkingLot(ParkingBuilding parkingBuilding, EntranceGate entranceGate, ExitGate exitGate) {
        this.parkingBuilding = parkingBuilding;
        this.entranceGate = entranceGate;
        this.exitGate = exitGate;
    }

    public Ticket vehicleArrives(Vehicle vehicle){
        return entranceGate.enter(parkingBuilding, vehicle);
    }

    public void vehicleExits(Ticket ticket, Payment payment){
        exitGate.completeExit(parkingBuilding, ticket, payment);
    }


}
