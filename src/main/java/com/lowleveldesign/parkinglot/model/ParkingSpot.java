package com.lowleveldesign.parkinglot.model;

public class ParkingSpot {

    private final int parkingSpotId;
    private boolean isFree;


    public ParkingSpot(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public int getParkingSpotId() {
        return parkingSpotId;
    }

    public boolean isSpotFree(){
        return isFree;
    }

    public void occupySpot(){
        isFree = false;
    }

    public void releaseSpot(){
        isFree = true;
    }
}
