/**
 * Level 3: Intent Gate
 * Educational level focused on Android Intent system.
 * Players learn to properly create and use Intents for activity navigation.
 */

package com.example.androidventure.levels;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidventure.R;
import com.example.androidventure.utils.GameStateManager;
import com.example.androidventure.utils.SoundManager;
import com.example.androidventure.widgets.DraggableCodeBlock;

public class IntentGateLevel extends AppCompatActivity {

    private static final int LEVEL_ID = 3;
    private static final int MAX_SCORE = 3;

    private ConstraintLayout gameArea;
    private TextView levelTitle;
    private TextView instructionText;
    private Button checkButton;
    private Button hintButton;
    private GameStateManager gameStateManager;

    private DraggableCodeBlock intentBlock;
    private DraggableCodeBlock contextBlock;
    private DraggableCodeBlock activityBlock;

    private View targetIntent;
    private View targetContext;
    private View targetActivity;

    private int attempts = 0;
    private int score = MAX_SCORE;

    private View confettiOverlay;
    private View hintOverlay;
    private TextView hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_gate_level);

        gameStateManager = new GameStateManager(this);

        SoundManager.getInstance(this);

        initializeUI();

        setupDraggableBlocks();

        setupButtons();

        if (!gameStateManager.isTutorialCompleted()) {
            showTutorial();
        }
    }

    /*
     * Initializes the game's UI components and sets up the initial state.
     * Called when the activity is first created.
     */
    private void initializeUI() {
        gameArea = findViewById(R.id.game_area);
        levelTitle = findViewById(R.id.level_title);
        instructionText = findViewById(R.id.instruction_text);
        checkButton = findViewById(R.id.check_button);
        hintButton = findViewById(R.id.hint_button);
        confettiOverlay = findViewById(R.id.confetti_overlay);
        hintOverlay = findViewById(R.id.hint_overlay);
        hintText = findViewById(R.id.hint_text);

        levelTitle.setText(R.string.level_3_name);

        instructionText.setText("Arrange the Intent code blocks in the correct order to launch a new activity.");
    }

    /*
     * Sets up draggable code blocks with their initial positions and properties.
     * Configures block names and positions them in the game area.
     */
    private void setupDraggableBlocks() {
        intentBlock = findViewById(R.id.block_intent);
        contextBlock = findViewById(R.id.block_context);
        activityBlock = findViewById(R.id.block_activity);

        intentBlock.setBlockName("Intent intent = new Intent(");
        contextBlock.setBlockName("this, SecondActivity.class");
        activityBlock.setBlockName("startActivity(intent)");

        resetBlockPositions();
    }

    private void resetBlockPositions() {
       
        intentBlock.setX(100);
        intentBlock.setY(gameArea.getHeight() - 300);

        contextBlock.setX(300);
        contextBlock.setY(gameArea.getHeight() - 200);

        activityBlock.setX(200);
        activityBlock.setY(gameArea.getHeight() - 100);
    }

    private void setupButtons() {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHint();
                if (score > 1) {
                    score--;
                }
            }
        });

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showTutorial() {
       
        Toast.makeText(this, "Drag the code blocks to arrange them in the correct order", Toast.LENGTH_LONG).show();

       
        ObjectAnimator animation = ObjectAnimator.ofFloat(intentBlock, "translationY",
                intentBlock.getY(), intentBlock.getY() - 200, intentBlock.getY());
        animation.setDuration(2000);
        animation.start();

        gameStateManager.setTutorialCompleted(true);
    }

    private void showHint() {
     
        hintText.setText("Remember: First create the Intent, then specify the context and activity, finally start the activity.");
        hintOverlay.setVisibility(View.VISIBLE);
        hintOverlay.setAlpha(0f);
        hintOverlay.animate().alpha(1f).setDuration(400).start();
       
        new Handler().postDelayed(() -> hintOverlay.animate().alpha(0f).setDuration(400).withEndAction(() -> hintOverlay.setVisibility(View.GONE)).start(), 3000);
       
        ObjectAnimator highlight = ObjectAnimator.ofFloat(intentBlock, "alpha", 1f, 0.5f, 1f);
        highlight.setDuration(1000);
        highlight.start();
    }
    /*
     * Validates the player's answer and updates the game state accordingly.
     * Checks if blocks are in correct positions and order.
     * Updates score and provides feedback to the player.
     */
    private void checkAnswer() {
        attempts++;
        boolean correct = isBlockNearTarget(intentBlock, findViewById(R.id.target_intent)) &&
                isBlockNearTarget(contextBlock, findViewById(R.id.target_context)) &&
                isBlockNearTarget(activityBlock, findViewById(R.id.target_activity));

        if (correct) {
            boolean correctOrder = intentBlock.getY() < contextBlock.getY() && 
                                 contextBlock.getY() < activityBlock.getY();

            if (correctOrder) {
                SoundManager.getInstance(this).playSound(SoundManager.SOUND_SUCCESS);
                animateBlockSuccess(intentBlock);
                animateBlockSuccess(contextBlock);
                animateBlockSuccess(activityBlock);
                completeLevelSuccess();
            } else {
                if (attempts > 1 && score > 1) {
                    score--;
                }
                SoundManager.getInstance(this).playSound(SoundManager.SOUND_FAILURE);
                Toast.makeText(this, "The order is incorrect. Remember: First create the Intent, then specify the context and activity, finally start the activity.", Toast.LENGTH_LONG).show();
            }
        } else {
            if (attempts > 1 && score > 1) {
                score--;
            }
            SoundManager.getInstance(this).playSound(SoundManager.SOUND_FAILURE);
            Toast.makeText(this, "Not quite right. Make sure all blocks are in their correct positions!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isBlockNearTarget(View block, View target) {
        float blockCenterX = block.getX() + block.getWidth() / 2;
        float blockCenterY = block.getY() + block.getHeight() / 2;

        float targetCenterX = target.getX() + target.getWidth() / 2;
        float targetCenterY = target.getY() + target.getHeight() / 2;

        float distance = (float) Math.sqrt(
                Math.pow(blockCenterX - targetCenterX, 2) +
                        Math.pow(blockCenterY - targetCenterY, 2));

        return distance < 150;
    }

    private void animateBlockSuccess(View block) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(block, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(block, "scaleY", 1f, 1.2f, 1f);
        ObjectAnimator glow = ObjectAnimator.ofArgb(block, "backgroundColor", Color.parseColor("#4CAF50"), Color.parseColor("#A5D6A7"), Color.parseColor("#4CAF50"));
        set.playTogether(scaleX, scaleY, glow);
        set.setDuration(700);
        set.start();
    }

    private void completeLevelSuccess() {

        Toast.makeText(this, R.string.level_complete, Toast.LENGTH_LONG).show();

        gameStateManager.setLevelCompleted(LEVEL_ID, true);
        gameStateManager.setLevelScore(LEVEL_ID, score);

        
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(gameArea, "alpha",
                1f, 0.5f);
        fadeOut.setDuration(1000);
        fadeOut.start();

       
        gameArea.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
} 