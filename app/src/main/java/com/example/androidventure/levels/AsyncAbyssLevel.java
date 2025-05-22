/**
 * Level 5: Async Abyss
 * Teaches asynchronous programming concepts in Android through interactive gameplay.
 * Players learn about different async mechanisms:
 * - AsyncTask
 * - Coroutines
 * - Handlers
 * - RxJava
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

public class AsyncAbyssLevel extends AppCompatActivity {

    private static final int LEVEL_ID = 5;
    private static final int MAX_SCORE = 3;

    private ConstraintLayout gameArea;
    private TextView levelTitle;
    private TextView instructionText;
    private Button checkButton;
    private Button hintButton;
    private GameStateManager gameStateManager;

    private DraggableCodeBlock asyncTaskBlock;
    private DraggableCodeBlock coroutineBlock;
    private DraggableCodeBlock handlerBlock;
    private DraggableCodeBlock rxJavaBlock;

    private View targetAsyncTask;
    private View targetCoroutine;
    private View targetHandler;
    private View targetRxJava;

    private int attempts = 0;
    private int score = MAX_SCORE;

    private View confettiOverlay;
    private View hintOverlay;
    private TextView hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_abyss_level);

        gameStateManager = new GameStateManager(this);
        SoundManager.getInstance(this);

        initializeUI();
        setupDraggableBlocks();
        setupButtons();

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
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    private void setupDraggableBlocks() {
        ConstraintLayout codeBlocksArea = findViewById(R.id.code_blocks_area);

        asyncTaskBlock = new DraggableCodeBlock(this);
        asyncTaskBlock.setText("AsyncTask");

        coroutineBlock = new DraggableCodeBlock(this);
        coroutineBlock.setText("Coroutine");

        handlerBlock = new DraggableCodeBlock(this);
        handlerBlock.setText("Handler");

        rxJavaBlock = new DraggableCodeBlock(this);
        rxJavaBlock.setText("RxJava");


        codeBlocksArea.addView(asyncTaskBlock);
        codeBlocksArea.addView(coroutineBlock);
        codeBlocksArea.addView(handlerBlock);
        codeBlocksArea.addView(rxJavaBlock);

        targetAsyncTask = findViewById(R.id.target_asynctask);
        targetCoroutine = findViewById(R.id.target_coroutine);
        targetHandler = findViewById(R.id.target_handler);
        targetRxJava = findViewById(R.id.target_rxjava);

        positionBlocksAndTargets();
    }

    private void positionBlocksAndTargets() {

        int blockSpacing = 120;
        asyncTaskBlock.setX(50);
        asyncTaskBlock.setY(1000);
        coroutineBlock.setX(100  + blockSpacing);
        coroutineBlock.setY(800);
        handlerBlock.setX(150  + blockSpacing);
        handlerBlock.setY(1000);
        rxJavaBlock.setX(200 + blockSpacing);
        rxJavaBlock.setY(800);
    }

    private void setupButtons() {
        checkButton.setOnClickListener(v -> checkAnswer());
        hintButton.setOnClickListener(v -> showHint());
    }

    private void showTutorial() {

        Toast.makeText(this, "Drag the async operations to match them with their best use cases", Toast.LENGTH_LONG).show();

        ObjectAnimator animation = ObjectAnimator.ofFloat(asyncTaskBlock, "translationY",
                asyncTaskBlock.getY(), asyncTaskBlock.getY() - 200, asyncTaskBlock.getY());
        animation.setDuration(2000);
        animation.start();


        gameStateManager.setTutorialCompleted(true);
    }

    private void showHint() {

        hintText.setText("Match each async operation with its best use case:\n\n" +
                "• AsyncTask: For simple background tasks\n" +
                "• Coroutine: For modern, structured concurrency\n" +
                "• Handler: For UI thread operations\n" +
                "• RxJava: For complex reactive streams");
        hintOverlay.setVisibility(View.VISIBLE);
        hintOverlay.setAlpha(0f);
        hintOverlay.animate().alpha(1f).setDuration(400).start();


        new Handler().postDelayed(() -> hintOverlay.animate().alpha(0f).setDuration(400)
                .withEndAction(() -> hintOverlay.setVisibility(View.GONE)).start(), 3000);
        ObjectAnimator highlight = ObjectAnimator.ofFloat(asyncTaskBlock, "alpha", 1f, 0.5f, 1f);
        highlight.setDuration(1000);
        highlight.start();
    }

    private void checkAnswer() {
        attempts++;
        boolean correct = isBlockNearTarget(asyncTaskBlock, targetAsyncTask) &&
                isBlockNearTarget(coroutineBlock, targetCoroutine) &&
                isBlockNearTarget(handlerBlock, targetHandler) &&
                isBlockNearTarget(rxJavaBlock, targetRxJava);

        if (correct) {
            SoundManager.getInstance(this).playSound(SoundManager.SOUND_SUCCESS);
            animateBlockSuccess(asyncTaskBlock);
            animateBlockSuccess(coroutineBlock);
            animateBlockSuccess(handlerBlock);
            animateBlockSuccess(rxJavaBlock);
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

    private void completeLevelSuccess() {
        Toast.makeText(this, R.string.level_complete, Toast.LENGTH_LONG).show();

        gameStateManager.setLevelCompleted(LEVEL_ID, true);
        gameStateManager.setLevelScore(LEVEL_ID, score);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(gameArea, "alpha",
                1f, 0.5f);
        fadeOut.setDuration(1000);
        fadeOut.start();

        gameArea.postDelayed(() -> finish(), 2000);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
} 