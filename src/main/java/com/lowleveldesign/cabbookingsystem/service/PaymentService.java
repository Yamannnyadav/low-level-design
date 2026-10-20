package com.lowleveldesign.cabbookingsystem.service;

import com.lowleveldesign.cabbookingsystem.enums.PaymentMode;
import com.lowleveldesign.cabbookingsystem.enums.PaymentStatus;
import com.lowleveldesign.cabbookingsystem.model.Payment;
import com.lowleveldesign.cabbookingsystem.model.Ride;
import com.lowleveldesign.cabbookingsystem.strategy.payment.CardPaymentProcessor;
import com.lowleveldesign.cabbookingsystem.strategy.payment.CashPaymentProcessor;
import com.lowleveldesign.cabbookingsystem.strategy.payment.PaymentProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PaymentService {

    private final Map<PaymentMode, PaymentProcessor> processors = new ConcurrentHashMap<>();
    private final Map<String, Payment> paymentsByRide = new ConcurrentHashMap<>(); //rideId vs Payment
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public PaymentService() {
        processors.put(PaymentMode.CASH, new CashPaymentProcessor());
        processors.put(PaymentMode.CARD, new CardPaymentProcessor());
    }

    public void registerProcessor(PaymentMode mode, PaymentProcessor processor) {
        processors.put(mode, processor);
    }

    public Payment pay(Ride ride, PaymentMode mode) {
        PaymentProcessor processor = processors.get(mode);
        if (processor == null) {
            throw new IllegalArgumentException("Unsupported payment mode: " + mode);
        }

        boolean success = processor.process(ride.getRider(), ride.getFare());
        Payment payment = new Payment("P-" + idCounter.getAndIncrement(), ride.getId(),
                ride.getFare(), mode, success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        paymentsByRide.put(ride.getId(), payment);
        return payment;
    }

    public Payment getPaymentForRide(String rideId) {
        return paymentsByRide.get(rideId);
    }
}
