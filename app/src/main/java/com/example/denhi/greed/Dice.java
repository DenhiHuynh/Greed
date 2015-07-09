package com.example.denhi.greed;

import java.util.Random;

/**
 * Created by Denhi on 2015-06-09.
 */
public class Dice {
    private int diceValue;
    private boolean holdDiceValue;

    public Dice(int diceValue, boolean diceHold) {
        this.diceValue = diceValue;
        holdDiceValue = diceHold;
    }

    /**
     * Sets if the dice should hold the current dice value for next dice roll.
     */
    public void setHoldDiceValue(boolean value) {
        holdDiceValue = value;
    }

    /**
     * Gets if the dice value is held during the current round.
     *
     * @return the dice hold boolean value.
     */
    public boolean getHoldDice() {
        return holdDiceValue;
    }

    /**
     * Gets the current dice value.
     */
    public int getDiceValue() {
        return diceValue;
    }

    /**
     * Generates a new dice value between 1 and 6
     *
     * @return the new dice value
     */
    public int rollDice() {
        if (holdDiceValue) {
            return diceValue;
        } else {
            Random rand = new Random();
            diceValue = rand.nextInt(6) + 1;
            return diceValue;
        }
    }

}
