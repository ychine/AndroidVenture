/**
 * Manages audio playback and sound effects throughout the game.
 * Implements a singleton pattern to ensure consistent audio management.
 * Handles both background music and sound effects with volume control.
 */

package com.example.androidventure.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;

import com.example.androidventure.R;

public class SoundManager {
    private static SoundManager instance;
    private SoundPool soundPool;
    private SparseIntArray soundMap;
    private MediaPlayer backgroundMusic;
    private Context context;
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private float musicVolume = 0.2f;
    private float sfxVolume = 1.0f;
    private static final float SFX_VOLUME_MULTIPLIER = 1.5f;

    public static final int SOUND_CLICK = 1;
    public static final int SOUND_SUCCESS = 2;
    public static final int SOUND_FAILURE = 3;
    public static final int SOUND_DRAG = 4;
    public static final int SOUND_DROP = 5;
    public static final int SOUND_LEVEL_COMPLETE = 6;
    public static final int SOUND_UNLOCK = 7;

    private SoundManager(Context context) {
        this.context = context.getApplicationContext();
        initSoundPool();
        loadSounds();
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    private void initSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(8)
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        }

        soundMap = new SparseIntArray();
    }

    private void loadSounds() {
        soundMap.put(SOUND_CLICK, soundPool.load(context, R.raw.click, 1));
        soundMap.put(SOUND_SUCCESS, soundPool.load(context, R.raw.success, 1));
        soundMap.put(SOUND_FAILURE, soundPool.load(context, R.raw.failure, 1));
        soundMap.put(SOUND_DRAG, soundPool.load(context, R.raw.drag, 1));
        soundMap.put(SOUND_DROP, soundPool.load(context, R.raw.drop, 1));
        soundMap.put(SOUND_LEVEL_COMPLETE, soundPool.load(context, R.raw.level_complete, 1));
        soundMap.put(SOUND_UNLOCK, soundPool.load(context, R.raw.unlock, 1));
    }

    public void setVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(this.musicVolume, this.musicVolume);
        }
    }

    public void playSound(int soundId) {
        if (soundEnabled && soundMap.get(soundId) != 0) {

            float adjustedVolume = Math.min(1.0f, sfxVolume * SFX_VOLUME_MULTIPLIER);
            soundPool.play(soundMap.get(soundId), adjustedVolume, adjustedVolume, 1, 0, 1.0f);
        }
    }

    public void playMusic(int resId, boolean loop) {
        if (!musicEnabled) return;

        stopMusic();

        backgroundMusic = MediaPlayer.create(context, resId);
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(musicVolume, musicVolume);
            backgroundMusic.setLooping(loop);
            backgroundMusic.start();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null) {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.stop();
            }
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    public void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void resumeMusic() {
        if (musicEnabled && backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }
}