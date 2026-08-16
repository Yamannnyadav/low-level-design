package com.lowleveldesign.paymentsystem.instruments;

import com.lowleveldesign.atm.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InstrumentService {

    public static Map<Integer, List<Instrument>> userVsInstrument = new HashMap<>();

    public abstract InstrumentDO addInstrument(InstrumentDO instrumentDO);

    public abstract List<InstrumentDO> getInstrumentByUserId(int userId);

}
