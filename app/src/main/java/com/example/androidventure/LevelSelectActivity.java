package com.example.androidventure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidventure.adapters.LevelAdapter;
import com.example.androidventure.models.Level;
import com.example.androidventure.utils.GameStateManager;

import java.util.ArrayList;
import java.util.List;

public class LevelSelectActivity extends AppCompatActivity implements LevelAdapter.OnLevelClickListener {

    private RecyclerView levelRecyclerView;
    private LevelAdapter levelAdapter;
    private List<Level> levels;
    private GameStateManager gameStateManager;
    private ImageButton backButton;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);
        Toast.makeText(this, "LevelSelectActivity launched!", Toast.LENGTH_SHORT).show();

        // Initialize UI components
        levelRecyclerView = findViewById(R.id.level_recycler_view);
        backButton = findViewById(R.id.back_button);
        titleTextView = findViewById(R.id.level_select_title);

        // Set up game state manager
        gameStateManager = new GameStateManager(this);

        // Set up RecyclerView with GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        levelRecyclerView.setLayoutManager(gridLayoutManager);

        // Initialize level data
        initializeLevels();

        // Set up adapter
        levelAdapter = new LevelAdapter(levels, this);
        levelRecyclerView.setAdapter(levelAdapter);

        // Set up back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update level unlock status
        updateLevelStatus();
    }

    private void initializeLevels() {
        levels = new ArrayList<>();

        // Level 1: Activity Forest
        levels.add(new Level(
                1,
                getString(R.string.level_1_name),
                getString(R.string.level_1_description),
                R.drawable.level1_icon,
                true,  // First level is always unlocked
                false,
                0,
                "com.example.androidventure.levels.ActivityForestLevel"
        ));

        // Level 2: UI Builder's Workshop
        levels.add(new Level(
                2,
                getString(R.string.level_2_name),
                getString(R.string.level_2_description),
                R.drawable.level2_icon,
                false,
                false,
                0,
                "com.example.androidventure.levels.UiBuilderLevel"
        ));

        // Level 3: Intent Gate
        levels.add(new Level(
                3,
                getString(R.string.level_3_name),
                getString(R.string.level_3_description),
                R.drawable.level3_icon,
                false,
                false,
                0,
                "com.example.androidventure.levels.IntentGateLevel"
        ));

        // Level 4: Variable Vault
        levels.add(new Level(
                4,
                getString(R.string.level_4_name),
                getString(R.string.level_4_description),
                R.drawable.level4_icon,
                false,
                false,
                0,
                "com.example.androidventure.levels.VariableVaultLevel"
        ));


    }

    private void updateLevelStatus() {
        // Load saved progress
        for (Level level : levels) {
            level.setCompleted(gameStateManager.isLevelCompleted(level.getId()));
            level.setScore(gameStateManager.getLevelScore(level.getId()));

            // A level is unlocked if it's the first level or the previous level is completed
            if (level.getId() > 1) {
                level.setUnlocked(gameStateManager.isLevelCompleted(level.getId() - 1));
            }
        }

        // Notify adapter of data change
        levelAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLevelClick(Level level) {
        if (level.isUnlocked()) {
            try {
                // Load the level activity dynamically using reflection
                Class<?> levelClass = Class.forName(level.getClassName());
                Intent intent = new Intent(this, levelClass);
                intent.putExtra("level_id", level.getId());
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                // Handle error - level not found
            }
        }
    }
}