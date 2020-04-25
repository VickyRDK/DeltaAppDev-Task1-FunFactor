package com.example.deltafactorrefined;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.os.Trace;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class FactorActivity extends AppCompatActivity {
    public static final String SCORING = "scoring";
    private static final long COUNTDOWN_IN_MILLIS = 10000;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";

    private static final String KEY_SCORE = "keyscore";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_ANSWERED_RIGHT = "keyansweredright";
    private static final String KEY_OPTION_ARRAY = "keyoptionarray";
    private static final String KEY_READY_FLAG = "keyreadyflag";
    private static final String KEY_VIBRATOR_FLAG = "keyvibratorflag";
    private static final String KEY_CORRECT_ANSWER = "keycrctanswer";
    private static final String KEY_RB_CHECK = "keyrbcheck";

    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    String num1;
    int strnum;
    private int crctanswer;
    private int vibratorFlag;
    private long backPressedTime;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmAnswer;
    private int score;
    private boolean answered;
    private boolean answerright;
    private boolean rbcheck;
    ArrayList<Integer> optionarray = new ArrayList<>();
    TextView scoreCounter;
    Button ready;
    Vibrator vibrator;
    EditText number;
    TextView timer;
    TextView title;
    Button finishAnswer;
    ImageView image11;
    ImageView image12;
    Boolean readyflag;
    TextView longstreak;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_factor);

        int strnum;
        ready = (Button) findViewById(R.id.ready);
        rbGroup = (RadioGroup) findViewById(R.id.radio_group);
        rb1 = (RadioButton) findViewById(R.id.Option_1);
        rb2 = (RadioButton) findViewById(R.id.Option_2);
        rb3 = (RadioButton) findViewById(R.id.Option_3);
        scoreCounter = (TextView) findViewById(R.id.scorecounter);
        title = (TextView) findViewById(R.id.title);
        buttonConfirmAnswer = (Button) findViewById(R.id.confirm_answer);
        finishAnswer = (Button) findViewById(R.id.finish);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        number = (EditText) findViewById(R.id.edittextnum);
        timer = (TextView) findViewById(R.id.timer);
        textColorDefaultCd = timer.getTextColors();
        image11 = (ImageView) findViewById(R.id.greenport);
        image12 = (ImageView) findViewById(R.id.redport);
        longstreak = (TextView) findViewById(R.id.longstreak);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        longstreak.setText("Longest Streak: " + highscore);



        if (savedInstanceState == null) {
            nextnumber();
        } else {
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            answerright = savedInstanceState.getBoolean(KEY_ANSWERED_RIGHT);
            optionarray = savedInstanceState.getIntegerArrayList(KEY_OPTION_ARRAY);
            readyflag = savedInstanceState.getBoolean(KEY_READY_FLAG);
            vibratorFlag = savedInstanceState.getInt(KEY_VIBRATOR_FLAG);
            crctanswer = savedInstanceState.getInt(KEY_CORRECT_ANSWER);
            rbcheck = savedInstanceState.getBoolean(KEY_RB_CHECK);

            if(rbcheck== true){
                rb1.setEnabled(false);
                rb2.setEnabled(false);
                rb3.setEnabled(false);
                buttonConfirmAnswer.setVisibility(View.GONE);
            }else {
                assert true;
            }

            if (readyflag == true) {
                ready.setVisibility(View.GONE);
                number.setVisibility(View.GONE);
                rbGroup.setVisibility(View.VISIBLE);
                buttonConfirmAnswer.setVisibility(View.GONE);
                if (!answered) {

                    startCountDown();
                } else {
                    updateCountDownText();
                    showAnswer();
                }
            } else {
                assert true;
            }


        }

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number.getText().toString().trim().length() <= 0) {
                    Toast.makeText(FactorActivity.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(number.getText().toString()) == 0) {
                    Toast.makeText(FactorActivity.this, "Please enter a number greater than zero", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(number.getText().toString()) == 1 || Integer.parseInt(number.getText().toString()) == 2 || Integer.parseInt(number.getText().toString()) == 3 || Integer.parseInt(number.getText().toString()) == 4) {
                    Toast.makeText(FactorActivity.this, "Enter a number greater than 4", Toast.LENGTH_SHORT).show();
                } else {
                    readyflag = true;
                    readyButton();
                }
            }
        });

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbcheck = true;
                rb1.setEnabled(false);
                rb2.setEnabled(false);
                rb3.setEnabled(false);
                checkSelection();
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbcheck = true;
                rb1.setEnabled(false);
                rb2.setEnabled(false);
                rb3.setEnabled(false);
                checkSelection();
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbcheck = true;
                rb1.setEnabled(false);
                rb2.setEnabled(false);
                rb3.setEnabled(false);
                checkSelection();
            }
        });

        buttonConfirmAnswer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkSelection();
            }
        });

        finishAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishgame();
            }
        });

    }

    private void readyButton() {
        int intnum1 = Integer.parseInt(number.getText().toString());
        String intnum1str = String.valueOf(intnum1);
        title.setText(intnum1str);
        ArrayList<Integer> optionarray = factorize(intnum1);
        rb1.setText(Integer.toString(optionarray.get(0)));
        rb2.setText(Integer.toString(optionarray.get(1)));
        rb3.setText(Integer.toString(optionarray.get(2)));
        rbGroup.setVisibility(View.VISIBLE);
        ready.setVisibility(View.GONE);
        number.setVisibility(View.GONE);
        buttonConfirmAnswer.setVisibility(View.GONE);
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();
    }


    public void showAnswer() {
        if (answerright == true) {
            rb1.setTextColor(Color.BLACK);
            rb2.setTextColor(Color.BLACK);
            rb3.setTextColor(Color.BLACK);
            scoreCounter.setText("Current Streak: " + score);
            title.setText("CORRECT ANSWER");
            title.setTextColor(Color.BLACK);
            scoreCounter.setTextColor(Color.BLACK);
            timer.setTextColor(Color.BLACK);
            image11.setVisibility(View.VISIBLE);
            buttonConfirmAnswer.setVisibility(View.VISIBLE);
            buttonConfirmAnswer.setText("NEXT");
        }
        if (answerright == false) {
            rb1.setTextColor(Color.BLACK);
            rb2.setTextColor(Color.BLACK);
            rb3.setTextColor(Color.BLACK);
            title.setTextColor(Color.BLACK);
            scoreCounter.setTextColor(Color.BLACK);
            timer.setTextColor(Color.BLACK);
            title.setText("      Wrong Answer " + "\n" + crctanswer + " is the Right Answer");
            scoreCounter.setText("Current Streak: " + score);
            vibratorFlag++;
            if (vibratorFlag == 1) {
                vibrator.vibrate(600);
            } else {
                assert true;
            }
            buttonConfirmAnswer.setVisibility(View.GONE);
            image12.setVisibility(View.VISIBLE);
            buttonConfirmAnswer.setVisibility(View.GONE);
        }

    }

    private void checkSelectiontimer() {
        if (!answered) {
            if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                checkAnswer();
            } else {
                if (readyflag == true) {
                    rb1.setEnabled(false);
                    rb2.setEnabled(false);
                    rb3.setEnabled(false);
                    rb1.setTextColor(Color.BLACK);
                    rb2.setTextColor(Color.BLACK);
                    rb3.setTextColor(Color.BLACK);
                    title.setTextColor(Color.BLACK);
                    scoreCounter.setTextColor(Color.BLACK);
                    timer.setTextColor(Color.BLACK);
                    title.setText("   No Option Selected " + "\n" + crctanswer + " is the Right Answer");
                    scoreCounter.setText("Current Streak: " + score);
                    vibratorFlag++;
                    if (vibratorFlag == 1) {
                        vibrator.vibrate(600);
                    } else {
                        assert true;
                    }
                    buttonConfirmAnswer.setVisibility(View.GONE);
                    image12.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(FactorActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    buttonConfirmAnswer.setVisibility(View.GONE);
                }

            }
        } else {
            nextnumber();
        }
    }

    private void checkSelection() {
        if (!answered) {
            if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                checkAnswer();
            } else {
                Toast.makeText(FactorActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        } else {
            nextnumber();
        }
    }

    private void finishgame() {
        Intent s = new Intent(FactorActivity.this, MainActivity.class);
        s.putExtra(SCORING, score);
        setResult(RESULT_OK, s);
        finish();
    }

    private void nextnumber() {
        rbcheck = false;
        readyflag = false;
        rb1.setEnabled(true);
        rb2.setEnabled(true);
        rb3.setEnabled(true);
        rb1.setTextColor(Color.WHITE);
        rb2.setTextColor(Color.WHITE);
        rb3.setTextColor(Color.WHITE);
        title.setTextColor(Color.WHITE);
        scoreCounter.setTextColor(Color.WHITE);
        timer.setTextColor(Color.WHITE);
        number.setVisibility(View.GONE);
        ready.setVisibility(View.VISIBLE);
        number.setVisibility(View.VISIBLE);
        title.setText("Enter a Number");
        answered = false;
        answerright = false;
        number.setText("");
        rbGroup.setVisibility(View.GONE);
        buttonConfirmAnswer.setVisibility(View.GONE);
        buttonConfirmAnswer.setText("CONFIRM ANSWER");
        rbGroup.clearCheck();
        timer.setText("00:10");
        image11.setVisibility(View.GONE);
        image12.setVisibility(View.GONE);
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkSelectiontimer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeFormatted);

        if (timeLeftInMillis < 4000) {
            timer.setTextColor(Color.WHITE);
        } else {
            timer.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        countDownTimer.cancel();
        answered = true;
        answerright = false;
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        strnum = Integer.parseInt((String) title.getText());
        int selectedAnswer = Integer.parseInt((String) rbSelected.getText());
        if (strnum % selectedAnswer == 0) {
            answerright = true;
            score++;
        }
        showAnswer();
    }


    public ArrayList<Integer> factorize(int number) {

        ArrayList<Integer> factors = new ArrayList<Integer>();
        ArrayList<Integer> optionlist = new ArrayList<Integer>();
        Random random = new Random();
        int rnum;


        for (int i = 2; i <= (int) number; i++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }

        Collections.shuffle(factors);
        Collections.shuffle(factors);
        optionlist.add(factors.get(0));
        crctanswer = optionlist.get(0);

        while (optionlist.size() != 3) {
            int count = 0;
            int count1 = 0;
            rnum = random.nextInt(number);
            for (int j : factors) {
                if (rnum == j || rnum == 1 || rnum == 0) {
                    count = count + 1;
                }
            }
            for (int l : optionlist) {
                if (rnum == l || rnum == 1 || rnum == 0) {
                    count1 = count1 + 1;
                }
            }

            if (count == 0 && count1 == 0) {
                optionlist.add(rnum);
            } else {
                assert true;
            }
        }

        Collections.shuffle(optionlist);
        Collections.shuffle(optionlist);

        return optionlist;
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishgame();
        } else {
            Toast.makeText(this, "Press again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putBoolean(KEY_ANSWERED_RIGHT, answerright);
        outState.putIntegerArrayList(KEY_OPTION_ARRAY, optionarray);
        outState.putBoolean(KEY_READY_FLAG, readyflag);
        outState.putInt(KEY_VIBRATOR_FLAG, vibratorFlag);
        outState.putInt(KEY_CORRECT_ANSWER, crctanswer);
        outState.putBoolean(KEY_RB_CHECK, rbcheck);
    }
}
