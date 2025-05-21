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
import com.example.androidventure.utils.SoundManager;

public class DraggableCodeBlock extends AppCompatTextView {

    private float dX, dY;
    private float lastTouchX, lastTouchY;
    private boolean isDragging = false;
    private String blockName;
    private SoundManager soundManager;

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
        
        setTextColor(Color.BLACK);
        setTextSize(16);
        setAllCaps(false);
        setPadding(24, 12, 24, 12);
        setGravity(android.view.Gravity.CENTER);

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(16);
        shape.setColor(ContextCompat.getColor(getContext(), R.color.codeBlock));
        shape.setStroke(4, ContextCompat.getColor(getContext(), R.color.codeBlockBorder));
        setBackground(shape);

        setElevation(8);

        soundManager = SoundManager.getInstance(getContext());
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

                soundManager.playSound(SoundManager.SOUND_DRAG);

                lastTouchX = event.getRawX();
                lastTouchY = event.getRawY();

                dX = getX() - lastTouchX;
                dY = getY() - lastTouchY;

                bringToFront();

                animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).start();

                return true;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    float newX = event.getRawX() + dX;
                    float newY = event.getRawY() + dY;

                    newX = Math.max(0, Math.min(newX, parent.getWidth() - getWidth()));
                    newY = Math.max(0, Math.min(newY, parent.getHeight() - getHeight()));

                    setX(newX);
                    setY(newY);
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;

                soundManager.playSound(SoundManager.SOUND_DROP);

                animate().scaleX(1f).scaleY(1f).setDuration(100).start();

                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setBlockName(String name) {
        this.blockName = name;
        setText(name);
    }


    public String getBlockName() {
        return blockName;
    }
}