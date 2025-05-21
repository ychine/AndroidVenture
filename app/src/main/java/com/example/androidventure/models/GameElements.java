package com.example.androidventure.models;

import android.graphics.drawable.Drawable;

/**
 * Represents UI elements used in the game levels
 */
public class GameElements {

    public enum ElementType {
        CODE_BLOCK,
        TARGET_AREA,
        OBSTACLE,
        CONNECTOR,
        DECORATION,
        HINT
    }

    private int id;
    private ElementType type;
    private String displayText;
    private Drawable icon;
    private boolean isInteractive;
    private int resourceId;
    private String code;

    public GameElements(int id, ElementType type, String displayText, Drawable icon,
                        boolean isInteractive, int resourceId, String code) {
        this.id = id;
        this.type = type;
        this.displayText = displayText;
        this.icon = icon;
        this.isInteractive = isInteractive;
        this.resourceId = resourceId;
        this.code = code;
    }

    public GameElements(int id, ElementType type, String displayText, int resourceId) {
        this(id, type, displayText, null, true, resourceId, null);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isInteractive() {
        return isInteractive;
    }

    public void setInteractive(boolean interactive) {
        isInteractive = interactive;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}