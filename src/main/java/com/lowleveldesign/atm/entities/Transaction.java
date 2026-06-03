package com.lowleveldesign.atm.entities;

public class Transaction {

    int transactionAmount;

    public Transaction(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }
}
