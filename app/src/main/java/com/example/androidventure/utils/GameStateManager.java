package com.example.androidventure.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages game state persistence using SharedPreferences
 */
public class GameStateManager {
    private static final String PREF_NAME = "AndroidVenturePrefs";
    private static final String KEY_LEVEL_COMPLETED_PREFIX = "level_completed_";
    private static final String KEY_LEVEL_SCORE_PREFIX = "level_score_";
    private static final String KEY_CURRENT_LEVEL = "current_level";
    private static final String KEY_TOTAL_SCORE = "total_score";
    private static final String KEY_TUTORIAL_COMPLETED = "tutorial_completed";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * Constructor for GameStateManager
     *
     * @param context Application context
     */
    public GameStateManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * Check if a level is completed
     *
     * @param levelId Level identifier
     * @return true if the level is completed, false otherwise
     */
    public boolean isLevelCompleted(int levelId) {
        return preferences.getBoolean(KEY_LEVEL_COMPLETED_PREFIX + levelId, false);
    }

    /**
     * Set a level as completed
     *
     * @param levelId Level identifier
     * @param completed Completion status
     */
    public void setLevelCompleted(int levelId, boolean completed) {
        editor.putBoolean(KEY_LEVEL_COMPLETED_PREFIX + levelId, completed);
        editor.apply();
    }

    /**
     * Get the score for a level
     *
     * @param levelId Level identifier
     * @return Score (0-3 stars)
     */
    public int getLevelScore(int levelId) {
        return preferences.getInt(KEY_LEVEL_SCORE_PREFIX + levelId, 0);
    }

    /**
     * Set the score for a level
     *
     * @param levelId Level identifier
     * @param score Score (0-3 stars)
     */
    public void setLevelScore(int levelId, int score) {
        editor.putInt(KEY_LEVEL_SCORE_PREFIX + levelId, score);
        editor.apply();

        // Update total score
        int totalScore = getTotalScore();
        totalScore += score;
        setTotalScore(totalScore);

        // If this level is completed with at least 1 star, unlock the next level
        if (score > 0) {
            setLevelCompleted(levelId, true);
        }
    }

    /**
     * Get the current level
     *
     * @return Current level identifier
     */
    public int getCurrentLevel() {
        return preferences.getInt(KEY_CURRENT_LEVEL, 1);
    }

    /**
     * Set the current level
     *
     * @param levelId Level identifier
     */
    public void setCurrentLevel(int levelId) {
        editor.putInt(KEY_CURRENT_LEVEL, levelId);
        editor.apply();
    }

    /**
     * Get the total score across all levels
     *
     * @return Total score
     */
    public int getTotalScore() {
        return preferences.getInt(KEY_TOTAL_SCORE, 0);
    }

    /**
     * Set the total score
     *
     * @param score Total score
     */
    public void setTotalScore(int score) {
        editor.putInt(KEY_TOTAL_SCORE, score);
        editor.apply();
    }

    /**
     * Check if the tutorial has been completed
     *
     * @return true if the tutorial is completed, false otherwise
     */
    public boolean isTutorialCompleted() {
        return preferences.getBoolean(KEY_TUTORIAL_COMPLETED, false);
    }

    /**
     * Set the tutorial as completed
     *
     * @param completed Completion status
     */
    public void setTutorialCompleted(boolean completed) {
        editor.putBoolean(KEY_TUTORIAL_COMPLETED, completed);
        editor.apply();
    }

    /**
     * Reset all game progress
     */
    public void resetProgress() {
        editor.clear();
        editor.apply();
    }
}