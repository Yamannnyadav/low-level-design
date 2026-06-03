package com.lowleveldesign.atm.States;

import com.lowleveldesign.atm.entities.ATM;
import com.lowleveldesign.atm.entities.Card;
import com.lowleveldesign.atm.withdrawalprocessor.CashWithdrawalProcessor;
import com.lowleveldesign.atm.withdrawalprocessor.FiveHundredNotesProcessor;
import com.lowleveldesign.atm.withdrawalprocessor.OneHundredNotesProcessor;
import com.lowleveldesign.atm.withdrawalprocessor.TwoThousandNotesProcessor;

public class CashWithdrawalState extends ATMStates {

    public CashWithdrawalState() {
        System.out.println("Please enter the Withdrawal Amount");
    }

    @Override
    public void cashWithdrawal(Card card, ATM atm, int cashWithdrawalAmount){
        if(atm.getAtmBalance() < cashWithdrawalAmount){
            System.out.println("Insufficient fund in the ATM Machine");
            exit(atm);
        }
        else if(card.getBankBalance() < cashWithdrawalAmount){
            System.out.println("Insufficient fund in the your Bank Account");
            exit(atm);
        }
        else{
            atm.deductAtmBalance(cashWithdrawalAmount);
            card.deductBankBalance(cashWithdrawalAmount);

            //using chain of responsibility for this logic, how many 2k Rs notes, how many 500 Rs notes etc, has to be withdrawal
            CashWithdrawalProcessor withdrawProcessor =
                    new TwoThousandNotesProcessor(new FiveHundredNotesProcessor(new OneHundredNotesProcessor(null)));

            withdrawProcessor.withdraw(atm, cashWithdrawalAmount);
            exit(atm);
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
