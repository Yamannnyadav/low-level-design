package com.lowleveldesign.paymentsystem.instruments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Instrument {

    private int id;
    private int userId;
    private InstrumentType instrumentType;

    public Instrument(){

    }

    public Instrument(int id, int userId, InstrumentType instrumentType){
        this.id = id;
        this.userId = userId;
        this.instrumentType = instrumentType;
    }

}
