package com.example.androidventure.models;

/**
 * Model class that represents a game level
 */
public class Level {
    private int id;
    private String name;
    private String description;
    private int iconResourceId;
    private boolean unlocked;
    private boolean completed;
    private int score;
    private String className;

    /**
     * Constructor for creating a new Level
     *
     * @param id Level identifier
     * @param name Level name
     * @param description Brief description of the level
     * @param iconResourceId Resource ID for the level icon
     * @param unlocked Whether the level is available to play
     * @param completed Whether the level has been completed
     * @param score Player's score in the level (0-3 stars)
     * @param className Full class name of the Activity to launch for this level
     */
    public Level(int id, String name, String description, int iconResourceId,
                 boolean unlocked, boolean completed, int score, String className) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconResourceId = iconResourceId;
        this.unlocked = unlocked;
        this.completed = completed;
        this.score = score;
        this.className = className;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}