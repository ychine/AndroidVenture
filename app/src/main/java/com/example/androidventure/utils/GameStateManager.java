package com.example.androidventure.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

import com.example.androidventure.R;

/**
 * Manages game state persistence using SharedPreferences
 */
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

    /**
     * Constructor for GameStateManager
     *
     * @param context Application context
     */
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

    public void playButtonClick() {
        if (preferences.getBoolean("sound_enabled", true)) {
            soundPool.play(buttonClickSound, 1.0f, 1.0f, 1, 0, 1.0f);
        }
        if (preferences.getBoolean("vibration_enabled", true) && vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(50);
            }
        }
    }

    public void playLevelComplete() {
        if (preferences.getBoolean("sound_enabled", true)) {
            soundPool.play(levelCompleteSound, 1.0f, 1.0f, 1, 0, 1.0f);
        }
        if (preferences.getBoolean("vibration_enabled", true) && vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(200);
            }
        }
    }

    /**
     * Check if a level is completed
     *
     * @param levelId Level identifier
     * @return true if the level is completed, false otherwise
     */
    public boolean isLevelCompleted(int levelId) {
        return preferences.getBoolean(KEY_LEVEL_COMPLETED + levelId, false);
    }

    /**
     * Set a level as completed
     *
     * @param levelId Level identifier
     * @param completed Completion status
     */
    public void setLevelCompleted(int levelId, boolean completed) {
        preferences.edit().putBoolean(KEY_LEVEL_COMPLETED + levelId, completed).apply();
    }

    /**
     * Get the score for a level
     *
     * @param levelId Level identifier
     * @return Score (0-3 stars)
     */
    public int getLevelScore(int levelId) {
        return preferences.getInt(KEY_LEVEL_SCORE + levelId, 0);
    }

    /**
     * Set the score for a level
     *
     * @param levelId Level identifier
     * @param score Score (0-3 stars)
     */
    public void setLevelScore(int levelId, int score) {
        preferences.edit().putInt(KEY_LEVEL_SCORE + levelId, score).apply();
        updateHighScore(score);
    }

    /**
     * Get the total stars across all levels
     *
     * @return Total stars
     */
    public int getTotalStars() {
        return preferences.getInt(KEY_TOTAL_STARS, 0);
    }

    /**
     * Add stars to the total stars
     *
     * @param stars Stars to add
     */
    public void addStars(int stars) {
        int currentStars = getTotalStars();
        preferences.edit().putInt(KEY_TOTAL_STARS, currentStars + stars).apply();
    }

    /**
     * Get the high score
     *
     * @return High score
     */
    public int getHighScore() {
        return preferences.getInt(KEY_HIGH_SCORE, 0);
    }

    private void updateHighScore(int newScore) {
        int currentHighScore = getHighScore();
        if (newScore > currentHighScore) {
            preferences.edit().putInt(KEY_HIGH_SCORE, newScore).apply();
        }
    }

    /**
     * Check if the tutorial is completed
     * @return true if completed, false otherwise
     */
    public boolean isTutorialCompleted() {
        return preferences.getBoolean(KEY_TUTORIAL_COMPLETED, false);
    }

    /**
     * Set the tutorial as completed
     * @param completed Completion status
     */
    public void setTutorialCompleted(boolean completed) {
        preferences.edit().putBoolean(KEY_TUTORIAL_COMPLETED, completed).apply();
    }

    /**
     * Release resources
     */
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}