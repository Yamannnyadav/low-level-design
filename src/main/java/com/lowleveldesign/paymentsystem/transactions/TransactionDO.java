package com.lowleveldesign.paymentsystem.transactions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDO {

    int txnID;
    int senderId;
    int receiverId;
    int debitInstrumentId;
    int creditInstrumentId;
    int amount;
    TransactionStatus status;

    public TransactionDO() {
    }

    public TransactionDO(int txnID, int senderId, int receiverId, int debitInstrumentId, int creditInstrumentId, int amount, TransactionStatus status) {
        this.txnID = txnID;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.debitInstrumentId = debitInstrumentId;
        this.creditInstrumentId = creditInstrumentId;
        this.amount = amount;
        this.status = status;
    }

}
