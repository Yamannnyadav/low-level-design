package com.lowleveldesign.atm.entities;

import java.time.LocalDate;

public class Card {

    private static int cardPin = 1234;
    String cardNumber;
    LocalDate expiryDate;
    int cvv;
    String cardProvider;
    UserBankAccount userBankAccount;

    public Card(String cardNumber, LocalDate expiryDate, int cvv, String cardProvider, UserBankAccount userBankAccount) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.cardProvider = cardProvider;
        this.userBankAccount = userBankAccount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getCardProvider() {
        return cardProvider;
    }

    public int getCvv() {
        return cvv;
    }

    public boolean isCorrectPinInserted(int pin){
        return pin == cardPin;
    }

    public int getBankBalance(){
        return userBankAccount.accountBalance;
    }

    public void deductBankBalance(int amount){
        userBankAccount.withdrawalBalance(amount);
    }

    public void addBankBalance(int amount){
        userBankAccount.depositBalance(amount);
    }

    public void setUserBankAccount(UserBankAccount userBankAccount) {
        this.userBankAccount = userBankAccount;
    }
}
