package com.lowleveldesign.carrental.payments;

import com.lowleveldesign.carrental.bill.Bill;

public interface PaymentStrategy {

    Payment processPayment(Bill bill, double paymentAmount);

}
