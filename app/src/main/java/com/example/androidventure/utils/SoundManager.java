package com.example.androidventure.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;

import com.example.androidventure.R;

/**
 * Manages game sounds and music
 */
public class SoundManager {
    private static SoundManager instance;
    private SoundPool soundPool;
    private SparseIntArray soundMap;
    private MediaPlayer backgroundMusic;
    private Context context;
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private float volume = 1.0f;

    // Constants for sound IDs
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

    /**
     * Get the singleton instance of SoundManager
     */
    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    /**
     * Initialize the sound pool based on Android version
     */
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
            // For older devices
            soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        }

        soundMap = new SparseIntArray();
    }

    /**
     * Load all sound effects into memory
     */
    private void loadSounds() {
        // Load sound effects from raw resources
        soundMap.put(SOUND_CLICK, soundPool.load(context, R.raw.click, 1));
        soundMap.put(SOUND_SUCCESS, soundPool.load(context, R.raw.success, 1));
        soundMap.put(SOUND_FAILURE, soundPool.load(context, R.raw.failure, 1));
        soundMap.put(SOUND_DRAG, soundPool.load(context, R.raw.drag, 1));
        soundMap.put(SOUND_DROP, soundPool.load(context, R.raw.drop, 1));
        soundMap.put(SOUND_LEVEL_COMPLETE, soundPool.load(context, R.raw.level_complete, 1));
        soundMap.put(SOUND_UNLOCK, soundPool.load(context, R.raw.unlock, 1));
    }

    /**
     * Play a sound effect
     *
     * @param soundId ID of the sound to play
     */
    public void playSound(int soundId) {
        if (soundEnabled && soundMap.get(soundId) != 0) {
            soundPool.play(soundMap.get(soundId), volume, volume, 1, 0, 1.0f);
        }
    }

    /**
     * Start playing background music
     *
     * @param resId Resource ID of the music file
     * @param loop Whether to loop the music
     */
    public void playMusic(int resId, boolean loop) {
        if (!musicEnabled) return;

        stopMusic();

        backgroundMusic = MediaPlayer.create(context, resId);
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume, volume);
            backgroundMusic.setLooping(loop);
            backgroundMusic.start();
        }
    }

    /**
     * Stop the currently playing background music
     */
    public void stopMusic() {
        if (backgroundMusic != null) {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.stop();
            }
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    /**
     * Pause the background music
     */
    public void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    /**
     * Resume the background music if it was paused
     */
    public void resumeMusic() {
        if (musicEnabled && backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    /**
     * Enable or disable sound effects
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    /**
     * Enable or disable background music
     */
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (enabled) {
            resumeMusic();
        } else {
            pauseMusic();
        }
    }

    /**
     * Set the volume for both sound effects and music
     *
     * @param volume Volume level (0.0f to 1.0f)
     */
    public void setVolume(float volume) {
        this.volume = volume;
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume, volume);
        }
    }

    /**
     * Release all resources
     */
    public void release() {
        stopMusic();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        instance = null;
    }
}