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


public class StartActivity extends ActionBarActivity {
    private boolean onGoingGameExists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedPreferences prefs = getSharedPreferences("greed", Context.MODE_PRIVATE);
        onGoingGameExists = prefs.getBoolean("resume", false);
        if(!onGoingGameExists){
            Button button = (Button) findViewById(R.id.resumeButton);
            button.setAlpha(0.3f);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

    public void startNewGame(View view) {
        SharedPreferences prefs = getSharedPreferences("greed", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("resume",false).apply();
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }

    public void resumeGame(View view) {
        if(onGoingGameExists) {
            Intent intent = new Intent(this, GameActivity .class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("greed", Context.MODE_PRIVATE);
        onGoingGameExists = prefs.getBoolean("resume", false);
        if(!onGoingGameExists){
            Button button = (Button) findViewById(R.id.resumeButton);
            button.setAlpha(0.3f);
        }else{
            Button button = (Button) findViewById(R.id.resumeButton);
            button.setAlpha(1f);
        }
    }

    @Override
    public void onBackPressed() {
        //Save preferences instead
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        StartActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
