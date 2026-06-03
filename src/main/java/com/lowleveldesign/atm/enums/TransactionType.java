package com.lowleveldesign.atm.enums;

public enum TransactionType {

    CASH_DEPOSIT,
    CASH_WITHDRAWAL,
    CHECK_BALANCE;

    public static void showAllTransaction(){
        for(TransactionType transactionType : TransactionType.values()){
            System.out.println(transactionType.name());
        }
    }
}
