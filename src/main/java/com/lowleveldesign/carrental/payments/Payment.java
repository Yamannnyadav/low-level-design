package com.lowleveldesign.carrental.payments;

import com.lowleveldesign.carrental.enums.PaymentMode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Payment {

    private final int paymentId;
    private final int billId;
    private final double amountPaid;
    private final PaymentMode paymentMode;
    private final Date paymentDate;

    public Payment(int paymentId,
                   int billId,
                   double amountPaid,
                   PaymentMode paymentMode,
                   Date paymentDate) {

        this.paymentId = paymentId;
        this.billId = billId;
        this.amountPaid = amountPaid;
        this.paymentMode = paymentMode;
        this.paymentDate = paymentDate;
    }

}
