package com.example.androidventure.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.example.androidventure.R;

/**
 * Custom view for draggable code blocks in game levels
 */
public class DraggableCodeBlock extends AppCompatTextView {

    private float dX, dY;
    private float lastTouchX, lastTouchY;
    private boolean isDragging = false;
    private String blockName;

    public DraggableCodeBlock(Context context) {
        super(context);
        init();
    }

    public DraggableCodeBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DraggableCodeBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Set text appearance
        setTextColor(Color.BLACK);
        setTextSize(16);
        setAllCaps(false);
        setPadding(24, 12, 24, 12);
        setGravity(android.view.Gravity.CENTER);

        // Create rounded rectangle background
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(16);
        shape.setColor(ContextCompat.getColor(getContext(), R.color.codeBlock));
        shape.setStroke(4, ContextCompat.getColor(getContext(), R.color.codeBlockBorder));
        setBackground(shape);

        // Add drop shadow
        setElevation(8);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup parent = (ViewGroup) getParent();
        if (parent == null) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDragging = true;

                // Record initial touch point
                lastTouchX = event.getRawX();
                lastTouchY = event.getRawY();

                // Record initial view position offset
                dX = getX() - lastTouchX;
                dY = getY() - lastTouchY;

                // Bring view to front
                bringToFront();

                // Increase size slightly to give visual feedback
                animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).start();

                return true;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    // Calculate new position
                    float newX = event.getRawX() + dX;
                    float newY = event.getRawY() + dY;

                    // Apply constraints to keep within parent
                    newX = Math.max(0, Math.min(newX, parent.getWidth() - getWidth()));
                    newY = Math.max(0, Math.min(newY, parent.getHeight() - getHeight()));

                    // Move the view
                    setX(newX);
                    setY(newY);
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;

                // Return to normal size
                animate().scaleX(1f).scaleY(1f).setDuration(100).start();

                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Set the name of this code block
     *
     * @param name The method or code block name
     */
    public void setBlockName(String name) {
        this.blockName = name;
        setText(name);
    }

    /**
     * Get the name of this code block
     *
     * @return The method or code block name
     */
    public String getBlockName() {
        return blockName;
    }
}