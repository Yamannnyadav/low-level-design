package com.lowleveldesign.parkinglot.parkinglot;

import com.lowleveldesign.parkinglot.model.ParkingSpot;
import com.lowleveldesign.parkinglot.model.Ticket;
import com.lowleveldesign.parkinglot.model.Vehicle;
import com.lowleveldesign.parkinglot.pricing.CostComputation;

import java.util.List;

public class ParkingBuilding {

    private final List<ParkingLevel> parkingLevelList;
    private final CostComputation costComputation;

    public ParkingBuilding(List<ParkingLevel> parkingLevelList, CostComputation costComputation) {
        this.parkingLevelList = parkingLevelList;
        this.costComputation = costComputation;
    }

    public Ticket allocate(Vehicle vehicle){
        for(ParkingLevel parkingLevel : parkingLevelList){
            if(parkingLevel.hasAvailability(vehicle.getVehicleType())){
                ParkingSpot spot = parkingLevel.park(vehicle.getVehicleType());
                if(spot != null){
                    Ticket ticket = new Ticket(vehicle, parkingLevel, spot);
                    System.out.println("Parking has been allocated for vehicle: " + vehicle.getVehicleNumber() + " at level number: " +
                            parkingLevel.getLevelNum() + " at spot: " + spot.getParkingSpotId());
                    return ticket;
                }
            }
        }
        throw new RuntimeException("Parking Full");
    }

    public void release(Ticket ticket){
        ticket.getParkingLevel().unPark(ticket.getVehicle().getVehicleType(), ticket.getParkingSpot());
    }
}
