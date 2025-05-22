/**
 * Manages the game state and persistence for AndroidVenture.
 * Handles saving and loading of level progress, scores, and settings.
 * Implements a singleton pattern for global access.
 */

package com.example.androidventure.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

import com.example.androidventure.R;

public class GameStateManager {
    private static final String PREF_NAME = "GameState";
    private static final String KEY_LEVEL_COMPLETED = "level_completed_";
    private static final String KEY_LEVEL_SCORE = "level_score_";
    private static final String KEY_TOTAL_STARS = "total_stars";
    private static final String KEY_HIGH_SCORE = "high_score";
    private static final String KEY_TUTORIAL_COMPLETED = "tutorial_completed";

    private SharedPreferences preferences;
    private Context context;
    private SoundPool soundPool;
    private int buttonClickSound;
    private int levelCompleteSound;
    private Vibrator vibrator;

    public GameStateManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        initializeAudio();
        initializeVibration();
    }

    private void initializeAudio() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        buttonClickSound = soundPool.load(context, R.raw.click, 1);
        levelCompleteSound = soundPool.load(context, R.raw.level_complete, 1);
    }

    private void initializeVibration() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = vibratorManager.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }


    public boolean isLevelCompleted(int levelId) {
        return preferences.getBoolean(KEY_LEVEL_COMPLETED + levelId, false);
    }

    public void setLevelCompleted(int levelId, boolean completed) {
        preferences.edit().putBoolean(KEY_LEVEL_COMPLETED + levelId, completed).apply();
    }

    public int getLevelScore(int levelId) {
        return preferences.getInt(KEY_LEVEL_SCORE + levelId, 0);
    }

    public void setLevelScore(int levelId, int score) {
        preferences.edit().putInt(KEY_LEVEL_SCORE + levelId, score).apply();
        updateHighScore(score);
    }

    public int getTotalStars() {
        return preferences.getInt(KEY_TOTAL_STARS, 0);
    }

    public void addStars(int stars) {
        int currentStars = getTotalStars();
        preferences.edit().putInt(KEY_TOTAL_STARS, currentStars + stars).apply();
    }

    public int getHighScore() {
        return preferences.getInt(KEY_HIGH_SCORE, 0);
    }

    private void updateHighScore(int newScore) {
        int currentHighScore = getHighScore();
        if (newScore > currentHighScore) {
            preferences.edit().putInt(KEY_HIGH_SCORE, newScore).apply();
        }
    }

    public boolean isTutorialCompleted() {
        return preferences.getBoolean(KEY_TUTORIAL_COMPLETED, false);
    }

    public void setTutorialCompleted(boolean completed) {
        preferences.edit().putBoolean(KEY_TUTORIAL_COMPLETED, completed).apply();
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}