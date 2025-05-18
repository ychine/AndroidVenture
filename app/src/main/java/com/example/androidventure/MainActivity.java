package com.example.androidventure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Switch;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button settingsButton;
    private ImageView logoImageView;
    private TextView titleTextView;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize preferences
        preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);

        // Initialize UI components
        startButton = findViewById(R.id.start_button);
        settingsButton = findViewById(R.id.settings_button);
        logoImageView = findViewById(R.id.logo_image);
        titleTextView = findViewById(R.id.title_text);

        // Enhanced animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1500);
        logoImageView.startAnimation(fadeIn);
        titleTextView.startAnimation(fadeIn);

        // Add bounce animation for buttons
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        startButton.startAnimation(bounce);
        settingsButton.startAnimation(bounce);

        // Set up click listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enhanced button animation
                v.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Launching Level Select...", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LevelSelectActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        }
                    });
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsDialog();
            }
        });
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_settings, null);
        
        // Initialize settings switches
        Switch soundSwitch = dialogView.findViewById(R.id.sound_switch);
        Switch musicSwitch = dialogView.findViewById(R.id.music_switch);
        Switch vibrationSwitch = dialogView.findViewById(R.id.vibration_switch);
        
        // Load saved preferences
        soundSwitch.setChecked(preferences.getBoolean("sound_enabled", true));
        musicSwitch.setChecked(preferences.getBoolean("music_enabled", true));
        vibrationSwitch.setChecked(preferences.getBoolean("vibration_enabled", true));
        
        // Set up switch listeners
        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("sound_enabled", isChecked).apply();
        });
        
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("music_enabled", isChecked).apply();
        });
        
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("vibration_enabled", isChecked).apply();
        });

        builder.setView(dialogView)
               .setTitle("Settings")
               .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}