package com.example.denhi.greed;

import java.util.Random;

/**
 * Created by Denhi on 2015-06-09.
 */
public class Dice {
    private int diceValue;
    private boolean holdDiceValue;

    public Dice(){
        diceValue = 0;
        holdDiceValue = false;
    }

    /**
     * Sets if the dice should hold the current dice value for next dice roll.
     */
    public void setHoldDiceValue(boolean value){
        holdDiceValue = value;
    }

    /**
     * Gets the current dice value.
     */
    public int getDiceValue(){
        return diceValue;
    }

    /**
     * Generates a new dice value between 1 and 6
     * @return the new dice value
     */
    public int rollDice(){
        if(holdDiceValue){
            return diceValue;
        }else {
            Random rand = new Random();
            diceValue = rand.nextInt(6) + 1;
            return diceValue;
        }
    }

}
