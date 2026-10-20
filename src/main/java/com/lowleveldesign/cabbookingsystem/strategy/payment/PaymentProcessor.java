package com.lowleveldesign.cabbookingsystem.strategy.payment;

import com.lowleveldesign.cabbookingsystem.model.Rider;

public interface PaymentProcessor {
    boolean process(Rider rider, double amount);
}
