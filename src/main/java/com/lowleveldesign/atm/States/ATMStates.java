package com.lowleveldesign.atm.States;

import com.lowleveldesign.atm.entities.ATM;
import com.lowleveldesign.atm.entities.Card;
import com.lowleveldesign.atm.entities.Transaction;
import com.lowleveldesign.atm.enums.TransactionType;

public abstract class ATMStates {

    public void insertCard(Card card, ATM atm){
        System.out.println("OOPS!! Something went wrong");
    }

    public void authenticatePin(Card card, ATM atm, int pin){
        System.out.println("OOPS!! Something went wrong");
    }

    public void operationType(Card card, ATM atm, TransactionType transactionType){
        System.out.println("OOPS!! Something went wrong");
    }

    public void cashWithdrawal(Card card, ATM atm, int cashWithdrawalAmount){
        System.out.println("OOPS!! Something went wrong");
    }

    public void cashDeposit(Card card, ATM atm, int depositAmount, int hundredNotes, int fiveHundredNotes, int twoThousandNotes){
        System.out.println("OOPS!! Something went wrong");
    }

    public void displayBalance(Card card, ATM atm){
        System.out.println("OOPS!! Something went wrong");
    }

    public void returnCard(){
        System.out.println("OOPS!! Something went wrong");
    }

    public void exit(ATM atm){
        System.out.println("OOPS!! Something went wrong");
    }

}
