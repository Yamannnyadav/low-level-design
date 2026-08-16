package com.lowleveldesign.paymentsystem.instruments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstrumentDO {

    private int id;
    private int userId;
    private InstrumentType instrumentType;
    private String bankAccountNumber;
    private String ifscCode;
    private String cvvNumber;
    private String cardNumber;


}
