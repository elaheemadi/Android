package com.domain.fives;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class Configs extends Application {

    public static float roundTime = 15;

    public static int bonusProgress = 10;
    public static final String EMAIL ="andiajamadi@gmail.com";
    public static final String PASSWORD ="10021354";

    public static Typeface juneGull;


    public static  int[] circlesArray = new int[] {
            R.drawable.circle_corner_orange,
            R.drawable.circle_corner_blue,
            R.drawable.circle_corner_dark_purple,
            R.drawable.circle_corner_green,
            R.drawable.circle_corner_purple,
    };

    @Override
    public void onCreate() {
        super.onCreate();


        juneGull  = Typeface.createFromAsset(getAssets(),"font/junegull.ttf");


    }


    public static int bestScore;
    public static int score;
    public static List<String> stringsArray;



}
