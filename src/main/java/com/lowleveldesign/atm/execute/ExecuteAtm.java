package com.lowleveldesign.atm.execute;

import com.lowleveldesign.atm.entities.ATM;
import com.lowleveldesign.atm.entities.Card;
import com.lowleveldesign.atm.entities.User;
import com.lowleveldesign.atm.entities.UserBankAccount;
import com.lowleveldesign.atm.enums.TransactionType;

import java.time.LocalDate;

public class ExecuteAtm {

    ATM atm;
    User user;

    public static void main(String[] args){

        ExecuteAtm executeAtm = new ExecuteAtm();
        executeAtm.initialise();

        executeAtm.atm.printCurrentATMStatus();

        executeAtm.atm.getAtmStates().insertCard(executeAtm.user.getCard(), executeAtm.atm);
        executeAtm.atm.getAtmStates().authenticatePin(executeAtm.user.getCard(), executeAtm.atm, 1234);
        executeAtm.atm.getAtmStates().operationType(executeAtm.user.getCard(), executeAtm.atm, TransactionType.CASH_WITHDRAWAL);
        executeAtm.atm.getAtmStates().cashWithdrawal(executeAtm.user.getCard(), executeAtm.atm, 2700);

        executeAtm.atm.printCurrentATMStatus();



    }

    public void initialise(){

        //create ATM
        atm = ATM.getAtmObject();
        atm.setAtmBalance(3500, 5, 2, 1);

        //create user
        this.user = createUser();
    }

    public User createUser(){
        User user = new User();
        user.setCard(createCard());
        return user;
    }

    public Card createCard(){
        return new Card("1234-2343-2333-9909", LocalDate.now(), 123, "AMEX", new UserBankAccount(3500));

    }


}
