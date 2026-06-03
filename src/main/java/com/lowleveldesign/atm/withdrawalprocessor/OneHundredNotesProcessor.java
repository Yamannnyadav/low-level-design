package com.lowleveldesign.atm.withdrawalprocessor;

import com.lowleveldesign.atm.entities.ATM;

public class OneHundredNotesProcessor extends CashWithdrawalProcessor{


    public OneHundredNotesProcessor(CashWithdrawalProcessor nextCashWithdrawProcessor) {
        super(nextCashWithdrawProcessor);
    }

    @Override
    public void withdraw(ATM atm, int remainingAmount){

        int requiredNotes = remainingAmount/100;
        int balanceAmount = remainingAmount%100;

        if(requiredNotes <= atm.getNumOfHundredNotes()){
            atm.deductHundredNotes(requiredNotes);
        }
        else{
            atm.deductHundredNotes(requiredNotes);
            balanceAmount = balanceAmount + (requiredNotes - atm.getNumOfHundredNotes()) * 100;
        }

        if(balanceAmount != 0){
            System.out.println("Something went wrong");
        }
    }
}
