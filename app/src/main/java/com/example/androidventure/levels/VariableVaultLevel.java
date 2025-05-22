/**
 * Level 4: Variable Vault
 * Interactive level teaching Android variable types and data structures through drag-and-drop gameplay.
 * Players learn to identify and match different variable types:
 * - String: Text data (e.g., "John")
 * - int: Integer numbers (e.g., 25)
 * - boolean: True/false values (e.g., true)
 * - float: Decimal numbers (e.g., 19.99f)
 */

package com.example.androidventure.levels;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

public class VariableVaultLevel extends AppCompatActivity {

    private static final int LEVEL_ID = 4;
    private static final int MAX_SCORE = 3;

    private ConstraintLayout gameArea;
    private TextView levelTitle;
    private TextView instructionText;
    private Button checkButton;
    private Button hintButton;
    private GameStateManager gameStateManager;

    // Code blocks
    private DraggableCodeBlock stringBlock;
    private DraggableCodeBlock intBlock;
    private DraggableCodeBlock booleanBlock;
    private DraggableCodeBlock floatBlock;

    // Drop targets
    private View targetString;
    private View targetInt;
    private View targetBoolean;
    private View targetFloat;

    private int attempts = 0;
    private int score = MAX_SCORE;

    private View confettiOverlay;
    private View hintOverlay;
    private TextView hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_vault_level);

        // Initialize game state manager
        gameStateManager = new GameStateManager(this);

        // Initialize SoundManager
        SoundManager.getInstance(this);

        // Initialize UI components
        initializeUI();

        // Setup draggable blocks
        setupDraggableBlocks();

        // Setup buttons
        setupButtons();

        // Show tutorial if this is first time
        if (!gameStateManager.isTutorialCompleted()) {
            showTutorial();
        }
    }

    private void initializeUI() {
        gameArea = findViewById(R.id.game_area);
        levelTitle = findViewById(R.id.level_title);
        instructionText = findViewById(R.id.instruction_text);
        checkButton = findViewById(R.id.check_button);
        hintButton = findViewById(R.id.hint_button);
        confettiOverlay = findViewById(R.id.confetti_overlay);
        hintOverlay = findViewById(R.id.hint_overlay);
        hintText = findViewById(R.id.hint_text);

        // Set level name
        levelTitle.setText(R.string.level_4_name);

        // Set instruction text
        instructionText.setText("Match the variables with their correct data types");
    }

    private void setupDraggableBlocks() {
        stringBlock = findViewById(R.id.block_string);
        intBlock = findViewById(R.id.block_int);
        booleanBlock = findViewById(R.id.block_boolean);
        floatBlock = findViewById(R.id.block_float);

        // Setup block data and listeners
        stringBlock.setBlockName("name = \"John\"");
        intBlock.setBlockName("age = 25");
        booleanBlock.setBlockName("isActive = true");
        floatBlock.setBlockName("price = 19.99f");

        // Reset positions to original places
        resetBlockPositions();
    }

    private void resetBlockPositions() {
        // Position blocks in a randomized order
        booleanBlock.setX(100);
        booleanBlock.setY(gameArea.getHeight() - 300);

        floatBlock.setX(300);
        floatBlock.setY(gameArea.getHeight() - 200);

        stringBlock.setX(200);
        stringBlock.setY(gameArea.getHeight() - 100);

        intBlock.setX(400);
        intBlock.setY(gameArea.getHeight() - 150);
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
                // Reduce potential score when hint is used
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
        // Simple tutorial that shows how to drag blocks
        Toast.makeText(this, "Drag the variable blocks to match them with their data types", Toast.LENGTH_LONG).show();

        // Animate a block to simulate dragging
        ObjectAnimator animation = ObjectAnimator.ofFloat(stringBlock, "translationY",
                stringBlock.getY(), stringBlock.getY() - 200, stringBlock.getY());
        animation.setDuration(2000);
        animation.start();

        // Mark tutorial as completed
        gameStateManager.setTutorialCompleted(true);
    }

    private void showHint() {
        // Show animated hint overlay
        hintText.setText("Remember: String for text, int for whole numbers, boolean for true/false, and float for decimal numbers.");
        hintOverlay.setVisibility(View.VISIBLE);
        hintOverlay.setAlpha(0f);
        hintOverlay.animate().alpha(1f).setDuration(400).start();
        // Hide after 3 seconds
        new Handler().postDelayed(() -> hintOverlay.animate().alpha(0f).setDuration(400).withEndAction(() -> hintOverlay.setVisibility(View.GONE)).start(), 3000);
        // Highlight string block briefly
        ObjectAnimator highlight = ObjectAnimator.ofFloat(stringBlock, "alpha", 1f, 0.5f, 1f);
        highlight.setDuration(1000);
        highlight.start();
    }

    private void checkAnswer() {
        attempts++;
        boolean correct = isBlockNearTarget(stringBlock, findViewById(R.id.target_string)) &&
                isBlockNearTarget(intBlock, findViewById(R.id.target_int)) &&
                isBlockNearTarget(booleanBlock, findViewById(R.id.target_boolean)) &&
                isBlockNearTarget(floatBlock, findViewById(R.id.target_float));

        if (correct) {
            // Play success sound
            SoundManager.getInstance(this).playSound(SoundManager.SOUND_SUCCESS);
            // Animate blocks with bounce and glow
            animateBlockSuccess(stringBlock);
            animateBlockSuccess(intBlock);
            animateBlockSuccess(booleanBlock);
            animateBlockSuccess(floatBlock);
            completeLevelSuccess();
        } else {
            // Reduce score for each failed attempt after the first
            if (attempts > 1 && score > 1) {
                score--;
            }
            // Play failure sound
            SoundManager.getInstance(this).playSound(SoundManager.SOUND_FAILURE);
            Toast.makeText(this, "Not quite right. Try again!", Toast.LENGTH_SHORT).show();
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
        // Display success message
        Toast.makeText(this, R.string.level_complete, Toast.LENGTH_LONG).show();

        // Save progress
        gameStateManager.setLevelCompleted(LEVEL_ID, true);
        gameStateManager.setLevelScore(LEVEL_ID, score);

        // Show success animation
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(gameArea, "alpha",
                1f, 0.5f);
        fadeOut.setDuration(1000);
        fadeOut.start();

        // Return to level select after a delay
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