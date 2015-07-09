package com.example.denhi.greed;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class GameActivity extends ActionBarActivity {
    private Greed greed;
    private ImageView dice1, dice2, dice3, dice4, dice5, dice6;
    private TextView turnScore, totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dice1 = (ImageView) findViewById(R.id.diceImage1);
        dice2 = (ImageView) findViewById(R.id.diceImage2);
        dice3 = (ImageView) findViewById(R.id.diceImage3);
        dice4 = (ImageView) findViewById(R.id.diceImage4);
        dice5 = (ImageView) findViewById(R.id.diceImage5);
        dice6 = (ImageView) findViewById(R.id.diceImage6);
        turnScore = (TextView) findViewById(R.id.turnScore);
        totalScore = (TextView) findViewById(R.id.totalScore);


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("greed", Context.MODE_PRIVATE);
        boolean onGoingGameExists = prefs.getBoolean("resume", false);
        if(!onGoingGameExists){
            greed = new Greed();
        }else{
            greed = resumeGreedGame();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        greed.saveGameInstance(this);
    }

    private Greed resumeGreedGame() {
        SharedPreferences prefs = getSharedPreferences("greed", Context.MODE_PRIVATE);
        int totalScore = prefs.getInt("totalScore", -1);
        int turnScore = prefs.getInt("turnScore", -1);
        this.totalScore.setText(Integer.toString(totalScore));
        this.turnScore.setText(Integer.toString(turnScore));
        int rounds = prefs.getInt("rounds", -1);
        int lastRollScore = prefs.getInt("lastRollScore", -1);
        boolean newRound = prefs.getBoolean("newRound", false);
        boolean lastRollHadFullHold = prefs.getBoolean("lastRollHadFullHold", false);
        ArrayList<Dice> dices = getResumedGameDiceList();
        for (int i = 0; i < 6; i++) {
            drawDice(dices.get(i), i + 1);
        }
        return new Greed(totalScore,turnScore,rounds,lastRollScore,newRound,lastRollHadFullHold,dices);
    }

    private ArrayList<Dice> getResumedGameDiceList() {
        SharedPreferences prefs = getSharedPreferences("greed", Context.MODE_PRIVATE);
        int dice1 = prefs.getInt("dice1", -1);
        boolean dice1Hold = prefs.getBoolean("dice1hold", false);
        int dice2 = prefs.getInt("dice2", -1);
        boolean dice2Hold = prefs.getBoolean("dice2hold", false);
        int dice3 = prefs.getInt("dice3", -1);
        boolean dice3Hold = prefs.getBoolean("dice3hold", false);
        int dice4 = prefs.getInt("dice4", -1);
        boolean dice4Hold = prefs.getBoolean("dice4hold", false);
        int dice5 = prefs.getInt("dice5", -1);
        boolean dice5Hold = prefs.getBoolean("dice5hold", false);
        int dice6 = prefs.getInt("dice6", -1);
        boolean dice6Hold = prefs.getBoolean("dice6hold", false);
        ArrayList<Dice> diceList = new ArrayList<>();
        diceList.add(new Dice(dice1,dice1Hold));
        diceList.add(new Dice(dice2,dice2Hold));
        diceList.add(new Dice(dice3,dice3Hold));
        diceList.add(new Dice(dice4,dice4Hold));
        diceList.add(new Dice(dice5,dice5Hold));
        diceList.add(new Dice(dice6,dice6Hold));
        return diceList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void score(View view) {
        boolean hasWon = greed.addToTotalScore();
        int total = greed.getTotalScore();
        int turn = greed.getTurnScore();
        if (!hasWon) {
            totalScore.setText(Integer.toString(total));
            turnScore.setText(Integer.toString(turn));
            Toast.makeText(getApplicationContext(), getString(R.string.greed_round_over),
                    Toast.LENGTH_SHORT).show();
            ArrayList<Dice> diceValues = greed.getDiceList();
            for (int i = 0; i < 6; i++) {
                drawDice(diceValues.get(i), i + 1);
            }

        } else {
            //Erase info to start new game
            Intent intent = new Intent(this, WinScreenActivity.class);
            intent.putExtra("Total", total);
            intent.putExtra("Rounds", greed.getRounds());
            startActivity(intent);
        }
    }

    public void roll(View view) {
        boolean success = greed.rollAllDices();
        if (!success) {
            Toast.makeText(getApplicationContext(), getString(R.string.Roll_fail_message),
                    Toast.LENGTH_SHORT).show();
        } else {
            int thisTurnScore = greed.evaluateScore();
            turnScore.setText(Integer.toString(thisTurnScore));
            ArrayList<Dice> diceValues = greed.getDiceList();
            for (int i = 0; i < 6; i++) {
                drawDice(diceValues.get(i), i + 1);
            }
            if (greed.isNewRound()) {
                Toast.makeText(getApplicationContext(), getString(R.string.greed_round_over),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Draws the new image to the correct imageView.
     *
     * @param dice       The value of the new dice
     * @param diceNumber Which dice to change.
     */
    private void drawDice(Dice dice, int diceNumber) {
        int imageDrawableId = findImageResource(dice.getDiceValue());
        boolean holdDice = dice.getHoldDice();
        if (imageDrawableId != -1) {
            switch (diceNumber) {
                case 1:
                    dice1.setImageResource(imageDrawableId);
                    if (!holdDice) {
                        dice1.setAlpha(1.0f);
                    }else{
                        dice1.setAlpha(0.5f);
                    }
                    break;
                case 2:
                    dice2.setImageResource(imageDrawableId);
                    if (!holdDice) {
                        dice2.setAlpha(1.0f);
                    }else{
                        dice2.setAlpha(0.5f);
                    }
                    break;
                case 3:
                    dice3.setImageResource(imageDrawableId);
                    if (!holdDice) {
                        dice3.setAlpha(1.0f);
                    }else{
                        dice3.setAlpha(0.5f);
                    }
                    break;
                case 4:
                    dice4.setImageResource(imageDrawableId);
                    if (!holdDice) {
                        dice4.setAlpha(1.0f);
                    }else{
                        dice4.setAlpha(0.5f);
                    }
                    break;
                case 5:
                    dice5.setImageResource(imageDrawableId);
                    if (!holdDice) {
                        dice5.setAlpha(1.0f);
                    }else{
                        dice5.setAlpha(0.5f);
                    }
                    break;
                case 6:
                    dice6.setImageResource(imageDrawableId);
                    if (!holdDice) {
                        dice6.setAlpha(1.0f);
                    }else{
                        dice6.setAlpha(0.5f);
                    }
                    break;

            }
        }
    }

    /**
     * Finds the drawable id for the correct dice.
     *
     * @param diceValue The value of the dice.
     * @return The drawable id. -1 if not found.
     */
    private int findImageResource(int diceValue) {
        switch (diceValue) {
            case 1:
                return R.drawable.dice1;
            case 2:
                return R.drawable.dice2;
            case 3:
                return R.drawable.dice3;
            case 4:
                return R.drawable.dice4;
            case 5:
                return R.drawable.dice5;
            case 6:
                return R.drawable.dice6;

        }
        return -1; //This should never happen
    }


    public void saveDiceValue(View view) {
        boolean success = false;
        switch (view.getId()) {
            case R.id.diceImage1:
                success = greed.setHoldDice(0, true);
                if(success) {
                    dice1.setAlpha(0.5f);
                }
                break;
            case R.id.diceImage2:
                success = greed.setHoldDice(1, true);
                if(success) {
                    dice2.setAlpha(0.5f);
                }
                break;
            case R.id.diceImage3:
                success = greed.setHoldDice(2,true);
                if(success) {
                    dice3.setAlpha(0.5f);
                }
                break;
            case R.id.diceImage4:
                success = greed.setHoldDice(3, true);
                if(success) {
                    dice4.setAlpha(0.5f);
                }
                break;
            case R.id.diceImage5:
                success = greed.setHoldDice(4, true);
                if(success) {
                    dice5.setAlpha(0.5f);
                }
                break;
            case R.id.diceImage6:
                success = greed.setHoldDice(5, true);
                if(success) {
                    dice6.setAlpha(0.5f);
                }
                break;
        }
        if(!success){
            Toast.makeText(getApplicationContext(), "Can not hold any dice during first round",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        greed.saveGameInstance(this);
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
    }





}
