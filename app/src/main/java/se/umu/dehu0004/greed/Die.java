package se.umu.dehu0004.greed;

import java.util.Random;

/**
 * Created by Denhi on 2015-06-09.
 */
public class Die {
    private int dieValue;
    private boolean holdDieValue;

    /**
     * Creates a new die with specified values.
     * @param dieValue is the value of the die.
     * @param dieHold if the die should hold the value for new rolls (i.e if the die should have the same value after new die rolls).
     */
    public Die(int dieValue, boolean dieHold) {
        this.dieValue = dieValue;
        holdDieValue = dieHold;
    }

    /**
     * Sets if the die should hold the current die value for next die roll.
     */
    public void setHoldDieValue(boolean value) {
        holdDieValue = value;
    }

    /**
     * Gets if the die value is held during the current round.
     *
     * @return the die hold boolean value.
     */
    public boolean getHoldDie() {
        return holdDieValue;
    }

    /**
     * Gets the current die value.
     */
    public int getDieValue() {
        return dieValue;
    }

    /**
     * Generates a new die value between 1 and 6
     *
     * @return the new die value
     */
    public int rollDie() {
        if (holdDieValue) {
            return dieValue;
        } else {
            Random rand = new Random();
            dieValue = rand.nextInt(6) + 1;
            return dieValue;
        }
    }

}
