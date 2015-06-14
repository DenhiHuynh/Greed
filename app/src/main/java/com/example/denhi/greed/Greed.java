package com.example.denhi.greed;

/**
 * Created by Denhi on 2015-06-08.
 */
public class Greed {
    private int totalScore, turnScore, rounds;

    public Greed(){
        totalScore = 0;
        turnScore = 0;
        rounds = 0;
    }


    /**
     * Evaluates score according to greed rules
     * @return
     */
    public int evaluateScore(){
        //Check how many point a dice throw should give
        return 0;
    }

    public boolean hasWon(){
        return totalScore > 10000;
    }


}
