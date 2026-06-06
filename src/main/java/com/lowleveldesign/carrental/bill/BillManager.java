package com.lowleveldesign.carrental.bill;

import com.lowleveldesign.carrental.reservations.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
@Setter
public class BillManager {

    private BillingStrategy billingStrategy;

    //to store all bills
    private final ConcurrentMap<Integer, Bill> bills = new ConcurrentHashMap<>();

    public BillManager(BillingStrategy billingStrategy){
        this.billingStrategy = billingStrategy;
    }

    public Bill generateBill(Reservation reservation){

        Bill bill = billingStrategy.generateBill(reservation);
        bills.put(bill.getId(), bill);
        return bill;
    }

    public Optional<Bill> getBill(int billId){
        return Optional.ofNullable(bills.get(billId));
    }
}
