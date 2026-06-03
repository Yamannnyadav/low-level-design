package com.lowleveldesign.atm.States;

import com.lowleveldesign.atm.entities.ATM;
import com.lowleveldesign.atm.entities.Card;
import com.lowleveldesign.atm.enums.TransactionType;

public class SelectOperationState extends ATMStates {

    public SelectOperationState(){
        showOperations();
    }

    public void showOperations(){
        System.out.println("Please select the Operation");
        TransactionType.showAllTransaction();
    }

    @Override
    public void operationType(Card card, ATM atm, TransactionType transactionType){
        switch (transactionType) {
            case CASH_WITHDRAWAL :
                atm.setAtmStates(new CashWithdrawalState());
                break;
            case CHECK_BALANCE :
                atm.setAtmStates(new CheckBalanceState());
                break;
            case CASH_DEPOSIT :
                atm.setAtmStates(new CashDepositState());
                break;
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
