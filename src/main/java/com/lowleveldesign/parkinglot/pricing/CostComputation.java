package com.lowleveldesign.parkinglot.pricing;

import com.lowleveldesign.parkinglot.model.Ticket;

public class CostComputation {

    private final PricingStrategy pricingStrategy;

    public CostComputation(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double compute(Ticket ticket){
        return pricingStrategy.calculate(ticket);
    }
}
