package com.lowleveldesign.parkinglot.payment;

public class UPIPayment implements Payment{

    @Override
    public boolean pay(double amount){
        System.out.println("Paid by UPI");
        return true;
    }

}
