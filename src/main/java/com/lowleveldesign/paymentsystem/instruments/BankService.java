package com.lowleveldesign.paymentsystem.instruments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankService extends InstrumentService {

    public BankService(){
        super();
    }

    @Override
    public InstrumentDO addInstrument(InstrumentDO instrumentDO) {
        BankInstrument bankInstrument = new BankInstrument();
        bankInstrument.setId(new Random().nextInt(100 - 10) + 10);
        bankInstrument.setBankAccountNumber(instrumentDO.getBankAccountNumber());
        bankInstrument.setIfscCode(instrumentDO.getIfscCode());
        bankInstrument.setInstrumentType(InstrumentType.BANK);
        bankInstrument.setUserId(instrumentDO.getUserId());
        List<Instrument> userInstrumentsList = userVsInstrument.get(bankInstrument.getUserId());
        if (userInstrumentsList == null) {
            userInstrumentsList = new ArrayList<>();
            userVsInstrument.put(bankInstrument.getUserId(), userInstrumentsList);
        }
        userInstrumentsList.add(bankInstrument);
        return mapBankInstrumentToInstrumentDO(bankInstrument);
    }

    @Override
    public List<InstrumentDO> getInstrumentByUserId(int userID) {
        List<Instrument> userInstruments = userVsInstrument.get(userID);
        List<InstrumentDO> userInstrumentsFetched = new ArrayList<>();
        for (Instrument instrument : userInstruments) {
            if (instrument.getInstrumentType() == InstrumentType.BANK)
                userInstrumentsFetched.add(mapBankInstrumentToInstrumentDO((BankInstrument) instrument));
        }
        return userInstrumentsFetched;
    }

    public InstrumentDO mapBankInstrumentToInstrumentDO(BankInstrument bankInstrument) {
        InstrumentDO instrumentDOObj = new InstrumentDO();
        instrumentDOObj.setInstrumentType(bankInstrument.getInstrumentType());
        instrumentDOObj.setId(bankInstrument.getId());
        instrumentDOObj.setBankAccountNumber(bankInstrument.getBankAccountNumber());
        instrumentDOObj.setIfscCode(bankInstrument.getIfscCode());
        instrumentDOObj.setUserId(bankInstrument.getUserId());
        return instrumentDOObj;
    }
}
