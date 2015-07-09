package com.example.denhi.greed;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Denhi on 2015-06-08.
 */
public class Greed {
    private Activity activity;
    private int totalScore, turnScore, rounds, lastRollScore;
    private boolean newRound, lastRollHadFullHold;
    private ArrayList<Dice> diceList;


    /**
     * Constructor for new game
     */
    public Greed() {
        totalScore = 0;
        lastRollScore = 0;
        turnScore = 0;
        rounds = 0;
        newRound = true;
        lastRollHadFullHold = false;
        diceList = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Dice dice = new Dice(i, false);
            diceList.add(dice);
        }
    }

    /**
     * Constructor for resumed game.
     *
     * @param totalScore
     * @param turnScore
     * @param rounds
     * @param lastRollScore
     * @param newRound
     * @param lastRollHadFullHold
     * @param diceList
     */
    public Greed(int totalScore, int turnScore, int rounds, int lastRollScore, boolean newRound, boolean lastRollHadFullHold, ArrayList<Dice> diceList) {
        this.totalScore = totalScore;
        this.turnScore = turnScore;
        this.rounds = rounds;
        this.lastRollScore = lastRollScore;
        this.newRound = newRound;
        this.lastRollHadFullHold = lastRollHadFullHold;
        this.diceList = diceList;
    }

    /**
     * Sets the hold dice value for a specific dice.
     *
     * @param diceNbr which dice to hold the value for, starting from index 0 to 5.
     * @param value   the value to hold.
     */
    public boolean setHoldDice(int diceNbr, boolean value) {
        if (newRound) {
            return false;
        }
        Dice dice = diceList.get(diceNbr);
        dice.setHoldDiceValue(value);
        return true;
    }

    /**
     * Rolls all 6 dices
     *
     * @return the dice value for each dice so it can be updated in the user interface.
     */
    public boolean rollAllDices() {
        if (!newRound && noDiceOnHold()) {
            return false;
        }
        if (allDiceOnHold()) {
            lastRollHadFullHold = true;
            resetDiceHold();
        }
        for (Dice d : diceList) {
            d.rollDice();
        }
        return true;
    }

    /**
     * Checks if no dices are held.
     *
     * @return the boolean value of the check.
     */
    private boolean noDiceOnHold() {
        for (Dice d : diceList) {
            if (d.getHoldDice()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all dices are held.
     *
     * @return the boolean value of the check.
     */
    private boolean allDiceOnHold() {
        for (Dice d : diceList) {
            if (!d.getHoldDice()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets diceHold to false for all dices.
     */
    private void resetDiceHold() {
        for (Dice d : diceList) {
            d.setHoldDiceValue(false);
        }
    }

    /**
     * Saves turn score into total score
     *
     * @return if the player has won
     */
    public boolean addToTotalScore() {
        totalScore += turnScore;
        turnScore = 0;
        rounds++;
        newRound = true;
        resetDiceHold();
        return totalScore >= 10000;
    }

    /**
     * Gets the total score
     *
     * @return the total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Gets the total score
     *
     * @return the total score
     */
    public int getRounds() {
        return rounds;
    }

    /**
     * Evaluates score according to greed rules
     *
     * @return the turn score.
     */
    public int evaluateScore() {
        int score = GreedRules.calculateRoundScore(diceList);
        if (newRound && score < 300) {
            resetDiceHold();
            turnScore = 0;
        } else if (newRound && score >= 300) {
            resetDiceHold();
            newRound = false;
            lastRollScore = score;
            turnScore += score;
        } else {
            int newPoints;
            if (lastRollHadFullHold) {
                lastRollHadFullHold = false;
                newPoints = score;
            } else {
                newPoints = Math.abs(score - lastRollScore);
            }
            if (newPoints == 0) {
                resetDiceHold();
                newRound = true;
                lastRollScore = 0;
                turnScore = 0;
            } else {
                lastRollScore = score;
                turnScore += newPoints;
            }
        }
        return turnScore;
    }


    /**
     * Saves to game state to shared preferences.
     * @param context the activity calling this method.
     */
    public void saveGameInstance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("greed", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("resume", true).apply();

        prefs.edit().putBoolean("newRound", newRound).apply();
        prefs.edit().putBoolean("lastRollHadFullHold", lastRollHadFullHold).apply();
        prefs.edit().putInt("totalScore", totalScore).apply();
        prefs.edit().putInt("turnScore", turnScore).apply();
        prefs.edit().putInt("rounds", rounds).apply();
        prefs.edit().putInt("lastRollScore", lastRollScore).apply();


        prefs.edit().putInt("dice1", diceList.get(0).getDiceValue()).apply();
        prefs.edit().putInt("dice2", diceList.get(1).getDiceValue()).apply();
        prefs.edit().putInt("dice3", diceList.get(2).getDiceValue()).apply();
        prefs.edit().putInt("dice4", diceList.get(3).getDiceValue()).apply();
        prefs.edit().putInt("dice5", diceList.get(4).getDiceValue()).apply();
        prefs.edit().putInt("dice6", diceList.get(5).getDiceValue()).apply();

        prefs.edit().putBoolean("dice1hold", diceList.get(0).getHoldDice()).apply();
        prefs.edit().putBoolean("dice2hold", diceList.get(1).getHoldDice()).apply();
        prefs.edit().putBoolean("dice3hold", diceList.get(2).getHoldDice()).apply();
        prefs.edit().putBoolean("dice4hold", diceList.get(3).getHoldDice()).apply();
        prefs.edit().putBoolean("dice5hold", diceList.get(4).getHoldDice()).apply();
        prefs.edit().putBoolean("dice6hold", diceList.get(5).getHoldDice()).apply();

    }

    /**
     * Gets the dicelist.
     * @return the dicelist.
     */
    public ArrayList<Dice> getDiceList() {
        return diceList;
    }

    /**
     * Gets the turn score.
     * @return the turn score.
     */
    public int getTurnScore() {
        return turnScore;
    }

    /**
     * Gets the boolean value if it is a new round.
     * @return the boolean value of newRound.
     */
    public boolean isNewRound() {
        return newRound;
    }
}
