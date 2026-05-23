package com.lowleveldesign.parkinglot.parkinglot;

import com.lowleveldesign.parkinglot.model.Ticket;
import com.lowleveldesign.parkinglot.payment.Payment;
import com.lowleveldesign.parkinglot.pricing.CostComputation;

public class ExitGate {

    private final CostComputation costComputation;

    public ExitGate(CostComputation costComputation) {
        this.costComputation = costComputation;
    }

    public void completeExit(ParkingBuilding parkingBuilding, Ticket ticket, Payment payment){

        double amount = costComputation.compute(ticket);
        boolean isPaid = payment.pay(amount);
        if(isPaid){
            parkingBuilding.release(ticket);
            System.out.println("Payment Successful, Gate Opened");
        }
        else{
            throw new RuntimeException("Payment is not cleared");
        }
    }
}
