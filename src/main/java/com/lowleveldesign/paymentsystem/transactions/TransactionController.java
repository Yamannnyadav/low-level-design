package com.lowleveldesign.paymentsystem.transactions;

import java.util.List;

public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(){
        this.transactionService = new TransactionService();
    }

    public TransactionDO makePayment(TransactionDO txnDO) {
        return transactionService.makePayment(txnDO);
    }

    public List<Transaction> getTransactionHistory(int userID) {
        return transactionService.getTransactionHistory(userID);
    }
}
