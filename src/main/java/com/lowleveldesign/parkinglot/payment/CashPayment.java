package com.lowleveldesign.parkinglot.payment;

public class CashPayment implements Payment{

    @Override
    public boolean pay(double amount){
        System.out.println("Paid by Cash");
        return true;
    }
}
