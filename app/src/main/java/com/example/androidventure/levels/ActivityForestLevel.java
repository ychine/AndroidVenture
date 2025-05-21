package com.example.androidventure.levels;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidventure.R;
import com.example.androidventure.utils.GameStateManager;
import com.example.androidventure.utils.SoundManager;
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

    private DraggableCodeBlock onCreateBlock;
    private DraggableCodeBlock onStartBlock;
    private DraggableCodeBlock onResumeBlock;
    private DraggableCodeBlock onPauseBlock;
    private DraggableCodeBlock onStopBlock;
    private DraggableCodeBlock onDestroyBlock;

    private View target1;
    private View target2;
    private View target3;
    private View target4;
    private View target5;
    private View target6;

    private int attempts = 0;
    private int score = MAX_SCORE;

    private View confettiOverlay;
    private View hintOverlay;
    private TextView hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forest_level);

        //initailization
        gameStateManager = new GameStateManager(this);
        SoundManager.getInstance(this);
        initializeUI();
        setupDraggableBlocks();
        setupDropTargets();
        setupButtons();

        // tutorial if first time
        if (!gameStateManager.isTutorialCompleted()) {
            showTutorial();
        }

        confettiOverlay = findViewById(R.id.confetti_overlay);
        hintOverlay = findViewById(R.id.hint_overlay);
        hintText = findViewById(R.id.hint_text);
    }

    private void initializeUI() {
        gameArea = findViewById(R.id.game_area);
        levelTitle = findViewById(R.id.level_title);
        instructionText = findViewById(R.id.instruction_text);
        checkButton = findViewById(R.id.check_button);
        hintButton = findViewById(R.id.hint_button);

        levelTitle.setText(R.string.level_1_name);
        instructionText.setText("Drag the activity lifecycle methods into the correct order");
    }

    private void setupDraggableBlocks() {

        onCreateBlock = findViewById(R.id.block_oncreate);
        onStartBlock = findViewById(R.id.block_onstart);
        onResumeBlock = findViewById(R.id.block_onresume);
        onPauseBlock = findViewById(R.id.block_onpause);
        onStopBlock = findViewById(R.id.block_onstop);
        onDestroyBlock = findViewById(R.id.block_ondestroy);

        onCreateBlock.setBlockName("onCreate()");
        onStartBlock.setBlockName("onStart()");
        onResumeBlock.setBlockName("onResume()");
        onPauseBlock.setBlockName("onPause()");
        onStopBlock.setBlockName("onStop()");
        onDestroyBlock.setBlockName("onDestroy()");

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

    private void resetBlockPositions() {

        int baseX = 100;
        int baseY = gameArea.getHeight() - 300;

        if (baseY <= 0) {
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

        Toast.makeText(this, R.string.tutorial_drag, Toast.LENGTH_LONG).show();

        ObjectAnimator animation = ObjectAnimator.ofFloat(onCreateBlock, "translationY",
                onCreateBlock.getY(), onCreateBlock.getY() - 200, onCreateBlock.getY());
        animation.setDuration(2000);
        animation.start();

        gameStateManager.setTutorialCompleted(true);
    }

    private void showHint() {
    
        hintText.setText("The Android activity lifecycle follows a specific sequence from creation to destruction.");
        hintOverlay.setVisibility(View.VISIBLE);
        hintOverlay.setAlpha(0f);
        hintOverlay.animate().alpha(1f).setDuration(400).start();
      
        new Handler().postDelayed(() -> hintOverlay.animate().alpha(0f).setDuration(400).withEndAction(() -> hintOverlay.setVisibility(View.GONE)).start(), 3000);
     
        ObjectAnimator highlight = ObjectAnimator.ofFloat(onCreateBlock, "alpha", 1f, 0.5f, 1f);
        highlight.setDuration(1000);
        highlight.start();
        SoundManager.getInstance(this).playSound(SoundManager.SOUND_CLICK);
    }

    private void checkAnswer() {
        attempts++;
        boolean correct = isBlockNearTarget(onCreateBlock, target1) &&
                isBlockNearTarget(onStartBlock, target2) &&
                isBlockNearTarget(onResumeBlock, target3) &&
                isBlockNearTarget(onPauseBlock, target4) &&
                isBlockNearTarget(onStopBlock, target5) &&
                isBlockNearTarget(onDestroyBlock, target6);
        if (correct) {
           
            animateBlockSuccess(onCreateBlock);
            animateBlockSuccess(onStartBlock);
            animateBlockSuccess(onResumeBlock);
            animateBlockSuccess(onPauseBlock);
            animateBlockSuccess(onStopBlock);
            animateBlockSuccess(onDestroyBlock);
            showConfetti();
            completeLevelSuccess();
        } else {
            if (attempts > 1 && score > 1) {
                score--;
            }
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

    private void showConfetti() {
        confettiOverlay.setBackgroundColor(Color.parseColor("#80FFD700"));
        confettiOverlay.setVisibility(View.VISIBLE);
        confettiOverlay.setAlpha(0f);
        confettiOverlay.animate().alpha(1f).setDuration(400).start();
       
        new Handler().postDelayed(() -> confettiOverlay.animate().alpha(0f).setDuration(800).withEndAction(() -> confettiOverlay.setVisibility(View.GONE)).start(), 1500);
    }

    private void completeLevelSuccess() {
        SoundManager.getInstance(this).playSound(SoundManager.SOUND_SUCCESS);
        Toast.makeText(this, R.string.level_complete, Toast.LENGTH_LONG).show();

        Log.d("ActivityForestLevel", "Attempting to play success sound");

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