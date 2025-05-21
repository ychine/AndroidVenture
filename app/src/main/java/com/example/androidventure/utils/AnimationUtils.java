package com.example.androidventure.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Utility class for custom animations used throughout the game
 */
public class AnimationUtils {

    private static final long DEFAULT_DURATION = 300;

    public static AnimatorSet createBounceAnimation(View view, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f);

        scaleX.setDuration(duration);
        scaleY.setDuration(duration);

        scaleX.setInterpolator(new BounceInterpolator());
        scaleY.setInterpolator(new BounceInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);

        return animatorSet;
    }

    public static AnimatorSet createBounceAnimation(View view) {
        return createBounceAnimation(view, DEFAULT_DURATION);
    }


    public static AnimatorSet createPulseAnimation(View view, int repeatCount) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.8f, 1f);

        scaleX.setDuration(1000);
        scaleY.setDuration(1000);
        alpha.setDuration(1000);

        scaleX.setRepeatCount(repeatCount);
        scaleY.setRepeatCount(repeatCount);
        alpha.setRepeatCount(repeatCount);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);

        return animatorSet;
    }

    public static ObjectAnimator createFadeInAnimation(View view, long duration) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(duration);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        return fadeIn;
    }


    public static ObjectAnimator createFadeOutAnimation(View view, long duration) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(duration);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        return fadeOut;
    }


    public static ObjectAnimator createSlideInAnimation(View view, long duration) {
        float distance = view.getResources().getDisplayMetrics().heightPixels;
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(view, "translationY", distance, 0f);
        slideIn.setDuration(duration);
        slideIn.setInterpolator(new DecelerateInterpolator());
        return slideIn;
    }


    public static ObjectAnimator createSlideOutAnimation(View view, long duration) {
        float distance = view.getResources().getDisplayMetrics().heightPixels;
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(view, "translationY", 0f, distance);
        slideOut.setDuration(duration);
        slideOut.setInterpolator(new AccelerateInterpolator());
        return slideOut;
    }

    public static AnimatorSet createSuccessAnimation(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.3f, 1f);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);

        scaleX.setDuration(700);
        scaleY.setDuration(700);
        rotation.setDuration(700);

        scaleX.setInterpolator(new OvershootInterpolator());
        scaleY.setInterpolator(new OvershootInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotation);

        return animatorSet;
    }

    public static ObjectAnimator createShakeAnimation(View view) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(view, "translationX", 0, -15, 15, -10, 10, -5, 5, 0);
        shake.setDuration(500);
        return shake;
    }


    public static AnimatorSet createHighlightAnimation(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.7f, 1f);

        scaleX.setDuration(500);
        scaleY.setDuration(500);
        alpha.setDuration(500);

        scaleX.setRepeatCount(2);
        scaleY.setRepeatCount(2);
        alpha.setRepeatCount(2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);

        return animatorSet;
    }

    public static ObjectAnimator createFloatingAnimation(View view, float distance, int repeatCount) {
        ObjectAnimator floatAnim = ObjectAnimator.ofFloat(view, "translationY", 0f, -distance, 0f, distance, 0f);
        floatAnim.setDuration(3000);
        floatAnim.setRepeatCount(repeatCount);
        floatAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        return floatAnim;
    }
}