package com.lowleveldesign.paymentsystem.instruments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardInstrument extends Instrument {

    private String cardNumber;
    private String cvv;

    public CardInstrument(){

    }

    public CardInstrument(int instrumentId, int userID, InstrumentType type, String cardNumber, String cvv) {
        super(instrumentId, userID, type);
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

}
