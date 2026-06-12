package com.lowleveldesign.snakeandladder.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class Dice {

    private int diceCount;
    private final int min = 1;
    private final int max = 6;

    public Dice(int diceCount){
        this.diceCount = diceCount;
    }

    public int rollDice() {

        int totalSum = 0;

        for(int i = 0; i < diceCount; i++){
            totalSum += ThreadLocalRandom.current().nextInt(min, max + 1);
        }

        return totalSum;
    }
}
