package com.lowleveldesign.atm.entities;

import java.util.List;

public class UserBankAccount {

    int accountBalance;

    public UserBankAccount(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void withdrawalBalance(int amount){
        accountBalance = accountBalance - amount;
    }

    public void depositBalance(int amount){
        accountBalance = accountBalance + amount;
    }

}
