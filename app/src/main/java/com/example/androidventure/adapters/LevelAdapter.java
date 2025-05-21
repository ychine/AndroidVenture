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
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.os.Handler;

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
        private View rippleEffect;

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
            rippleEffect = itemView.findViewById(R.id.ripple_effect);
        }

        /**
         * Bind level data to the ViewHolder
         *
         * @param level Level to display
         * @param listener Click listener
         */
        public void bind(final Level level, final OnLevelClickListener listener) {
            
            levelNumber.setText(String.valueOf(level.getId()));

            levelName.setText(level.getName());
            levelDescription.setText(level.getDescription());

            levelIcon.setImageResource(level.getIconResourceId());

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

           
            if (level.isCompleted()) {
                levelStars.setRating(level.getScore());
                levelStars.setVisibility(View.VISIBLE);
            } else {
                levelStars.setVisibility(View.INVISIBLE);
            }

         
            if (level.isUnlocked()) {
          
                lockIcon.animate().alpha(0f).setDuration(400).start();
                overlay.animate().alpha(0.15f).setDuration(400).start();

                if (level.isCompleted()) {
                    levelStars.setVisibility(View.VISIBLE);
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(levelStars, "scaleX", 0.7f, 1.2f, 1f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(levelStars, "scaleY", 0.7f, 1.2f, 1f);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(scaleX, scaleY);
                    set.setDuration(600);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                } else {
                    levelStars.setVisibility(View.INVISIBLE);
                }

                itemView.setClickable(true);
                itemView.setFocusable(true);
                itemView.setForeground(itemView.getContext().getDrawable(android.R.drawable.list_selector_background));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    
                        rippleEffect.setPressed(true);
                        new Handler().postDelayed(() -> rippleEffect.setPressed(false), 300);
                        listener.onLevelClick(level);
                    }
                });
            } else {
              
                lockIcon.setVisibility(View.VISIBLE);
                lockIcon.animate().alpha(1f).setDuration(400).start();
                overlay.animate().alpha(0.7f).setDuration(400).start();
                levelStars.setVisibility(View.INVISIBLE);
                itemView.setOnClickListener(null);
                itemView.setClickable(false);
                itemView.setFocusable(false);
            }
        }
    }
}