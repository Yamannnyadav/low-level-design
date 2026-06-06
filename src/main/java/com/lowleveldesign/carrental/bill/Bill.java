package com.lowleveldesign.carrental.bill;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bill {

    private int id;
    private int reservationId;
    private double totalBillAmount;
    private boolean billPaid;

    public Bill(int id, int reservationId, double totalBillAmount) {
        this.id = id;
        this.reservationId = reservationId;
        this.totalBillAmount = totalBillAmount;
        this.billPaid = false;
    }
}
