package com.strongholdgames.timer;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

public class GoingGoingGoneActivity extends Activity {
    private static final String TAG = "SHG_" + GoingGoingGoneActivity.class.getSimpleName();
    enum TimerState { TIMER_RESET, TIMER_STOPPED, TIMER_RUNNING }
    enum Speed { SLOW, MEDIUM, FAST, RANDOM }
    protected GGGTimer timer;
    private Button btnStop;
    private ToggleButton btnSlow;
    private ToggleButton btnMedium;
    private ToggleButton btnFast;
    private ToggleButton btnRandom;
    private TextView txtCountdown;
    private TimerState timerState;
    private Speed selectedSpeed;
    private SoundPool player;
    private int[] countId;
    private int interval;
    private Random rand = new Random();

    public class GGGTimer extends CountDownTimer {
        int count;

        GGGTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            count = 20;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (count % 2 == 0) {
                txtCountdown.setVisibility(View.VISIBLE);
                int soundNum = count / 2;
                player.play(countId[soundNum], (float)1.0, (float)1.0, 0, 0, (float)1.0);
                Log.d(TAG, "Playing sound #" + soundNum);
            } else {
                txtCountdown.setVisibility(View.INVISIBLE);
            }
            --count;
        }

        @Override
        public void onFinish() {
            Log.d(TAG, "Timer finished");
            timer.cancel();
            timer = null;
            timerState = TimerState.TIMER_STOPPED;
            txtCountdown.setText(R.string.gone);
            player.play(countId[0], (float)1.0, (float)1.0, 0, 0, (float)1.0);
            updateDisplay();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ggg);
        btnStop = findViewById(R.id.btnStop);
        btnSlow = findViewById(R.id.btnSlow);
        btnMedium = findViewById(R.id.btnMedium);
        btnFast = findViewById(R.id.btnFast);
        btnRandom = findViewById(R.id.btnRandom);
        txtCountdown = findViewById(R.id.txtCountdown);
        timerState = TimerState.TIMER_RESET;
        selectedSpeed = Speed.SLOW;
        player = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        countId = new int[11];
        int[] resources = {
                R.raw.gavel1,
                R.raw.one,
                R.raw.two,
                R.raw.three,
                R.raw.four,
                R.raw.five,
                R.raw.six,
                R.raw.seven,
                R.raw.eight,
                R.raw.nine,
                R.raw.ten};
        for (int i = 0; i < resources.length; ++i) {
            countId[i] = player.load(this, resources[i], 1);
        }
        interval = 500;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        updateDisplay();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        player.release();
        super.onDestroy();
    }

    public void stopClicked(View btn) {
        switch (timerState) {
            case TIMER_RESET:
                Log.d(TAG, "stopClicked(): Starting timer");
                timerState = TimerState.TIMER_RUNNING;
                startTimer();
                break;

            case TIMER_STOPPED:
                Log.d(TAG, "stopClicked(): Reset");
                timerState = TimerState.TIMER_RESET;
                break;

            case TIMER_RUNNING:
                Log.d(TAG, "stopClicked(): Stopping timer");
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timerState = TimerState.TIMER_STOPPED;
                break;
        }
        updateDisplay();
    }

    public void speedClicked(View view) {
        ToggleButton btn = (ToggleButton)view;
        btnSlow.setChecked(false);
        btnMedium.setChecked(false);
        btnFast.setChecked(false);
        btnRandom.setChecked(false);

        switch (btn.getId()) {
            case R.id.btnSlow:
                Log.d(TAG, "speedClicked(): SLOW");
                selectedSpeed = Speed.SLOW;
                interval = 500;
                btnSlow.setChecked(true);
                break;

            case R.id.btnMedium:
                Log.d(TAG, "speedClicked(): MEDIUM");
                selectedSpeed = Speed.MEDIUM;
                interval = 333;
                btnMedium.setChecked(true);
                break;

            case R.id.btnFast:
                Log.d(TAG, "speedClicked(): FAST");
                selectedSpeed = Speed.FAST;
                interval = 167;
                btnFast.setChecked(true);
                break;

            case R.id.btnRandom:
                Log.d(TAG, "speedClicked(): RANDOM");
                selectedSpeed = Speed.RANDOM;
                btnRandom.setChecked(true);
                break;
        }
    }

    private void startTimer() {
        if (selectedSpeed == Speed.RANDOM) {
            interval = rand.nextInt(351) + 150;
        }
        Log.d(TAG, "Starting timer, interval = " + interval);
        txtCountdown.setText(R.string.going);
        txtCountdown.setVisibility(View.VISIBLE);
        timer = new GGGTimer((20 * interval) + 100, interval);
        timer.start();
    }

    private void updateDisplay() {
        btnStop.setText(
                timerState == TimerState.TIMER_RESET ? R.string.start :
                        timerState == TimerState.TIMER_RUNNING ? R.string.stop :
                                R.string.reset
        );
        txtCountdown.setVisibility(timerState == TimerState.TIMER_RESET
                ? View.INVISIBLE
                : View.VISIBLE);
        btnFast.setVisibility(timerState == TimerState.TIMER_RESET
                ? View.VISIBLE
                : View.INVISIBLE);
        btnMedium.setVisibility(timerState == TimerState.TIMER_RESET
                ? View.VISIBLE
                : View.INVISIBLE);
        btnSlow.setVisibility(timerState == TimerState.TIMER_RESET
                ? View.VISIBLE
                : View.INVISIBLE);
        btnRandom.setVisibility(timerState == TimerState.TIMER_RESET
                ? View.VISIBLE
                : View.INVISIBLE);
    }
}
