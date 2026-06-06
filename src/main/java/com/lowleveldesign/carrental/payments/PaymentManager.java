package com.lowleveldesign.carrental.payments;

import com.lowleveldesign.carrental.bill.Bill;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
@Setter
public class PaymentManager {

    private PaymentStrategy paymentStrategy;

    public PaymentManager(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    //to store all payment against paymentId
    private final ConcurrentMap<Integer, Payment> payments = new ConcurrentHashMap<>();

    public Payment makePayment(Bill bill, double paymentAmount) {

        Payment payment = paymentStrategy.processPayment(bill, paymentAmount);
        payments.put(payment.getPaymentId(), payment);
        return payment;
    }

    //there might be failed payments, partial advance payments
    public List<Payment> getPaymentsForBill(int billId) {
        return payments.values().stream()
                .filter(p -> p.getBillId() == billId)
                .toList();
    }

    public Optional<Payment> getPayment(int paymentId) {
        return Optional.ofNullable(payments.get(paymentId));
    }


}
