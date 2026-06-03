package com.lowleveldesign.atm.States;

import com.lowleveldesign.atm.entities.ATM;
import com.lowleveldesign.atm.entities.Card;

public class CheckBalanceState extends ATMStates {

    @Override
    public void displayBalance(Card card, ATM atm){
        System.out.println("Your current balance: " + card.getBankBalance());
        exit(atm);
    }

    @Override
    public void returnCard(){
        System.out.println("Please collect your card");
    }

    @Override
    public void exit(ATM atm){
        returnCard();
        atm.setAtmStates(new IdleState());
        System.out.println("Exiting the ATM");
    }

}
