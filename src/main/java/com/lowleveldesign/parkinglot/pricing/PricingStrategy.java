package com.lowleveldesign.parkinglot.pricing;

import com.lowleveldesign.parkinglot.model.Ticket;

public interface PricingStrategy {

    public double calculate(Ticket ticket);
}
