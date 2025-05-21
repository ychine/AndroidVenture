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
import com.example.androidventure.utils.SoundManager;
import com.example.androidventure.widgets.DraggableCodeBlock;

public class NotificationAnatomyLevel extends AppCompatActivity {

    private static final int LEVEL_ID = 6;
    private static final int MAX_SCORE = 3;

    private ConstraintLayout gameArea;
    private TextView levelTitle;
    private TextView instructionText;
    private Button checkButton;
    private Button hintButton;
    private ImageView notificationAnatomyImage;
    private GameStateManager gameStateManager;

    private DraggableCodeBlock smallIconBlock;
    private DraggableCodeBlock appNameBlock;
    private DraggableCodeBlock timeStampBlock;
    private DraggableCodeBlock largeIconBlock;
    private DraggableCodeBlock titleBlock;
    private DraggableCodeBlock textBlock;

    private View targetSmallIcon;
    private View targetAppName;
    private View targetTimeStamp;
    private View targetLargeIcon;
    private View targetTitle;
    private View targetText;

    private int attempts = 0;
    private int score = MAX_SCORE;

    private View confettiOverlay;
    private View hintOverlay;
    private TextView hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_anatomy_level);

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
        notificationAnatomyImage = findViewById(R.id.notification_anatomy_image);
        confettiOverlay = findViewById(R.id.confetti_overlay);
        hintOverlay = findViewById(R.id.hint_overlay);
        hintText = findViewById(R.id.hint_text);

        levelTitle.setText(R.string.level_6_name);
        instructionText.setText("Match the notification components with their correct positions in the notification anatomy");
    }

    private void setupDraggableBlocks() {
        smallIconBlock = findViewById(R.id.block_small_icon);
        appNameBlock = findViewById(R.id.block_app_name);
        timeStampBlock = findViewById(R.id.block_time_stamp);
        largeIconBlock = findViewById(R.id.block_large_icon);
        titleBlock = findViewById(R.id.block_title);
        textBlock = findViewById(R.id.block_text);

        smallIconBlock.setBlockName("Small Icon");
        appNameBlock.setBlockName("App Name");
        timeStampBlock.setBlockName("Time Stamp");
        largeIconBlock.setBlockName("Large Icon");
        titleBlock.setBlockName("Title");
        textBlock.setBlockName("Text");

        resetBlockPositions();
    }

    private void resetBlockPositions() {
        int baseX = 100;
        int baseY = gameArea.getHeight() - 300;

        if (baseY <= 0) {
            baseY = 1200;
        }

        smallIconBlock.setX(baseX);
        smallIconBlock.setY(baseY);

        appNameBlock.setX(baseX + 250);
        appNameBlock.setY(baseY);

        timeStampBlock.setX(baseX + 500);
        timeStampBlock.setY(baseY);

        largeIconBlock.setX(baseX);
        largeIconBlock.setY(baseY + 150);

        titleBlock.setX(baseX + 250);
        titleBlock.setY(baseY + 150);

        textBlock.setX(baseX + 500);
        textBlock.setY(baseY + 150);
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
        Toast.makeText(this, "Drag the notification components to match them with their correct positions", Toast.LENGTH_LONG).show();

        ObjectAnimator animation = ObjectAnimator.ofFloat(smallIconBlock, "translationY",
                smallIconBlock.getY(), smallIconBlock.getY() - 200, smallIconBlock.getY());
        animation.setDuration(2000);
        animation.start();

        gameStateManager.setTutorialCompleted(true);
    }

    private void showHint() {
        hintText.setText("Look at the notification anatomy image and match each component with its corresponding number. The small icon is at the top left, followed by the app name and timestamp.");
        hintOverlay.setVisibility(View.VISIBLE);
        hintOverlay.setAlpha(0f);
        hintOverlay.animate().alpha(1f).setDuration(400).start();
        
        new Handler().postDelayed(() -> hintOverlay.animate().alpha(0f).setDuration(400).withEndAction(() -> hintOverlay.setVisibility(View.GONE)).start(), 3000);
        
        ObjectAnimator highlight = ObjectAnimator.ofFloat(smallIconBlock, "alpha", 1f, 0.5f, 1f);
        highlight.setDuration(1000);
        highlight.start();
    }

    private void checkAnswer() {
        attempts++;
        boolean correct = isBlockNearTarget(smallIconBlock, findViewById(R.id.target_small_icon)) &&
                isBlockNearTarget(appNameBlock, findViewById(R.id.target_app_name)) &&
                isBlockNearTarget(timeStampBlock, findViewById(R.id.target_time_stamp)) &&
                isBlockNearTarget(largeIconBlock, findViewById(R.id.target_large_icon)) &&
                isBlockNearTarget(titleBlock, findViewById(R.id.target_title)) &&
                isBlockNearTarget(textBlock, findViewById(R.id.target_text));

        if (correct) {
            SoundManager.getInstance(this).playSound(SoundManager.SOUND_SUCCESS);
            animateBlockSuccess(smallIconBlock);
            animateBlockSuccess(appNameBlock);
            animateBlockSuccess(timeStampBlock);
            animateBlockSuccess(largeIconBlock);
            animateBlockSuccess(titleBlock);
            animateBlockSuccess(textBlock);
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