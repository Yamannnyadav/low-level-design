package com.lowleveldesign.paymentsystem.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {

    private int txnID;
    private int amount;
    private int senderId;
    private int receiverId;
    private int debitInstrumentId;
    private int creditInstrumentId;
    private TransactionStatus status;

    public Transaction(){

    }
}
