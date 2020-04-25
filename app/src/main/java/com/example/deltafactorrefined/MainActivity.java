package com.example.deltafactorrefined;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SCORE = 1;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_TEXT_SCORE = "keytextscore";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private TextView textViewHighscore;
    private int highscore;
    int score;

    TextView lastgame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        textViewHighscore = (TextView) findViewById(R.id.textview_streak);
        loadStreak();
        Button buttonStart = findViewById(R.id.button_start);
        lastgame = (TextView) findViewById(R.id.lastgame);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    private void startGame() {
        Intent intent = new Intent(MainActivity.this, FactorActivity.class);
        startActivityForResult(intent, REQUEST_SCORE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SCORE) {
            if (resultCode == RESULT_OK) {
                score = data.getIntExtra(FactorActivity.SCORING, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }

        }
    }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setText("Longest Streak: " + highscore);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.commit();
    }

    private void loadStreak() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Longest Streak: " + highscore);
    }
}