package se.umu.dehu0004.greed;

import java.util.ArrayList;

/**
 * Created by Denhi on 2015-06-14.
 */
public class GreedRules {
    private boolean[] dieUsedForScoring;

    public GreedRules() {

    }

    /**
     * Calculates the current round's score.
     *
     * @param dieList the list containing all dice and their values.
     * @return the total score given from triplets, straight and number values.
     */
    public int calculateRoundScore(ArrayList<Die> dieList) {
        dieUsedForScoring = new boolean[dieList.size()];
        int total = calculateStraightScore(dieList);
        total += calculateTripletScore(dieList);
        total += calculateNumberScore(dieList);

        return total;
    }

    /**
     * Check if a certain die is allowed for hold. It is allowed if the die is a part of a straight, triplet or a 1 or 5.
     *
     * @param dieNbr
     * @param dieList
     * @return
     */
    public boolean dieAllowedForHold(int dieNbr, ArrayList<Die> dieList) {
        int diceValue = dieList.get(dieNbr).getDieValue();
        dieUsedForScoring = new boolean[dieList.size()];
        if (diceValue == 1 || diceValue == 5 || calculateStraightScore(dieList) != 0 || calculateSingleTripletScore(diceValue, dieList) != 0) {
            return true;
        }
        return false;
    }

    /**
     * Calculates if the user has a straight. I'm assuming the straight must be when the dice are in the correct order 1,2,3,4,5,6 and not in any order such as 1,5,2,4,3,6
     *
     * @param dieList the list containing all dice and their values.
     * @return the score from the straight
     */
    private int calculateStraightScore(ArrayList<Die> dieList) {
        if(dieList.size() != 6){
            return 0;
        }
        int straightValue = 1;
        for (Die d : dieList) {
            if (d.getDieValue() != straightValue) {
                return 0;
            }
            straightValue++;
        }
        for (boolean b : dieUsedForScoring) {
            b = true;
        }
        return 1000;
    }

    /**
     * Calculates the total triplet score given from a roll of dice.
     *
     * @param dieList the list containing all dice and their values.
     * @return the score given from triplets.
     */
    private int calculateTripletScore(ArrayList<Die> dieList) {
        int total = 0;
        for (int i = 1; i <= 6; i++) {
            int points = calculateSingleTripletScore(i, dieList);
            total += points;
        }
        return total;
    }

    /**
     * Calculates the triplet score for a specific dice value.
     *
     * @param diceValue the dice value to calculate triplet score against.
     * @param dieList   the list of dices to check for triplet score
     * @return the score from a single triplet check.
     */
    private int calculateSingleTripletScore(int diceValue, ArrayList<Die> dieList) {
        int nbrOfAKind = 0;
        for (Die d : dieList) {
            if (d.getDieValue() == diceValue) {
                nbrOfAKind++;
            }
        }
        if (nbrOfAKind >= 3) {
            for (int i = 0; i < dieList.size(); i++) {
                if (dieList.get(i).getDieValue() == diceValue) {
                    dieUsedForScoring[i] = true;
                }
            }
            if (diceValue != 1) {
                return diceValue * 100;
            } else {
                return 1000;
            }
        }
        return 0;
    }

    /**
     * Calculates the score given from 1s and 5s which are not in a straight or in a triplet.
     *
     * @param dieList the list containing all dice and their values.
     * @return the score given from the numbers 1 and 5 which are not in straight or triplet.
     */
    private int calculateNumberScore(ArrayList<Die> dieList) {
        int numberScore = 0;

        for (int i = 0; i < dieList.size(); i++) {
            if (dieList.get(i).getDieValue() == 1 && !dieUsedForScoring[i]) {
                numberScore += 100;
            } else if (dieList.get(i).getDieValue() == 5 && !dieUsedForScoring[i]) {
                numberScore += 50;
            }
        }
        return numberScore;
    }


}
