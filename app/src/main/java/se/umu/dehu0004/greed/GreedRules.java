package se.umu.dehu0004.greed;

import java.util.ArrayList;

/**
 * Created by Denhi on 2015-06-14.
 */
public class GreedRules {

    /**
     * Calculates the current round's score.
     *
     * @param dieList the list containing all dice and their values.
     * @return the total score given from triplets, straight and number values.
     */
    public static int calculateRoundScore(ArrayList<Die> dieList) {
        int total = calculateStraightScore(dieList);
        total += calculateNumberScore(dieList);
        total += calculateTripletScore(dieList);
        return total;
    }

    /**
     * Calculates the score given from 1s and 5s which are not in a straight or in a triplet.
     *
     * @param dieList the list containing all dice and their values.
     * @return the score given from the numbers 1 and 5 which are not in straight or triplet.
     */
    private static int calculateNumberScore(ArrayList<Die> dieList) {
        int numberScore = 0;
        if (calculateStraightScore(dieList) == 0) {
            if (calculateSingleTripletScore(1, dieList) == 0) {
                for (Die d : dieList) {
                    if (d.getDieValue() == 1) {
                        numberScore += 100;
                    }
                }
            }
            if (calculateSingleTripletScore(5, dieList) == 0) {
                for (Die d : dieList) {
                    if (d.getDieValue() == 5) {
                        numberScore += 50;
                    }
                }
            }
        }
        return numberScore;
    }

    /**
     * Calculates if the user has a straight. I'm assuming the straight must be when the dice are in the correct order 1,2,3,4,5,6 and not in any order such as 1,5,2,4,3,6
     *
     * @param dieList the list containing all dice and their values.
     * @return the score from the straight
     */
    private static int calculateStraightScore(ArrayList<Die> dieList) {
        int straightValue = 1;
        for (Die d : dieList) {
            if (d.getDieValue() != straightValue) {
                return 0;
            }
            straightValue++;
        }
        return 1000;
    }

    /**
     * Calculates the total triplet score given from a roll of dice.
     *
     * @param dieList the list containing all dice and their values.
     * @return the score given from triplets.
     */
    private static int calculateTripletScore(ArrayList<Die> dieList) {
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
     * @param dieList  the list of dices to check for triplet score
     * @return the score from a single triplet check.
     */
    private static int calculateSingleTripletScore(int diceValue, ArrayList<Die> dieList) {
        int nbrOfAKind = 0;
        for (Die d : dieList) {
            if (d.getDieValue() == diceValue) {
                nbrOfAKind++;
            }
        }
        if (nbrOfAKind >= 3) {
            if (diceValue != 1) {
                return diceValue * 100;
            } else {
                return 1000;
            }
        }
        return 0;
    }
}
