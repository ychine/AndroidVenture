package com.example.androidventure.levels;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidventure.R;
import com.example.androidventure.utils.GameStateManager;
import com.example.androidventure.widgets.DraggableCodeBlock;

public class ActivityForestLevel extends AppCompatActivity {

    private static final int LEVEL_ID = 1;
    private static final int MAX_SCORE = 3;

    private ConstraintLayout gameArea;
    private TextView levelTitle;
    private TextView instructionText;
    private Button checkButton;
    private Button hintButton;
    private ImageView lifecycleMapView;
    private GameStateManager gameStateManager;

    // Lifecycle method blocks
    private DraggableCodeBlock onCreateBlock;
    private DraggableCodeBlock onStartBlock;
    private DraggableCodeBlock onResumeBlock;
    private DraggableCodeBlock onPauseBlock;
    private DraggableCodeBlock onStopBlock;
    private DraggableCodeBlock onDestroyBlock;

    // Drop targets
    private View target1;
    private View target2;
    private View target3;
    private View target4;
    private View target5;
    private View target6;

    private int attempts = 0;
    private int score = MAX_SCORE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forest_level);

        // Initialize game state manager
        gameStateManager = new GameStateManager(this);

        // Initialize UI components
        initializeUI();

        // Setup draggable blocks
        setupDraggableBlocks();

        // Setup drop targets
        setupDropTargets();

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
        lifecycleMapView = findViewById(R.id.lifecycle_map);

        // Set level name
        levelTitle.setText(R.string.level_1_name);

        // Set instruction text
        instructionText.setText("Drag the activity lifecycle methods into the correct order");
    }

    private void setupDraggableBlocks() {
        // Initialize draggable blocks
        onCreateBlock = findViewById(R.id.block_oncreate);
        onStartBlock = findViewById(R.id.block_onstart);
        onResumeBlock = findViewById(R.id.block_onresume);
        onPauseBlock = findViewById(R.id.block_onpause);
        onStopBlock = findViewById(R.id.block_onstop);
        onDestroyBlock = findViewById(R.id.block_ondestroy);

        // Setup block data and listeners
        onCreateBlock.setBlockName("onCreate()");
        onStartBlock.setBlockName("onStart()");
        onResumeBlock.setBlockName("onResume()");
        onPauseBlock.setBlockName("onPause()");
        onStopBlock.setBlockName("onStop()");
        onDestroyBlock.setBlockName("onDestroy()");

        // Reset positions to original places
        resetBlockPositions();
    }

    private void setupDropTargets() {
        target1 = findViewById(R.id.target_1);
        target2 = findViewById(R.id.target_2);
        target3 = findViewById(R.id.target_3);
        target4 = findViewById(R.id.target_4);
        target5 = findViewById(R.id.target_5);
        target6 = findViewById(R.id.target_6);
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

        // Setup back button
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void resetBlockPositions() {
        // Position blocks in a randomized order at the bottom area
        int baseX = 100;
        int baseY = gameArea.getHeight() - 200;

        if (baseY <= 0) {
            // If view hasn't been laid out yet, use a reasonable default
            baseY = 1200;
        }

        onCreateBlock.setX(baseX);
        onCreateBlock.setY(baseY);

        onStartBlock.setX(baseX + 250);
        onStartBlock.setY(baseY);

        onResumeBlock.setX(baseX + 500);
        onResumeBlock.setY(baseY);

        onPauseBlock.setX(baseX);
        onPauseBlock.setY(baseY + 150);

        onStopBlock.setX(baseX + 250);
        onStopBlock.setY(baseY + 150);

        onDestroyBlock.setX(baseX + 500);
        onDestroyBlock.setY(baseY + 150);
    }

    private void showTutorial() {
        // Simple tutorial that shows how to drag blocks
        Toast.makeText(this, R.string.tutorial_drag, Toast.LENGTH_LONG).show();

        // Animate a block to simulate dragging
        ObjectAnimator animation = ObjectAnimator.ofFloat(onCreateBlock, "translationY",
                onCreateBlock.getY(), onCreateBlock.getY() - 200, onCreateBlock.getY());
        animation.setDuration(2000);
        animation.start();

        // Mark tutorial as completed
        gameStateManager.setTutorialCompleted(true);
    }

    private void showHint() {
        // Show a toast with a hint about the correct order
        Toast.makeText(this, "The Android activity lifecycle follows a specific sequence from creation to destruction.",
                Toast.LENGTH_LONG).show();

        // Highlight onCreate block briefly
        ObjectAnimator highlight = ObjectAnimator.ofFloat(onCreateBlock, "alpha",
                1f, 0.5f, 1f);
        highlight.setDuration(1000);
        highlight.start();
    }

    private void checkAnswer() {
        attempts++;

        // Check if blocks are in correct positions
        boolean correct = isBlockNearTarget(onCreateBlock, target1) &&
                isBlockNearTarget(onStartBlock, target2) &&
                isBlockNearTarget(onResumeBlock, target3) &&
                isBlockNearTarget(onPauseBlock, target4) &&
                isBlockNearTarget(onStopBlock, target5) &&
                isBlockNearTarget(onDestroyBlock, target6);

        if (correct) {
            completeLevelSuccess();
        } else {
            // Reduce score for each failed attempt after the first
            if (attempts > 1 && score > 1) {
                score--;
            }

            Toast.makeText(this, "Not quite right. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isBlockNearTarget(View block, View target) {
        // Check if the block is positioned near enough to the target
        float blockCenterX = block.getX() + block.getWidth() / 2;
        float blockCenterY = block.getY() + block.getHeight() / 2;

        float targetCenterX = target.getX() + target.getWidth() / 2;
        float targetCenterY = target.getY() + target.getHeight() / 2;

        // Calculate distance
        float distance = (float) Math.sqrt(
                Math.pow(blockCenterX - targetCenterX, 2) +
                        Math.pow(blockCenterY - targetCenterY, 2));

        // Block is considered on target if its center is within 50dp of target center
        return distance < 150;
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
        // Show confirmation dialog if level is in progress
        finish();
    }
}