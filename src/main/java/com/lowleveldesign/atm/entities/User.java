package com.lowleveldesign.atm.entities;


public class User {

    Card card;
    UserBankAccount userBankAccount;

    public Card getCard() {
        return card;
    }

    public UserBankAccount getUserBankAccount() {
        return userBankAccount;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setUserBankAccount(UserBankAccount userBankAccount) {
        this.userBankAccount = userBankAccount;
    }
}
