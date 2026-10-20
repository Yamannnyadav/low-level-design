package com.lowleveldesign.cabbookingsystem.strategy.pricing;

import com.lowleveldesign.cabbookingsystem.enums.CabType;

public class DistanceBasedPricingStrategy implements PricingStrategy {

    @Override
    public double calculateFare(CabType cabType, double distanceKm) {
        double fare = cabType.getBaseFare() + cabType.getPerKmRate() * distanceKm;
        return Math.round(fare * 100.0) / 100.0;
    }
}
