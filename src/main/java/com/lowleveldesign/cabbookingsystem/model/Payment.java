package com.lowleveldesign.cabbookingsystem.model;


import com.lowleveldesign.cabbookingsystem.enums.PaymentMode;
import com.lowleveldesign.cabbookingsystem.enums.PaymentStatus;

public class Payment {

    private final String id;
    private final String rideId;
    private final double amount;
    private final PaymentMode mode;
    private final PaymentStatus status;

    public Payment(String id, String rideId, double amount, PaymentMode mode, PaymentStatus status) {
        this.id = id;
        this.rideId = rideId;
        this.amount = amount;
        this.mode = mode;
        this.status = status;
    }

    public String getId() { return id; }
    public String getRideId() { return rideId; }
    public double getAmount() { return amount; }
    public PaymentMode getMode() { return mode; }
    public PaymentStatus getStatus() { return status; }
}
