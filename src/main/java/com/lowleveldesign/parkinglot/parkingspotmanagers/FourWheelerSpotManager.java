package com.lowleveldesign.parkinglot.parkingspotmanagers;

import com.lowleveldesign.parkinglot.model.ParkingSpot;
import com.lowleveldesign.parkinglot.parkinglookupstrategy.ParkingLookupStrategy;

import java.util.List;

public class FourWheelerSpotManager extends ParkingSpotManager {

    /*
    1. Maintains a list of Two Wheeler Spots only
    2. Has its own lookup strategy
    3. Has its own lock, to avoid conflicts with other spot managers
    */
    public FourWheelerSpotManager(List<ParkingSpot> parkingSpotList, ParkingLookupStrategy parkingLookupStrategy) {
        super(parkingSpotList, parkingLookupStrategy);
    }
}
