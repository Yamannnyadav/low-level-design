package com.lowleveldesign.carrental.bill;

import com.lowleveldesign.carrental.reservations.Reservation;

public interface BillingStrategy {

    Bill generateBill(Reservation reservation);

}
