/**
 * Model class representing game elements used across different levels.
 * Defines the structure and properties of interactive elements in the game.
 *
 * Element types include:
 * - Code blocks for drag-and-drop interactions
 * - Target areas for block placement
 * - Obstacles and decorations
 * - Connectors for linking elements
 * - Hints for player assistance
 */

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

}