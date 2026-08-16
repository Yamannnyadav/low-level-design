package com.lowleveldesign.paymentsystem.instruments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankInstrument extends Instrument {

    private String bankAccountNumber;
    private String ifscCode;

    public BankInstrument(){

    }

    public BankInstrument(int instrumentId, int userId, InstrumentType instrumentType, String bankAccountNumber, String ifscCode){
        super(instrumentId, userId, instrumentType);
        this.bankAccountNumber = bankAccountNumber;
        this.ifscCode = ifscCode;
    }

}
