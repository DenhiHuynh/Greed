package se.umu.dehu0004.greed;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class WinScreenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_screen);

        Intent intent = getIntent();
        int totalScore = intent.getIntExtra("Total", -1);
        int rounds = intent.getIntExtra("Rounds", -1);

        TextView winText = (TextView) findViewById(R.id.winText);
        winText.setText("You got " + totalScore + " \nin " + rounds + " rounds.");
        getSharedPreferences("greed", Context.MODE_PRIVATE).edit().putBoolean("resume",false).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_win_screen, menu);
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
     * Starts a new greed game.
     * @param view
     */
    public void startNewGame(View view){
        getSharedPreferences("greed", Context.MODE_PRIVATE).edit().putBoolean("resume",false).apply();
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getSharedPreferences("greed", Context.MODE_PRIVATE).edit().putBoolean("resume",false).apply();
    }

    @Override
    public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setTitle("Start new game")
                    .setMessage("Would you like to start a new game?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            getSharedPreferences("greed", Context.MODE_PRIVATE).edit().putBoolean("resume",false).apply();
                            startNewGame(null);
                        }
                    }).create().show();


    }
}
