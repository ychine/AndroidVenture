/**
 * Level 2: UI Builder's Workshop
 * Interactive level teaching Android UI components through drag-and-drop assembly.
 * Players match UI components with their correct positions and properties.
 */

package com.example.androidventure.levels;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.androidventure.utils.SoundManager;

public class UiBuilderLevel extends AppCompatActivity {

    private static final int LEVEL_ID = 2;
    private static final int MAX_SCORE = 3;

    private ConstraintLayout gameArea;
    private TextView levelTitle;
    private TextView instructionText;
    private Button checkButton;
    private Button hintButton;
    private GameStateManager gameStateManager;
    private SoundManager soundManager;

    private DraggableCodeBlock buttonBlock;
    private DraggableCodeBlock textViewBlock;
    private DraggableCodeBlock editTextBlock;
    private DraggableCodeBlock recyclerViewBlock;
    private DraggableCodeBlock imageViewBlock;

    private View targetEditText;
    private View targetRecyclerView;
    private View targetImageView;

    private int attempts = 0;
    private int score = MAX_SCORE;

    private View confettiOverlay;
    private View hintOverlay;
    private TextView hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_builder);

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

        levelTitle.setText(R.string.level_2_name);

        instructionText.setText("Drag the UI components into the correct layout.");
    }

    private void setupDraggableBlocks() {
        buttonBlock = findViewById(R.id.block_button);
        textViewBlock = findViewById(R.id.block_textview);
        editTextBlock = findViewById(R.id.block_edittext);
        recyclerViewBlock = findViewById(R.id.block_recyclerview);
        imageViewBlock = findViewById(R.id.block_imageview);

        buttonBlock.setBlockName("Button");
        textViewBlock.setBlockName("TextView");
        editTextBlock.setBlockName("EditText");
        recyclerViewBlock.setBlockName("RecyclerView");
        imageViewBlock.setBlockName("ImageView");

        resetBlockPositions();
    }

    private void resetBlockPositions() {

        buttonBlock.setX(100);
        buttonBlock.setY(gameArea.getHeight() - 10);

        textViewBlock.setX(100);
        textViewBlock.setY(gameArea.getHeight() - 500);

        editTextBlock.setX(50);
        editTextBlock.setY(gameArea.getHeight() - 500);

        recyclerViewBlock.setX(100);
        recyclerViewBlock.setY(gameArea.getHeight() - 50);

        imageViewBlock.setX(90);
        imageViewBlock.setY(gameArea.getHeight() - 300);
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

        Toast.makeText(this, "Drag the UI components to match them with their icons", Toast.LENGTH_LONG).show();

        ObjectAnimator animation = ObjectAnimator.ofFloat(buttonBlock, "translationY",
                buttonBlock.getY(), buttonBlock.getY() - 200, buttonBlock.getY());
        animation.setDuration(2000);
        animation.start();

        gameStateManager.setTutorialCompleted(true);
    }

    private void showHint() {

        hintText.setText("Remember: Button for actions, TextView for text display, EditText for user input, RecyclerView for lists, and ImageView for images.");
        hintOverlay.setVisibility(View.VISIBLE);
        hintOverlay.setAlpha(0f);
        hintOverlay.animate().alpha(1f).setDuration(400).start();

        new Handler().postDelayed(() -> hintOverlay.animate().alpha(0f).setDuration(400).withEndAction(() -> hintOverlay.setVisibility(View.GONE)).start(), 3000);

        ObjectAnimator highlight = ObjectAnimator.ofFloat(buttonBlock, "alpha", 1f, 0.5f, 1f);
        highlight.setDuration(1000);
        highlight.start();
    }

    private void checkAnswer() {
        attempts++;

        boolean correct = isBlockNearTarget(buttonBlock, findViewById(R.id.target_button)) &&
                isBlockNearTarget(textViewBlock, findViewById(R.id.target_textview)) &&
                isBlockNearTarget(editTextBlock, findViewById(R.id.target_edittext)) &&
                isBlockNearTarget(recyclerViewBlock, findViewById(R.id.target_recyclerview)) &&
                isBlockNearTarget(imageViewBlock, findViewById(R.id.target_imageview));

        if (correct) {

            boolean buttonCorrect = buttonBlock.getBlockName().equals("Button");
            boolean textViewCorrect = textViewBlock.getBlockName().equals("TextView");
            boolean editTextCorrect = editTextBlock.getBlockName().equals("EditText");
            boolean recyclerViewCorrect = recyclerViewBlock.getBlockName().equals("RecyclerView");
            boolean imageViewCorrect = imageViewBlock.getBlockName().equals("ImageView");

            correct = buttonCorrect && textViewCorrect && editTextCorrect && 
                     recyclerViewCorrect && imageViewCorrect;
        }

        if (correct) {

            SoundManager.getInstance(this).playSound(SoundManager.SOUND_SUCCESS);

            animateBlockSuccess(buttonBlock);
            animateBlockSuccess(textViewBlock);
            animateBlockSuccess(editTextBlock);
            animateBlockSuccess(recyclerViewBlock);
            animateBlockSuccess(imageViewBlock);
            completeLevelSuccess();
        } else {

            if (attempts > 1 && score > 1) {
                score--;
            }

            SoundManager.getInstance(this).playSound(SoundManager.SOUND_FAILURE);
            Toast.makeText(this, "Not quite right. Make sure each component is matched with its correct target!", Toast.LENGTH_SHORT).show();
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

        return distance < 50;
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
