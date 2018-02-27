package com.domain.fives;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


public class GameBoard extends AppCompatActivity implements View.OnClickListener {


    TextView sTitleTxt, scoreTxt, letter1, letter2, letter3, letter4, letter5;
    ProgressBar pb;
    Button letterButt1, letterButt2, letterButt3, letterButt4, letterButt5;


    Timer gameTimer;
    float progress;
    List<String> wordsArray;
    List<String> charArray;
    String wordStr = "";
    int tapsCount = 0;
    String firstWord = "";
    String secondWord = "";
    String thirdWord = "";
    String wordByCharacters = "";
    int randomCircle = 0;
    Button [] letterButtons;
    TextView [] letterTxts;
    MediaPlayer mp;


    @Override
    protected void onStart() {
        super.onStart();


        Configs.score = 0;
        scoreTxt.setText(String.valueOf(Configs.score));


        pb = (ProgressBar)findViewById(R.id.gbProgressBar);
        progress = 0;
        pb.setProgress((int) progress);
        startGameTimer();

        Random r = new Random();
        randomCircle = r.nextInt(Configs.circlesArray.length);

        tapsCount = -1;

        getRandomWord();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);


        getSupportActionBar().hide();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String [] wordsArr = getResources().getStringArray(R.array.words);
        wordsArray = new ArrayList<String>(Arrays.asList(wordsArr));

        sTitleTxt = (TextView)findViewById(R.id.gbScoreTxt);
        scoreTxt = (TextView)findViewById(R.id.gbPointsTxt);
        scoreTxt.setTypeface(Configs.juneGull);

        letter1 = (TextView)findViewById(R.id.letter1);
        letter1.setTypeface(Configs.juneGull);
        letter2 = (TextView)findViewById(R.id.letter2);
        letter2.setTypeface(Configs.juneGull);
        letter3 = (TextView)findViewById(R.id.letter3);
        letter3.setTypeface(Configs.juneGull);
        letter4 = (TextView)findViewById(R.id.letter4);
        letter4.setTypeface(Configs.juneGull);
        letter5 = (TextView)findViewById(R.id.letter5);
        letter5.setTypeface(Configs.juneGull);

        letterButt1 = (Button)findViewById(R.id.letterButt1);
        letterButt1.setTypeface(Configs.juneGull);
        letterButt1.setOnClickListener(this);
        letterButt2 = (Button)findViewById(R.id.letterButt2);
        letterButt2.setTypeface(Configs.juneGull);
        letterButt2.setOnClickListener(this);
        letterButt3 = (Button)findViewById(R.id.letterButt3);
        letterButt3.setTypeface(Configs.juneGull);
        letterButt3.setOnClickListener(this);
        letterButt4 = (Button)findViewById(R.id.letterButt4);
        letterButt4.setTypeface(Configs.juneGull);
        letterButt4.setOnClickListener(this);
        letterButt5 = (Button)findViewById(R.id.letterButt5);
        letterButt5.setTypeface(Configs.juneGull);
        letterButt5.setOnClickListener(this);


        letterButtons = new Button[5];
        letterButtons[0] = letterButt1;
        letterButtons[1] = letterButt2;
        letterButtons[2] = letterButt3;
        letterButtons[3] = letterButt4;
        letterButtons[4] = letterButt5;



        letterTxts = new TextView[5];
        letterTxts[0] = letter1;
        letterTxts[1] = letter2;
        letterTxts[2] = letter3;
        letterTxts[3] = letter4;
        letterTxts[4] = letter5;


        Button resetButt = (Button)findViewById(R.id.gbResetButt);
        resetButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              resetWord();

              playSound("resetWord.mp3");
         }});

        Button backButt = (Button)findViewById(R.id.gbBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              gameTimer.cancel();
              finish();
        }});


        AdView mAdView = (AdView) findViewById(R.id.admobBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }


    void resetLetterButtons() {

        for (int i = 0; i<5; i++) {
            letterButtons[i].setEnabled(true);
            letterButtons[i].setBackgroundResource(Configs.circlesArray[randomCircle]);
            letterButtons[i].setTextColor(Color.parseColor("#ffffff"));
        }


        resetLettersTxt();
    }

    void resetLettersTxt() {
        for (int i = 0; i<5; i++) {
            letterTxts[i].setText("");
            letterTxts[i].setBackgroundResource(R.drawable.circle_corner_white);
        }
    }

    void getRandomWord() {

        Random r = new Random();
        randomCircle = r.nextInt(Configs.circlesArray.length);

        String randomWord = wordsArray.get(new Random().nextInt(wordsArray.size()));
        wordStr = randomWord;
        Log.i("log-", "RANDOM WORD: " + wordStr);

        Configs.stringsArray = new ArrayList<String>();

        if (wordStr.contains(".")) {
            String[] one = wordStr.split(Pattern.quote("."));
            for (String word : one) { Configs.stringsArray.add(word); }
            Log.i("log-", "\n\nWORDS ARRAY: " + Configs.stringsArray);

        } else {
            Log.i("log-", "SINGLE WORD: " + wordStr);
            Configs.stringsArray.add(wordStr);
        }

          charArray = new ArrayList<String>();
        String[] chArr = wordStr.split("");
        for(int i=0; i<6; i++) {
            String c = chArr[i];
            charArray.add(c);
        }
        charArray.remove(0);
        Log.i("log-", "CHARS ARRAY: " + charArray);

          getRandomChar();
    }

      void getRandomChar() {

             Random r = new Random();
        int randomCombination = r.nextInt(3);

        switch (randomCombination) {
            case 0:
                letterButtons[1].setText(charArray.get(0));
                letterButtons[0].setText(charArray.get(1));
                letterButtons[4].setText(charArray.get(2));
                letterButtons[2].setText(charArray.get(3));
                letterButtons[3].setText(charArray.get(4));
            break;

            case 1:
                letterButtons[3].setText(charArray.get(0));
                letterButtons[0].setText(charArray.get(1));
                letterButtons[4].setText(charArray.get(2));
                letterButtons[1].setText(charArray.get(3));
                letterButtons[2].setText(charArray.get(4));
            break;

            case 2:
                letterButtons[4].setText(charArray.get(0));
                letterButtons[1].setText(charArray.get(1));
                letterButtons[0].setText(charArray.get(2));
                letterButtons[3].setText(charArray.get(3));
                letterButtons[2].setText(charArray.get(4));
            break;
        }


       resetWord();

    }


    void resetWord() {
        // Reset tap Counts
        tapsCount = -1;

        // reset wordByCharacters
        wordByCharacters = "";

        // Reset letter Buttons
        resetLetterButtons();

        // Reset top Letters
        resetLettersTxt();
    }

    void startGameTimer() {
        float delay = 10*Configs.roundTime;

        gameTimer =  new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress = progress + 10/Configs.roundTime;
                        pb.setProgress((int) progress);
                        // Log.i("log-", "PROGRESS: " + progress);

                        // TIME ENDED, GAME OVER!
                        if (progress >= 100) {
                            gameTimer.cancel();

                            gameOver();
                        }
            }});}
        }, (int)delay, (int)delay);

    }


      void updateTimer() {
        gameTimer.cancel();
        pb.setProgress((int) progress);
        startGameTimer();
    }


    @Override
    public void onClick(View v) {
        Button lettButt = (Button)findViewById(v.getId());

        tapsCount = tapsCount+1;

            letterTxts[tapsCount].setText(lettButt.getText().toString() );
        letterTxts[tapsCount].setBackgroundResource(Configs.circlesArray[randomCircle]);


        lettButt.setBackgroundResource(R.drawable.circle_corner_white);
        lettButt.setTextColor(Color.parseColor("#999999"));
        lettButt.setEnabled(false);


        wordByCharacters = wordByCharacters + letterTxts[tapsCount].getText().toString();
        Log.i("log-", "WORD BY CHARS: " + wordByCharacters);

        if (tapsCount == 4) { checkResult(); }


         playSound("buttTapped.mp3");

    }

    void checkResult() {

        firstWord = Configs.stringsArray.get(0);

        if (Configs.stringsArray.size() == 2) {
            secondWord = Configs.stringsArray.get(1);
        }
        if (Configs.stringsArray.size() == 3) {
            secondWord = Configs.stringsArray.get(1);
            thirdWord = Configs.stringsArray.get(2);
        }

        if (wordByCharacters.matches(firstWord) ||
                wordByCharacters.matches(secondWord) ||
                wordByCharacters.matches(thirdWord) ) {

            playSound("rightWord.mp3");


            progress = progress - Configs.bonusProgress;
            updateTimer();

            Configs.score = Configs.score + 10;
            scoreTxt.setText(String.valueOf(Configs.score));


            getRandomWord();


        } else {
            wordByCharacters = "";
            getRandomChar();


            playSound("resetWord.mp3");
        }
    }

    void gameOver() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GameBoard.this);
        Configs.bestScore = prefs.getInt("bestScore", Configs.bestScore);


        if (Configs.bestScore <= Configs.score) {
            Configs.bestScore = Configs.score;
            prefs.edit().putInt("bestScore", Configs.bestScore).apply();
        }

         playSound("gameOver.mp3");

        startActivity(new Intent(GameBoard.this, GameOver.class));
    }


    void playSound(String soundName) {
        try {

            MediaPlayer mp = new MediaPlayer();

            AssetFileDescriptor afd = getAssets().openFd("sounds/" + soundName);
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

            mp.prepare();
            mp.setVolume(1f, 1f);
            mp.setLooping(false);
            mp.start();

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
            }});

        } catch (Exception e) { e.printStackTrace(); }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        gameTimer.cancel();
    }


}// @end
