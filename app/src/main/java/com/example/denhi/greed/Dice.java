package com.example.denhi.greed;

import java.util.Random;

/**
 * Created by Denhi on 2015-06-09.
 */
public class Dice {
    int diceValue;


    public Dice(){
        diceValue = 0;
    }

    /**
     * Generates a new dice value between 1 and 6
     * @return the new dice value
     */
    public int rollDice(){
        Random rand = new Random();
        diceValue = rand.nextInt(6) + 1;
        return diceValue;
    }
}
