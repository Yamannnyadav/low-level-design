package com.lowleveldesign.parkinglot.parkinglookupstrategy;

import com.lowleveldesign.parkinglot.model.ParkingSpot;

import java.util.List;

public interface ParkingLookupStrategy {

    ParkingSpot findParkingSpot(List<ParkingSpot> spot);

}
