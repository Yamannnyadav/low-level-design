package com.lowleveldesign.paymentsystem.instruments;

import java.util.List;

public class InstrumentController {

    private InstrumentServiceFactory instrumentServiceFactory;

    public InstrumentController(){
        this.instrumentServiceFactory = new InstrumentServiceFactory();
    }

    public InstrumentDO addInstrument(InstrumentDO instrument){
        InstrumentService instrumentService = instrumentServiceFactory.getInstrumentService(instrument.getInstrumentType());
        return instrumentService.addInstrument(instrument);
    }

    public List<InstrumentDO> getAllInstruments(int userId){
        InstrumentService bankInstrumentService = instrumentServiceFactory.getInstrumentService(InstrumentType.BANK);
        InstrumentService cardInstrumentService = instrumentServiceFactory.getInstrumentService(InstrumentType.CARD);
        List<InstrumentDO> instrumentDOList = bankInstrumentService.getInstrumentByUserId(userId);
        instrumentDOList.addAll(cardInstrumentService.getInstrumentByUserId(userId));
        return instrumentDOList;
    }

    public InstrumentDO getInstrumentByID(int userID, int instrumentID) {
        List<InstrumentDO> instrumentDOList = getAllInstruments(userID);

        for (InstrumentDO instrumentDO : instrumentDOList) {
            if (instrumentDO.getId() == instrumentID) {
                return instrumentDO;
            }
        }
        return null;
    }

}
