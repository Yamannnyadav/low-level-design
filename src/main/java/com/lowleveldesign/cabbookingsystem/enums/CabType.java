package com.lowleveldesign.cabbookingsystem.enums;

public enum CabType {

    MINI(50.0, 10.0),
    SEDAN(80.0, 14.0),
    SUV(120.0, 18.0);

    private final double baseFare;
    private final double perKmRate;

    CabType(double baseFare, double perKmRate) {
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
    }

    public double getBaseFare() { return baseFare; }
    public double getPerKmRate() { return perKmRate; }
}
