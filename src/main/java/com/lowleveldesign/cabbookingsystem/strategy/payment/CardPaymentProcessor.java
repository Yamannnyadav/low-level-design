package com.lowleveldesign.cabbookingsystem.strategy.payment;

import com.lowleveldesign.cabbookingsystem.model.Rider;

public class CardPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean process(Rider rider, double amount) {
        // Simulated payment-gateway call; a real impl would call an external API.
        return amount > 0;
    }
}
