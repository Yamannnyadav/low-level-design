package com.lowleveldesign.cabbookingsystem.model;

import com.lowleveldesign.cabbookingsystem.enums.CabType;

public class Cab {
    private final String licensePlate;
    private final CabType cabType;

    public Cab(String licensePlate, CabType cabType) {
        this.licensePlate = licensePlate;
        this.cabType = cabType;
    }

    public String getLicensePlate() { return licensePlate; }
    public CabType getCabType() { return cabType; }
}
