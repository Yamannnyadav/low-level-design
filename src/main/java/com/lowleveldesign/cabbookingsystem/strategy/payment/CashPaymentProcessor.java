package com.lowleveldesign.cabbookingsystem.strategy.payment;

import com.lowleveldesign.cabbookingsystem.model.Rider;

public class CashPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean process(Rider rider, double amount) {
        return true; // collected by driver in person
    }
}
