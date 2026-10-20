package com.lowleveldesign.cabbookingsystem.strategy.pricing;

import com.lowleveldesign.cabbookingsystem.enums.CabType;

public class SurgePricingStrategy implements PricingStrategy {

    private final PricingStrategy delegate;
    private volatile double multiplier;

    public SurgePricingStrategy(PricingStrategy delegate, double multiplier) {
        this.delegate = delegate;
        setMultiplier(multiplier);
    }

    public void setMultiplier(double multiplier) {
        if (multiplier < 1.0) throw new IllegalArgumentException("Surge multiplier must be >= 1.0");
        this.multiplier = multiplier;
    }

    public double getMultiplier() { return multiplier; }

    @Override
    public double calculateFare(CabType cabType, double distanceKm) {
        double fare = delegate.calculateFare(cabType, distanceKm) * multiplier;
        return Math.round(fare * 100.0) / 100.0;
    }
}
