package com.example.denhi.greed;

import java.util.ArrayList;

/**
 * Created by Denhi on 2015-06-08.
 */
public class Greed {
    private int totalScore, turnScore, rounds, lastRollScore;
    private boolean newRound , lastRollHadFullHold;
    private ArrayList<Dice> diceList;

    public Greed() {
        totalScore = 0;
        lastRollScore = 0;
        turnScore = 0;
        rounds = 0;
        newRound = true;
        lastRollHadFullHold = false;
        diceList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Dice dice = new Dice();
            diceList.add(dice);
        }
    }

    /**
     * Sets the hold dice value for a specific dice.
     *
     * @param diceNbr which dice to hold the value for, starting from index 0 to 5.
     * @param value   the value to hold.
     */
    public boolean setHoldDice(int diceNbr, boolean value) {
        if(newRound){
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
        if(!newRound && noDiceOnHold()){
            return false;
        }
        if(allDiceOnHold()){
            lastRollHadFullHold = true;
            resetDiceHold();
        }
        for (Dice d : diceList) {
            d.rollDice();
        }
        return true;
    }

    private boolean noDiceOnHold() {
        for(Dice d: diceList){
            if(d.getHoldDice()){
                return false;
            }
        }
        return true;
    }

    private boolean allDiceOnHold() {
        for(Dice d: diceList){
            if(!d.getHoldDice()){
                return false;
            }
        }
        return true;
    }

    private void resetDiceHold() {
        for(Dice d: diceList){
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
        } else if(newRound && score>= 300) {
            resetDiceHold();
            newRound = false;
            lastRollScore = score;
            turnScore += score;
        } else {
            int newPoints;
            if(lastRollHadFullHold){
                lastRollHadFullHold = false;
                newPoints = score;
            }else {
                newPoints = Math.abs(score - lastRollScore);
            }
            if (newPoints == 0) {
                resetDiceHold();
                newRound = true;
                lastRollScore = 0;
                turnScore = 0;
            }else{
                lastRollScore = score;
                turnScore += newPoints;
            }
        }
        return turnScore;
    }

    public ArrayList<Dice> getDiceList(){
        return diceList;
    }

    public int getTurnScore() {
        return turnScore;
    }

    public boolean isNewRound() {
        return newRound;
    }
}
