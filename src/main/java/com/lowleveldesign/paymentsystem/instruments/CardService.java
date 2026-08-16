package com.lowleveldesign.paymentsystem.instruments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardService extends InstrumentService {

    public CardService() {
        super();
    }

    @Override
    public InstrumentDO addInstrument(InstrumentDO instrumentDO) {
        // card specific logic here

        CardInstrument cardInstrument = new CardInstrument();
        cardInstrument.setId(new Random().nextInt(100 - 10) + 10);
        cardInstrument.setCardNumber(instrumentDO.getCardNumber());
        cardInstrument.setCvv(instrumentDO.getCvvNumber());
        cardInstrument.setInstrumentType(InstrumentType.CARD);
        cardInstrument.setUserId(instrumentDO.getUserId());
        List<Instrument> userInstrumentsList = userVsInstrument.get(cardInstrument.getUserId());
        if (userInstrumentsList == null) {
            userInstrumentsList = new ArrayList<>();
            userVsInstrument.put(cardInstrument.getUserId(), userInstrumentsList);
        }
        userInstrumentsList.add(cardInstrument);
        return mapBankInstrumentToInstrumentDO(cardInstrument);
    }

    private InstrumentDO mapBankInstrumentToInstrumentDO(CardInstrument cardInstrument) {

        InstrumentDO instrumentDOObj = new InstrumentDO();
        instrumentDOObj.setInstrumentType(cardInstrument.getInstrumentType());
        instrumentDOObj.setId(cardInstrument.getId());
        instrumentDOObj.setBankAccountNumber(cardInstrument.getCardNumber());
        instrumentDOObj.setIfscCode(cardInstrument.getCvv());
        instrumentDOObj.setUserId(cardInstrument.getUserId());
        return instrumentDOObj;

    }

    @Override
    public List<InstrumentDO> getInstrumentByUserId(int userID) {
        List<Instrument> userInstruments = userVsInstrument.get(userID);
        List<InstrumentDO> userInstrumentsFetched = new ArrayList<>();
        for (Instrument instrument : userInstruments) {
            if (instrument.getInstrumentType() == InstrumentType.CARD)
                userInstrumentsFetched.add(mapBankInstrumentToInstrumentDO((CardInstrument) instrument));
        }
        return userInstrumentsFetched;
    }
}
