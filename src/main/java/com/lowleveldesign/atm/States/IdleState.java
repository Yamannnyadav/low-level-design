package com.lowleveldesign.atm.States;

import com.lowleveldesign.atm.entities.ATM;
import com.lowleveldesign.atm.entities.Card;

public class IdleState extends ATMStates {

    @Override
    public void insertCard(Card card, ATM atm){
        System.out.println("Wohoo! Card is Inserted.");
        atm.setAtmStates(new HasCardState());
    }
}
