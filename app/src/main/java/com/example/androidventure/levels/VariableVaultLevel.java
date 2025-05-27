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
    private SoundManager soundManager;

    private DraggableCodeBlock stringBlock;
    private DraggableCodeBlock intBlock;
    private DraggableCodeBlock booleanBlock;
    private DraggableCodeBlock floatBlock;

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

        gameStateManager = new GameStateManager(this);
        soundManager = SoundManager.getInstance(this);


        soundManager.playMusic(R.raw.levelbgmusic, true);


        initializeUI();
        setupDraggableBlocks();
        setupButtons();

        if (!gameStateManager.isTutorialCompleted()) {
            showTutorial();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.resumeMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundManager.stopMusic();
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

        levelTitle.setText(R.string.level_4_name);

        instructionText.setText("Match the variables with their correct data types");
    }

    private void setupDraggableBlocks() {
        stringBlock = findViewById(R.id.block_string);
        intBlock = findViewById(R.id.block_int);
        booleanBlock = findViewById(R.id.block_boolean);
        floatBlock = findViewById(R.id.block_float);

        stringBlock.setBlockName("name = \"John\"");
        intBlock.setBlockName("age = 25");
        booleanBlock.setBlockName("isActive = true");
        floatBlock.setBlockName("price = 19.99f");

        resetBlockPositions();
    }

    private void resetBlockPositions() {

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

        Toast.makeText(this, "Drag the variable blocks to match them with their data types", Toast.LENGTH_LONG).show();

        ObjectAnimator animation = ObjectAnimator.ofFloat(stringBlock, "translationY",
                stringBlock.getY(), stringBlock.getY() - 200, stringBlock.getY());
        animation.setDuration(2000);
        animation.start();


        gameStateManager.setTutorialCompleted(true);
    }

    private void showHint() {

        hintText.setText("Remember: String for text, int for whole numbers, boolean for true/false, and float for decimal numbers.");
        hintOverlay.setVisibility(View.VISIBLE);
        hintOverlay.setAlpha(0f);
        hintOverlay.animate().alpha(1f).setDuration(400).start();

        new Handler().postDelayed(() -> hintOverlay.animate().alpha(0f).setDuration(400).withEndAction(() -> hintOverlay.setVisibility(View.GONE)).start(), 3000);

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

            soundManager.playSound(SoundManager.SOUND_SUCCESS);

            animateBlockSuccess(stringBlock);
            animateBlockSuccess(intBlock);
            animateBlockSuccess(booleanBlock);
            animateBlockSuccess(floatBlock);
            completeLevelSuccess();
        } else {

            if (attempts > 1 && score > 1) {
                score--;
            }

            soundManager.playSound(SoundManager.SOUND_FAILURE);
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