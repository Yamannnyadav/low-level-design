package com.lowleveldesign.atm.withdrawalprocessor;

import com.lowleveldesign.atm.entities.ATM;

public abstract class CashWithdrawalProcessor {

    CashWithdrawalProcessor nextCashWithdrawalProcessor;

    public CashWithdrawalProcessor(CashWithdrawalProcessor cashWithdrawalProcessor){
        this.nextCashWithdrawalProcessor = cashWithdrawalProcessor;
    }

    public void withdraw(ATM atm, int remainingAmount){
        if(nextCashWithdrawalProcessor != null){
            nextCashWithdrawalProcessor.withdraw(atm, remainingAmount);
        }
    }
}
