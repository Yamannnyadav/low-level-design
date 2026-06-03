package com.lowleveldesign.atm.entities;

import com.lowleveldesign.atm.States.ATMStates;
import com.lowleveldesign.atm.States.IdleState;

public class ATM {

    ATMStates atmStates;
    int numOfHundredNotes;
    int numOfFiveHundredNotes;
    int numOfTwoThousandNotes;
    int atmBalance;

    public static ATM atmObject = new ATM();

    public ATM() {
    }

    public static ATM getAtmObject() {
        atmObject.setAtmStates(new IdleState() {
        });
        return atmObject;
    }

    public ATMStates getAtmStates() {
        return atmStates;
    }

    public int getAtmBalance() {
        return atmBalance;
    }

    public int getNumOfTwoThousandNotes() {
        return numOfTwoThousandNotes;
    }

    public int getNumOfFiveHundredNotes() {
        return numOfFiveHundredNotes;
    }

    public int getNumOfHundredNotes() {
        return numOfHundredNotes;
    }

    public void setAtmStates(ATMStates atmStates) {
        this.atmStates = atmStates;
    }

    public void setNumOfHundredNotes(int numOfHundredNotes) {
        this.numOfHundredNotes = numOfHundredNotes;
    }

    public void setNumOfFiveHundredNotes(int numOfFiveHundredNotes) {
        this.numOfFiveHundredNotes = numOfFiveHundredNotes;
    }

    public void setNumOfTwoThousandNotes(int numOfTwoThousandNotes) {
        this.numOfTwoThousandNotes = numOfTwoThousandNotes;
    }

    public void setAtmBalance(int atmBalance, int numOfHundredNotes, int numOfFiveHundredNotes, int numOfTwoThousandNotes) {
        this.atmBalance = atmBalance;
        this.numOfHundredNotes = numOfHundredNotes;
        this.numOfFiveHundredNotes = numOfFiveHundredNotes;
        this.numOfTwoThousandNotes = numOfTwoThousandNotes;
    }

    public void deductAtmBalance(int amount){
        atmBalance = atmBalance - amount;
    }

    public void deductHundredNotes(int number){
        numOfHundredNotes = numOfHundredNotes - number;
    }

    public void deductFiveHundredNotes(int number){
        numOfFiveHundredNotes = numOfFiveHundredNotes - number;
    }

    public void deductTwoThousandNotes(int number){
        numOfTwoThousandNotes = numOfTwoThousandNotes - number;
    }

    public void addAtmBalance(int amount) {
        atmBalance = atmBalance + amount;
    }

    public void addHundredNotes(int number) {
        numOfHundredNotes += number;
    }

    public void addFiveHundredNotes(int number) {
        numOfFiveHundredNotes += number;
    }

    public void addTwoThousandNotes(int number) {
        numOfTwoThousandNotes += number;
    }

    public void printCurrentATMStatus() {
        System.out.println("Balance: " + atmBalance);
        System.out.println("2kNotes: " + numOfTwoThousandNotes);
        System.out.println("500Notes: " + numOfFiveHundredNotes);
        System.out.println("100Notes: " + numOfHundredNotes);
    }

}
