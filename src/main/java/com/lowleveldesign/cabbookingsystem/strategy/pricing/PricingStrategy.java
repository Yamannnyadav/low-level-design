package com.lowleveldesign.cabbookingsystem.strategy.pricing;

import com.lowleveldesign.cabbookingsystem.enums.CabType;

public interface PricingStrategy {
    double calculateFare(CabType cabType, double distanceKm);
}
