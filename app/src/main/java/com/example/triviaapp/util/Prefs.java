package com.example.triviaapp.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences preferences;
    private static final String MESSAGE_ID = "messages_prevs";

    public Prefs(Activity activity) {
        this.preferences = activity.getSharedPreferences(MESSAGE_ID, activity.MODE_PRIVATE);
    }
    public void saveHighestScore(int score){
        int lastSavedScore = preferences.getInt("high_score", 0);

        if(score > lastSavedScore) {
           preferences.edit().putInt("high_score", score).apply();
        }
    }
    public int loadHighScore(){
       return preferences.getInt("high_score", 0);
    }


    public void setState(int questionIndex, int correctlyAns){
       preferences.edit().putInt("questionIndex_state", questionIndex).apply();
       preferences.edit().putInt("correctlyAns_state", correctlyAns).apply();
    }
    public int getQuestionIndex(){
        return preferences.getInt("questionIndex_state", 0);
    }
    public int getCorrectlyAns(){
        return preferences.getInt("correctlyAns_state", 0);
    }

    public void setTimer(long timer){
        preferences.edit().putLong("timeLeftMill_state", timer).apply();
    }
    public long getTimeLeftMil(){
        return preferences.getLong("timeLeftMill_state", 600000);
    }
    public void setIsPausedBool(boolean paused){
        preferences.edit().putBoolean("isPaused_state", paused).apply();
    }
    public boolean getIsPausedBool(){
        return preferences.getBoolean("isPaused_state", false);
    }

    
}
