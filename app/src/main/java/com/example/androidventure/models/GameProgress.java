package com.example.androidventure.models;

/**
 * Tracks overall player progress in the game
 */
public class GameProgress {
    private int totalStars;
    private int levelsCompleted;
    private int highestLevelUnlocked;
    private boolean tutorialCompleted;
    private long totalPlayTime; // in milliseconds

    public GameProgress() {
        this.totalStars = 0;
        this.levelsCompleted = 0;
        this.highestLevelUnlocked = 1; // First level always unlocked
        this.tutorialCompleted = false;
        this.totalPlayTime = 0;
    }

    /**
     * Creates GameProgress with specified values
     */
    public GameProgress(int totalStars, int levelsCompleted, int highestLevelUnlocked,
                        boolean tutorialCompleted, long totalPlayTime) {
        this.totalStars = totalStars;
        this.levelsCompleted = levelsCompleted;
        this.highestLevelUnlocked = highestLevelUnlocked;
        this.tutorialCompleted = tutorialCompleted;
        this.totalPlayTime = totalPlayTime;
    }

    // Getters and setters
    public int getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(int totalStars) {
        this.totalStars = totalStars;
    }

    public void addStars(int stars) {
        this.totalStars += stars;
    }

    public int getLevelsCompleted() {
        return levelsCompleted;
    }

    public void setLevelsCompleted(int levelsCompleted) {
        this.levelsCompleted = levelsCompleted;
    }

    public void incrementLevelsCompleted() {
        this.levelsCompleted++;
    }

    public int getHighestLevelUnlocked() {
        return highestLevelUnlocked;
    }

    public void setHighestLevelUnlocked(int highestLevelUnlocked) {
        this.highestLevelUnlocked = highestLevelUnlocked;
    }

    public boolean isTutorialCompleted() {
        return tutorialCompleted;
    }

    public void setTutorialCompleted(boolean tutorialCompleted) {
        this.tutorialCompleted = tutorialCompleted;
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public void addPlayTime(long additionalPlayTime) {
        this.totalPlayTime += additionalPlayTime;
    }

    /**
     * Calculate the overall completion percentage of the game
     * @param totalLevels Total number of levels in the game
     * @param maxStarsPerLevel Maximum stars attainable per level
     * @return Percentage of game completed (0-100)
     */
    public int calculateCompletionPercentage(int totalLevels, int maxStarsPerLevel) {
        int maxPossibleStars = totalLevels * maxStarsPerLevel;
        if (maxPossibleStars == 0) return 0;

        return (int) ((totalStars * 100.0) / maxPossibleStars);
    }
}