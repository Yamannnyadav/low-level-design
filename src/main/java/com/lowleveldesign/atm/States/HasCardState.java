package com.lowleveldesign.atm.States;

import com.lowleveldesign.atm.entities.ATM;
import com.lowleveldesign.atm.entities.Card;

public class HasCardState extends ATMStates {

    public HasCardState(){
        System.out.println("enter your card pin number");
    }

    @Override
    public void authenticatePin(Card card, ATM atm, int pin){
        System.out.println("Authenticating PIN");
        boolean isPinCorrect = card.isCorrectPinInserted(pin);
        if(!isPinCorrect){
            System.out.println("PIN entered is not correct, please take out the card and retry");
            exit(atm);
        }
        else{
            atm.setAtmStates(new SelectOperationState());
        }
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
