package com.example.denhi.greed;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private Greed greed;
    private ImageView dice1, dice2, dice3, dice4, dice5, dice6;
    private TextView turnScore, totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        greed = new Greed();
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

    public void Score(View view) {
        boolean hasWon = greed.addToTotalScore();
        int total = greed.getTotalScore();
        if(!hasWon) {
            totalScore.setText(Integer.toString(total));
            turnScore.setText(Integer.toString(0));
        }else{
            //Erase info to start new game
            Intent intent = new Intent(this,WinScreenActivity.class);
            intent.putExtra("Total", total);
            intent.putExtra("Rounds",greed.getRounds());
            startActivity(intent);
        }
    }

    public void Roll(View view) {
        ArrayList<Integer> diceValues = greed.rollAllDices();
        for (int i = 0; i < 6; i++) {
            drawDice(diceValues.get(i), i+1);
        }
        int thisTurnScore = greed.evaluateScore();
        turnScore.setText(Integer.toString(thisTurnScore));
    }

    /**
     * Draws the new image to the correct imageView.
     *
     * @param diceValue  The value of the new dice
     * @param diceNumber Which dice to change.
     */
    private void drawDice(int diceValue, int diceNumber) {
        int imageDrawableId = findImageResource(diceValue);
        if (imageDrawableId != -1) {
            switch (diceNumber) {
                case 1:
                    dice1.setImageResource(imageDrawableId);
                    break;
                case 2:
                    dice2.setImageResource(imageDrawableId);
                    break;
                case 3:
                    dice3.setImageResource(imageDrawableId);
                    break;
                case 4:
                    dice4.setImageResource(imageDrawableId);
                    break;
                case 5:
                    dice5.setImageResource(imageDrawableId);
                    break;
                case 6:
                    dice6.setImageResource(imageDrawableId);
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


}
