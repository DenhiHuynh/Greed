package com.example.denhi.greed;

import java.util.ArrayList;

/**
 * Created by Denhi on 2015-06-08.
 */
public class Greed {
    private int totalScore, turnScore, rounds;
    private ArrayList<Dice> diceList;

    public Greed() {
        totalScore = 0;
        turnScore = 0;
        rounds = 0;
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
    public void setHoldDice(int diceNbr, boolean value) {
        Dice dice = diceList.get(diceNbr);
        dice.setHoldDiceValue(value);
    }

    /**
     * Rolls all 6 dices
     * @return the dice value for each dice so it can be updated in the user interface.
     */
    public ArrayList<Integer> rollAllDices(){
        ArrayList<Integer> diceValueList = new ArrayList<>();
        for(Dice d:diceList){
            diceValueList.add(d.rollDice());
        }
        return diceValueList;
    }

    /**
     * Saves turn score into total score
     * @return if the player has won
     */
    public boolean addToTotalScore(){
        totalScore += turnScore;
        turnScore = 0;
        rounds++;
        return totalScore > 10000;
    }

    /**
     * Gets the total score
     * @return the total score
     */
    public int getTotalScore(){
        return totalScore;
    }

    /**
     * Gets the total score
     * @return the total score
     */
    public int getRounds(){
        return rounds;
    }

    /**
     * Evaluates score according to greed rules
     *
     * @return
     */
    public int evaluateScore() {
        int score = GreedRules.calculateRoundScore(diceList);
        //Check how many point a dice throw should give
        return score;
    }



}
