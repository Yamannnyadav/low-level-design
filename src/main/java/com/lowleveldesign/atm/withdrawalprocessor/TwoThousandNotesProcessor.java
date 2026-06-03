package com.lowleveldesign.atm.withdrawalprocessor;

import com.lowleveldesign.atm.entities.ATM;

public class TwoThousandNotesProcessor extends CashWithdrawalProcessor {

    public TwoThousandNotesProcessor(CashWithdrawalProcessor nextCashWithdrawProcessor) {
        super(nextCashWithdrawProcessor);
    }

    public void withdraw(ATM atm, int remainingAmount) {

        int requiredNotes = remainingAmount / 2000;
        int balanceAmount = remainingAmount % 2000;

        if (requiredNotes <= atm.getNumOfTwoThousandNotes()) {
            atm.deductTwoThousandNotes(requiredNotes);
        } else  {
            atm.deductTwoThousandNotes(atm.getNumOfTwoThousandNotes());
            balanceAmount = balanceAmount + (requiredNotes - atm.getNumOfTwoThousandNotes()) * 2000;
        }

        if (balanceAmount != 0) {
            super.withdraw(atm, balanceAmount);
        }
    }

}
