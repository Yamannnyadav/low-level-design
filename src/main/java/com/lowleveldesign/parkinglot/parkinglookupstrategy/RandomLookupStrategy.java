package com.lowleveldesign.parkinglot.parkinglookupstrategy;

import com.lowleveldesign.parkinglot.model.ParkingSpot;

import java.util.List;

public class RandomLookupStrategy implements ParkingLookupStrategy{

    @Override
    public ParkingSpot findParkingSpot(List<ParkingSpot> spot){
        return new ParkingSpot(891);
    }
}
