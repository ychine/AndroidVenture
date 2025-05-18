package com.example.androidventure.levels;

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

public class UiBuilderLevel extends AppCompatActivity {

    private static final int LEVEL_ID = 2;
    private static final int MAX_SCORE = 3;

    private ConstraintLayout gameArea;
    private TextView levelTitle;
    private TextView instructionText;
    private Button checkButton;
    private GameStateManager gameStateManager;

    private DraggableCodeBlock buttonBlock;
    private DraggableCodeBlock textViewBlock;

    private int attempts = 0;
    private int score = MAX_SCORE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_builder);

        // Initialize game state manager
        gameStateManager = new GameStateManager(this);

        // Initialize UI components
        initializeUI();

        // Setup draggable blocks
        setupDraggableBlocks();

        // Setup buttons
        setupButtons();
    }

    private void initializeUI() {
        gameArea = findViewById(R.id.game_area);
        levelTitle = findViewById(R.id.level_title);
        instructionText = findViewById(R.id.instruction_text);
        checkButton = findViewById(R.id.check_button);

        // Set level name
        levelTitle.setText(R.string.level_2_name);

        // Set instruction text
        instructionText.setText("Drag the UI components into the correct layout.");
    }

    private void setupDraggableBlocks() {
        buttonBlock = findViewById(R.id.block_button);
        textViewBlock = findViewById(R.id.block_textview);

        // Setup block data and listeners
        buttonBlock.setBlockName("Button");
        textViewBlock.setBlockName("TextView");

        // Reset positions to original places
        resetBlockPositions();
    }

    private void resetBlockPositions() {
        // Position blocks in a randomized order at the bottom area
        buttonBlock.setX(100);
        buttonBlock.setY(gameArea.getHeight() - 200);

        textViewBlock.setX(300);
        textViewBlock.setY(gameArea.getHeight() - 200);
    }

    private void setupButtons() {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void checkAnswer() {
        attempts++;

        // Check if blocks are in correct positions
        boolean correct = isBlockNearTarget(buttonBlock, findViewById(R.id.target_button)) &&
                isBlockNearTarget(textViewBlock, findViewById(R.id.target_textview));

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
        float blockCenterX = block.getX() + block.getWidth() / 2;
        float blockCenterY = block.getY() + block.getHeight() / 2;

        float targetCenterX = target.getX() + target.getWidth() / 2;
        float targetCenterY = target.getY() + target.getHeight() / 2;

        float distance = (float) Math.sqrt(
                Math.pow(blockCenterX - targetCenterX, 2) +
                        Math.pow(blockCenterY - targetCenterY, 2));

        return distance < 150;
    }

    private void completeLevelSuccess() {
        Toast.makeText(this, R.string.level_complete, Toast.LENGTH_LONG).show();
        gameStateManager.setLevelCompleted(LEVEL_ID, true);
        gameStateManager.setLevelScore(LEVEL_ID, score);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
