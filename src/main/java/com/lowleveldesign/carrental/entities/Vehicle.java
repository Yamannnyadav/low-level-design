package com.lowleveldesign.carrental.entities;

import com.lowleveldesign.carrental.enums.VehicleStatus;
import com.lowleveldesign.carrental.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vehicle {

    private final String number;
    private final int id;
    private final VehicleType vehicleType;
    private volatile VehicleStatus status;
    private int dailyRentalCot;


    public Vehicle(String number, int id, VehicleType vehicleType, int dailyRentalCot) {
        this.number = number;
        this.id = id;
        this.vehicleType = vehicleType;
        this.dailyRentalCot = dailyRentalCot;
        this.status = VehicleStatus.AVAILABLE;
    }
}
