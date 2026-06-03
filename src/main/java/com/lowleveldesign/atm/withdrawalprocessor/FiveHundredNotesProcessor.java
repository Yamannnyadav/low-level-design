package com.lowleveldesign.atm.withdrawalprocessor;

import com.lowleveldesign.atm.entities.ATM;

public class FiveHundredNotesProcessor extends CashWithdrawalProcessor {

    public FiveHundredNotesProcessor(CashWithdrawalProcessor nextCashWithdrawProcessor) {
        super(nextCashWithdrawProcessor);
    }

    @Override
    public void withdraw(ATM atm, int remainingAmount) {

        int requiredNotes = remainingAmount / 500;
        int balanceAmount = remainingAmount % 500;

        if (requiredNotes <= atm.getNumOfFiveHundredNotes()) {
            atm.deductFiveHundredNotes(requiredNotes);
        } else {
            atm.deductFiveHundredNotes(atm.getNumOfFiveHundredNotes());
            balanceAmount = balanceAmount + (requiredNotes - atm.getNumOfFiveHundredNotes()) * 500;
        }

        if (balanceAmount != 0) {
            super.withdraw(atm, balanceAmount);
        }
    }
}
