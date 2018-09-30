package com.strongholdgames.timer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class TimerActivity extends Activity {
    private static final String TAG = "SHG_" + TimerActivity.class.getSimpleName();
    private static final long TEN_SECONDS = 10_000L;
    protected Button btnStartStop;
    protected Button btnReset;
    protected TextView txtTimeLeft;
    protected SHGTimer timer;
    private int resFinishedSound = 0;
    private int resTickingSound = 0;
    private int resBackgroundSound = 0;
    private int duration = 0;
    private boolean isTicking = false;
    private MediaPlayer playerForeground;
    private MediaPlayer playerBackground;
    private SparseIntArray resSoundAtTime;
    private int lastSecondPlayed = -1;
    protected long muf = 0;
    SharedPreferences prefs;

    public class SHGTimer extends CountDownTimer {
        SHGTimer(long lDurationMillis, long lIntervalMillis) {
            super(lDurationMillis, lIntervalMillis);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            muf = millisUntilFinished;
            txtTimeLeft.setText(displayMillis(millisUntilFinished));
            if (!isTicking && millisUntilFinished < TEN_SECONDS) {
                isTicking = true;
                if (resTickingSound > 0) {
                    playSound(resTickingSound);
                }
                playerForeground.setLooping(true);
            }
            int seconds = (int)(millisUntilFinished / 1000L);
            if (seconds != lastSecondPlayed && resSoundAtTime.get(seconds) > 0) {
                lastSecondPlayed = seconds;
                playSound(resSoundAtTime.get(seconds));
            }
        }

        @Override
        public void onFinish() {
            btnStartStop.setEnabled(false);
            timer.cancel();
            timer = null;
            if (resFinishedSound > 0) {
                playSound(resFinishedSound);
            }
        }
    }

    public void setDuration(int dur) {
        duration = dur;
    }

    public int getDuration() {
        return duration;
    }

    public void addDuration(int dur) {
        if (timer != null) {  // Timer in progress
            timer.cancel();
            timer = null;
            playerForeground.stop();
            lastSecondPlayed = -1;
            long lDurationMillis = muf + (1000L * dur);
            timer = new SHGTimer(lDurationMillis, 500L);
            timer.start();
        } else if (muf == 0L) {
            duration += dur;
            txtTimeLeft.setText(displayMillis(1000L * duration));
        } else {
            muf += 1000L * dur;
            txtTimeLeft.setText(displayMillis(muf));
        }
    }

    public void setTickingSound(int res) {
        resTickingSound = res;
    }

    public void setFinishSound(int res) {
        resFinishedSound = res;
    }

    public void setBackgroundSound(int res) {
        resBackgroundSound = res;
    }

    protected void setBackgroundVolume(float vol) {
        playerBackground.setVolume(vol, vol);
    }

    public void setSoundAtTime(int res, int secs) {
        resSoundAtTime.put(secs, res);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerForeground = new MediaPlayer();
        isTicking = false;
        prefs = getSharedPreferences("defaults", Context.MODE_PRIVATE);
        resSoundAtTime = new SparseIntArray();
    }

    protected void playBackgroundSound() {
        Log.d(TAG, "playBackgroundSound()");
        if (resBackgroundSound > 0) {
            Log.d(TAG, "Playing backgroundSound " + resBackgroundSound);
            playerBackground = MediaPlayer.create(this, resBackgroundSound);
            playerBackground.setLooping(true);
            playerBackground.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean playSound = prefs.getBoolean("backgroundSound", false);
        if (playSound) {
            playBackgroundSound();
        } else {
            if (playerBackground != null) {
                playerBackground.stop();
                playerBackground.release();
                playerBackground = null;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playerBackground != null) {
            playerBackground.stop();
            playerBackground.release();
            playerBackground = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        playerForeground.stop();
        playerForeground.release();
        if (playerBackground != null) {
            playerBackground.stop();
            playerBackground.release();
        }
        super.onDestroy();
    }

    public void clickedStart(View btn) {
        Log.d(TAG, "clickedStart()");
        if (timer != null) {  // Running
            timer.cancel();
            timer = null;
            btnStartStop.setText(R.string.resume);
            isTicking = false;
            playerForeground.stop();
        } else {  // Stopped
            lastSecondPlayed = -1;
            btnStartStop.setText(R.string.pause);
            long lDurationMillis = (muf > 0) ? muf : 1000L * duration;
            timer = new SHGTimer(lDurationMillis, 500L);
            timer.start();
        }
    }

    protected void reset() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        lastSecondPlayed = -1;
        btnStartStop.setEnabled(true);
        btnStartStop.setText(R.string.start);
        long lDurationMillis = 1000L * duration;
        muf = 0L;
        txtTimeLeft.setText(displayMillis(lDurationMillis));
        isTicking = false;
        playerForeground.stop();
    }

    public void clickedReset(View btn) {
        Log.d(TAG, "clickedReset()");
        reset();
    }

    protected String displayMillis(long lMillis) {
        lMillis /= 1000L;
        long minutes = lMillis / 60L;
        long seconds = lMillis % 60L;
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    protected void playSound(int resId) {
        Uri uri = Uri.parse("android.resource://com.strongholdgames.timer/" + resId);
        playerForeground.reset();
        playerForeground.setLooping(false);
        playerForeground.setOnCompletionListener(null);
        try {
            playerForeground.setDataSource(this, uri);
            playerForeground.prepare();
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            return;
        }
        playerForeground.start();
    }
}
