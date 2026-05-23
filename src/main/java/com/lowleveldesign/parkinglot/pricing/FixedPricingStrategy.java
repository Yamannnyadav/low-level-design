package com.lowleveldesign.parkinglot.pricing;

import com.lowleveldesign.parkinglot.model.Ticket;

public class FixedPricingStrategy implements PricingStrategy {

    @Override
    public double calculate(Ticket ticket){
        return 100;
    }
}
