package com.lowleveldesign.parkinglot.parkinglookupstrategy;

import com.lowleveldesign.parkinglot.model.ParkingSpot;

import java.util.List;

public class NearToEntranceLookupStrategy implements ParkingLookupStrategy{

    @Override
    public ParkingSpot findParkingSpot(List<ParkingSpot> spot){
        return new ParkingSpot(123);
    }

}
