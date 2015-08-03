package se.umu.dehu0004.greed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class GameActivity extends ActionBarActivity {
    private Greed greed;
    private ImageView die1, die2, die3, die4, die5, die6;
    private TextView turnScore, totalScore, rounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        die1 = (ImageView) findViewById(R.id.dieImage1);
        die2 = (ImageView) findViewById(R.id.dieImage2);
        die3 = (ImageView) findViewById(R.id.dieImage3);
        die4 = (ImageView) findViewById(R.id.dieImage4);
        die5 = (ImageView) findViewById(R.id.dieImage5);
        die6 = (ImageView) findViewById(R.id.dieImage6);
        turnScore = (TextView) findViewById(R.id.turnScore);
        totalScore = (TextView) findViewById(R.id.totalScore);
        rounds = (TextView) findViewById(R.id.rounds);

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

    /**
     * Gathers values from previously saved game.
     * @return a new Greed object containing the values from the last saved game state.
     */
    private Greed resumeGreedGame() {
        SharedPreferences prefs = getSharedPreferences("greed", Context.MODE_PRIVATE);
        int totalScore = prefs.getInt("totalScore", -1);
        int turnScore = prefs.getInt("turnScore", -1);
        int rounds = prefs.getInt("rounds", -1);
        this.totalScore.setText(Integer.toString(totalScore));
        this.turnScore.setText(Integer.toString(turnScore));
        this.rounds.setText(Integer.toString(rounds));
        int lastRollScore = prefs.getInt("lastRollScore", -1);
        boolean newRound = prefs.getBoolean("newRound", false);
        boolean lastRollHadFullHold = prefs.getBoolean("lastRollHadFullHold", false);
        ArrayList<Die> dies = getResumedGameDiceList();
        for (int i = 0; i < 6; i++) {
            drawDie(dies.get(i), i + 1);
        }
        return new Greed(totalScore,turnScore,rounds,lastRollScore,newRound,lastRollHadFullHold, dies);
    }

    /**
     * Gathers dice values from shared preferences.
     * @return the list of dices from the last saved game state.
     */
    private ArrayList<Die> getResumedGameDiceList() {
        SharedPreferences prefs = getSharedPreferences("greed", Context.MODE_PRIVATE);
        int die1 = prefs.getInt("die1", -1);
        boolean die1Hold = prefs.getBoolean("die1hold", false);
        int die2 = prefs.getInt("die2", -1);
        boolean die2Hold = prefs.getBoolean("die2hold", false);
        int die3 = prefs.getInt("die3", -1);
        boolean die3Hold = prefs.getBoolean("die3hold", false);
        int die4 = prefs.getInt("die4", -1);
        boolean die4Hold = prefs.getBoolean("die4hold", false);
        int die5 = prefs.getInt("die5", -1);
        boolean die5Hold = prefs.getBoolean("die5hold", false);
        int die6 = prefs.getInt("die6", -1);
        boolean die6Hold = prefs.getBoolean("die6hold", false);
        ArrayList<Die> dieList = new ArrayList<>();
        dieList.add(new Die(die1,die1Hold));
        dieList.add(new Die(die2,die2Hold));
        dieList.add(new Die(die3,die3Hold));
        dieList.add(new Die(die4,die4Hold));
        dieList.add(new Die(die5,die5Hold));
        dieList.add(new Die(die6,die6Hold));
        return dieList;
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

    /**
     * Saves the points gathered in the current round.
     * @param view the view which called this method.
     */
    public void score(View view) {
        boolean hasWon = greed.addToTotalScore();
        int total = greed.getTotalScore();
        int turn = greed.getTurnScore();
        int roundsNumber = greed.getRounds();
        if (!hasWon) {
            totalScore.setText(Integer.toString(total));
            turnScore.setText(Integer.toString(turn));
            rounds.setText(Integer.toString(roundsNumber));
            Toast.makeText(getApplicationContext(), getString(R.string.greed_round_over),
                    Toast.LENGTH_SHORT).show();
            ArrayList<Die> dieValues = greed.getDieList();
            for (int i = 0; i < 6; i++) {
                drawDie(dieValues.get(i), i + 1);
            }

        } else {
            //Erase info to start new game
            Intent intent = new Intent(this, WinScreenActivity.class);
            intent.putExtra("Total", total);
            intent.putExtra("Rounds", greed.getRounds());
            startActivity(intent);
        }
    }

    /**
     * Rolls the dice which can be rolled(i.e not held dice). Also draws the new dice.
     * @param view the view which called this method.
     */
    public void roll(View view) {
        boolean success = greed.rollAllDice();
        if (!success) {
            Toast.makeText(getApplicationContext(), getString(R.string.Roll_fail_message),
                    Toast.LENGTH_SHORT).show();
        } else {
            int thisTurnScore = greed.evaluateScore();
            turnScore.setText(Integer.toString(thisTurnScore));
            ArrayList<Die> dieValues = greed.getDieList();
            for (int i = 0; i < 6; i++) {
                drawDie(dieValues.get(i), i + 1);
            }
            if (greed.isNewRound()) {
                rounds.setText(Integer.toString(greed.getRounds()));
                Toast.makeText(getApplicationContext(), getString(R.string.greed_round_over),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Draws the new image to the correct imageView.
     *
     * @param die       The value of the new die
     * @param dieNumber Which die to change.
     */
    private void drawDie(Die die, int dieNumber) {
        int imageDrawableId = findImageResource(die.getDieValue());
        boolean holdDie = die.getHoldDie();
        if (imageDrawableId != -1) {
            switch (dieNumber) {
                case 1:
                    die1.setImageResource(imageDrawableId);
                    if (!holdDie) {
                        die1.setAlpha(1.0f);
                    }else{
                        die1.setAlpha(0.5f);
                    }
                    break;
                case 2:
                    die2.setImageResource(imageDrawableId);
                    if (!holdDie) {
                        die2.setAlpha(1.0f);
                    }else{
                        die2.setAlpha(0.5f);
                    }
                    break;
                case 3:
                    die3.setImageResource(imageDrawableId);
                    if (!holdDie) {
                        die3.setAlpha(1.0f);
                    }else{
                        die3.setAlpha(0.5f);
                    }
                    break;
                case 4:
                    die4.setImageResource(imageDrawableId);
                    if (!holdDie) {
                        die4.setAlpha(1.0f);
                    }else{
                        die4.setAlpha(0.5f);
                    }
                    break;
                case 5:
                    die5.setImageResource(imageDrawableId);
                    if (!holdDie) {
                        die5.setAlpha(1.0f);
                    }else{
                        die5.setAlpha(0.5f);
                    }
                    break;
                case 6:
                    die6.setImageResource(imageDrawableId);
                    if (!holdDie) {
                        die6.setAlpha(1.0f);
                    }else{
                        die6.setAlpha(0.5f);
                    }
                    break;

            }
        }
    }

    /**
     * Finds the drawable id for the correct die.
     *
     * @param dieValue The value of the die.
     * @return The drawable id. -1 if not found.
     */
    private int findImageResource(int dieValue) {
        switch (dieValue) {
            case 1:
                return R.drawable.die1;
            case 2:
                return R.drawable.die2;
            case 3:
                return R.drawable.die3;
            case 4:
                return R.drawable.die4;
            case 5:
                return R.drawable.die5;
            case 6:
                return R.drawable.die6;

        }
        return -1; //This should never happen
    }


    public void saveDieValue(View view) {
        boolean success = false;
        switch (view.getId()) {
            case R.id.dieImage1:
                success = greed.setHoldDie(0, true);
                if(success) {
                    die1.setAlpha(0.5f);
                }
                break;
            case R.id.dieImage2:
                success = greed.setHoldDie(1, true);
                if(success) {
                    die2.setAlpha(0.5f);
                }
                break;
            case R.id.dieImage3:
                success = greed.setHoldDie(2, true);
                if(success) {
                    die3.setAlpha(0.5f);
                }
                break;
            case R.id.dieImage4:
                success = greed.setHoldDie(3, true);
                if(success) {
                    die4.setAlpha(0.5f);
                }
                break;
            case R.id.dieImage5:
                success = greed.setHoldDie(4, true);
                if(success) {
                    die5.setAlpha(0.5f);
                }
                break;
            case R.id.dieImage6:
                success = greed.setHoldDie(5, true);
                if(success) {
                    die6.setAlpha(0.5f);
                }
                break;
        }
        if(!success){
            Toast.makeText(getApplicationContext(), "Can not hold any die during first round",
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
