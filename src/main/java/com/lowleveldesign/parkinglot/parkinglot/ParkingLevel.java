package com.lowleveldesign.parkinglot.parkinglot;

import com.lowleveldesign.parkinglot.enumclasses.VehicleType;
import com.lowleveldesign.parkinglot.model.ParkingSpot;
import com.lowleveldesign.parkinglot.parkingspotmanagers.ParkingSpotManager;

import java.util.Map;

public class ParkingLevel {

    private final int levelNum;
    private final Map<VehicleType, ParkingSpotManager> managerMap;

    public int getLevelNum() {
        return levelNum;
    }

    public ParkingLevel(int levelNum, Map<VehicleType, ParkingSpotManager> managerMap) {
        this.levelNum = levelNum;
        this.managerMap = managerMap;
    }

    public boolean hasAvailability(VehicleType type){
        ParkingSpotManager manager = managerMap.get(type);
        return manager != null && manager.hasFreeSpot();
    }

    public ParkingSpot park(VehicleType type){
        ParkingSpotManager manager = managerMap.get(type);
        if(manager == null){
            throw new IllegalArgumentException("No parking manager exist for vehicle type: " +  type);
        }
        return manager.park();
    }

    public void unPark(VehicleType type, ParkingSpot spot){
        ParkingSpotManager manager = managerMap.get(type);
        if(manager == null){
            throw new IllegalArgumentException("No parking manager exist for vehicle type: " +  type);
        }
        manager.unPark(spot);
    }
}
