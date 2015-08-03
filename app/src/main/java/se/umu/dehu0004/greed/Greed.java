package se.umu.dehu0004.greed;

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
    private ArrayList<Die> dieList;


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
        dieList = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Die die = new Die(i, false);
            dieList.add(die);
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
     * @param dieList
     */
    public Greed(int totalScore, int turnScore, int rounds, int lastRollScore, boolean newRound, boolean lastRollHadFullHold, ArrayList<Die> dieList) {
        this.totalScore = totalScore;
        this.turnScore = turnScore;
        this.rounds = rounds;
        this.lastRollScore = lastRollScore;
        this.newRound = newRound;
        this.lastRollHadFullHold = lastRollHadFullHold;
        this.dieList = dieList;
    }

    /**
     * Sets the hold die value for a specific die.
     *
     * @param dieNbr which die to hold the value for, starting from index 0 to 5.
     * @param value   the value to hold.
     */
    public boolean setHoldDie(int dieNbr, boolean value) {
        if (newRound) {
            return false;
        }
        Die die = dieList.get(dieNbr);
        die.setHoldDieValue(value);
        return true;
    }

    /**
     * Rolls all 6 dice
     *
     * @return the die value for each die so it can be updated in the user interface.
     */
    public boolean rollAllDice() {
        if (!newRound && noDiceOnHold()) {
            return false;
        }
        if (allDiceOnHold()) {
            lastRollHadFullHold = true;
            resetDiceHold();
        }
        for (Die d : dieList) {
            d.rollDie();
        }
        return true;
    }

    /**
     * Checks if no dice are held.
     *
     * @return the boolean value of the check.
     */
    private boolean noDiceOnHold() {
        for (Die d : dieList) {
            if (d.getHoldDie()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all dice are held.
     *
     * @return the boolean value of the check.
     */
    private boolean allDiceOnHold() {
        for (Die d : dieList) {
            if (!d.getHoldDie()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets dieHold to false for all dice.
     */
    private void resetDiceHold() {
        for (Die d : dieList) {
            d.setHoldDieValue(false);
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
     * Evaluates score according to greed rules
     *
     * @return the turn score.
     */
    public int evaluateScore() {
        int score = GreedRules.calculateRoundScore(dieList);
        if (newRound && score < 300) {
            resetDiceHold();
            turnScore = 0;
            rounds++;
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
                rounds++;
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


        prefs.edit().putInt("die1", dieList.get(0).getDieValue()).apply();
        prefs.edit().putInt("die2", dieList.get(1).getDieValue()).apply();
        prefs.edit().putInt("die3", dieList.get(2).getDieValue()).apply();
        prefs.edit().putInt("die4", dieList.get(3).getDieValue()).apply();
        prefs.edit().putInt("die5", dieList.get(4).getDieValue()).apply();
        prefs.edit().putInt("die6", dieList.get(5).getDieValue()).apply();

        prefs.edit().putBoolean("die1hold", dieList.get(0).getHoldDie()).apply();
        prefs.edit().putBoolean("die2hold", dieList.get(1).getHoldDie()).apply();
        prefs.edit().putBoolean("die3hold", dieList.get(2).getHoldDie()).apply();
        prefs.edit().putBoolean("die4hold", dieList.get(3).getHoldDie()).apply();
        prefs.edit().putBoolean("die5hold", dieList.get(4).getHoldDie()).apply();
        prefs.edit().putBoolean("die6hold", dieList.get(5).getHoldDie()).apply();

    }


    /**
     * Gets the dielist.
     * @return the dielist.
     */
    public ArrayList<Die> getDieList() {
        return dieList;
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
}
