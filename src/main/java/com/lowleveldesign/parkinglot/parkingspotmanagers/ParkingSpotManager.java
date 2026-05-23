package com.lowleveldesign.parkinglot.parkingspotmanagers;

import com.lowleveldesign.parkinglot.model.ParkingSpot;
import com.lowleveldesign.parkinglot.parkinglookupstrategy.ParkingLookupStrategy;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ParkingSpotManager {

    private final List<ParkingSpot> parkingSpotList;
    private final ParkingLookupStrategy parkingLookupStrategy;
    private final ReentrantLock reentrantLock = new ReentrantLock(true);

    public ParkingSpotManager(List<ParkingSpot> parkingSpotList, ParkingLookupStrategy parkingLookupStrategy) {
        this.parkingSpotList = parkingSpotList;
        this.parkingLookupStrategy = parkingLookupStrategy;
    }

    public ParkingSpot park() {
        reentrantLock.lock();
        try{
            ParkingSpot spot = parkingLookupStrategy.findParkingSpot(parkingSpotList);
            if(spot == null){
                return null;
            }
            spot.occupySpot();
            return spot;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void unPark(ParkingSpot spot) {
        reentrantLock.lock();
        try {
            spot.releaseSpot();
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean hasFreeSpot(){
        reentrantLock.lock();
        try{
            return parkingSpotList.stream().anyMatch(ParkingSpot::isSpotFree);
        } finally {
            reentrantLock.unlock();
        }
    }


}
