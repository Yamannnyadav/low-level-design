package com.lowleveldesign.atm.States;

import com.lowleveldesign.atm.entities.ATM;
import com.lowleveldesign.atm.entities.Card;

public class CashDepositState extends ATMStates {

    public CashDepositState() {
        System.out.println("Please insert cash to deposit");
    }

    @Override
    public void cashDeposit(Card card, ATM atm, int depositAmount, int hundredNotes, int fiveHundredNotes, int twoThousandNotes) {

        int totalAmount = hundredNotes * 100 +
                        fiveHundredNotes * 500 +
                        twoThousandNotes * 2000;

        if(totalAmount != depositAmount) {
            System.out.println("Invalid cash amount inserted");
            exit(atm);
            return;
        }

        // Update ATM balance
        atm.addAtmBalance(depositAmount);

        // Update note counts
        atm.addHundredNotes(hundredNotes);
        atm.addFiveHundredNotes(fiveHundredNotes);
        atm.addTwoThousandNotes(twoThousandNotes);

        // Update bank balance
        card.addBankBalance(depositAmount);

        System.out.println("Amount deposited successfully: " + depositAmount);

        exit(atm);
    }

    @Override
    public void returnCard() {
        System.out.println("Please collect your card");
    }

    @Override
    public void exit(ATM atm) {
        returnCard();
        atm.setAtmStates(new IdleState());

        System.out.println("Exiting ATM");
    }
}