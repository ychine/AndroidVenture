package com.example.androidventure.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidventure.R;
import com.example.androidventure.models.Level;

import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {

    private List<Level> levels;
    private OnLevelClickListener listener;

    /**
     * Interface for level click events
     */
    public interface OnLevelClickListener {
        void onLevelClick(Level level);
    }

    /**
     * Constructor for LevelAdapter
     *
     * @param levels List of levels to display
     * @param listener Click listener for level items
     */
    public LevelAdapter(List<Level> levels, OnLevelClickListener listener) {
        this.levels = levels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_level, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        Level level = levels.get(position);
        holder.bind(level, listener);
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    /**
     * ViewHolder class for level items
     */
    static class LevelViewHolder extends RecyclerView.ViewHolder {
        private ImageView levelBackground;
        private ImageView levelIcon;
        private TextView levelNumber;
        private TextView levelName;
        private TextView levelDescription;
        private RatingBar levelStars;
        private ImageView lockIcon;
        private View overlay;

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            levelBackground = itemView.findViewById(R.id.level_background);
            levelIcon = itemView.findViewById(R.id.level_icon);
            levelNumber = itemView.findViewById(R.id.level_number);
            levelName = itemView.findViewById(R.id.level_name);
            levelDescription = itemView.findViewById(R.id.level_description);
            levelStars = itemView.findViewById(R.id.level_stars);
            lockIcon = itemView.findViewById(R.id.lock_icon);
            overlay = itemView.findViewById(R.id.overlay);
        }

        /**
         * Bind level data to the ViewHolder
         *
         * @param level Level to display
         * @param listener Click listener
         */
        public void bind(final Level level, final OnLevelClickListener listener) {
            // Set level number
            levelNumber.setText(String.valueOf(level.getId()));

            // Set level name and description
            levelName.setText(level.getName());
            levelDescription.setText(level.getDescription());

            // Set level icon and background
            levelIcon.setImageResource(level.getIconResourceId());

            // Set background based on level theme
            switch (level.getId()) {
                case 1:
                    levelBackground.setBackgroundResource(R.color.level1_primary);
                    break;
                case 2:
                    levelBackground.setBackgroundResource(R.color.level2_primary);
                    break;
                case 3:
                    levelBackground.setBackgroundResource(R.color.level3_primary);
                    break;
                case 4:
                    levelBackground.setBackgroundResource(R.color.level4_primary);
                    break;
                case 5:
                    levelBackground.setBackgroundResource(R.color.level5_primary);
                    break;
                case 6:
                    levelBackground.setBackgroundResource(R.color.level6_primary);
                    break;
            }

            // Set stars based on completion status and score
            if (level.isCompleted()) {
                levelStars.setRating(level.getScore());
                levelStars.setVisibility(View.VISIBLE);
            } else {
                levelStars.setVisibility(View.INVISIBLE);
            }

            // Handle lock/unlock status
            if (level.isUnlocked()) {
                lockIcon.setVisibility(View.GONE);
                overlay.setAlpha(0.5f);

                // Set click listener
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onLevelClick(level);
                    }
                });
            } else {
                lockIcon.setVisibility(View.VISIBLE);
                overlay.setAlpha(0.7f);
                itemView.setOnClickListener(null);
            }
        }
    }
}