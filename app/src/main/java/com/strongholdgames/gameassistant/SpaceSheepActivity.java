package com.strongholdgames.timer;

import java.io.IOException;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SpaceSheepActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "SHG_" + SpaceSheepActivity.class.getSimpleName();
    private static final int INTERVAL = 200;
    private Button btnWolfFreq;
    private Button btnStart;
    private Button btnAddDefense;
    private Button btnPause;
    private Button btnDefend;
    private SeekBar sbWolfStrength;
    private TextView txtClock;
    private TextView txtWolfStrength;
    private TextView txtWolfStrengthLabel;
    private TextView txtDefenseConsoleLabel;
    private TextView txtDefenseConsole;
    private TextView txtPressOnly;
    private int wolfStrength = 4;
    private int wolfTime = 60;
    private int lastDieRoll = 0;
    private int shieldStrength = 0;
    private long durationMillis = 0;
    private long expireMillis = 0;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private Random rand = new Random();
    private Handler handler;
    private AlertDialog alertDialog = null;
    private MediaPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_spacesheep);
        btnWolfFreq = findViewById(R.id.btnWolfFreq);
        btnStart = findViewById(R.id.btnStart);
        btnAddDefense = findViewById(R.id.btnAddDefense);
        btnPause = findViewById(R.id.btnPause);
        btnDefend = findViewById(R.id.btnDefend);
        sbWolfStrength = findViewById(R.id.sbWolfStrength);
        sbWolfStrength.setOnSeekBarChangeListener(this);
        txtClock = findViewById(R.id.txtClock);
        txtWolfStrength = findViewById(R.id.txtWolfStrength);
        txtWolfStrengthLabel = findViewById(R.id.txtWolfStrengthLabel);
        txtDefenseConsoleLabel = findViewById(R.id.txtDefenseConsoleLabel);
        txtDefenseConsole = findViewById(R.id.txtDefenseConsole);
        txtPressOnly = findViewById(R.id.txtPressOnlyIf);
        isRunning = false;
        shieldStrength = 0;
        handler = new Handler();
        soundPlayer = new MediaPlayer();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(timerTick);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sbWolfStrength.setProgress(wolfStrength);
        txtClock.setText(displayMillis(wolfTime * 1000));
        updateDisplay();
    }

    public void clickedWolfFreq(View btn) {
        Intent i = new Intent(this, TimePickerActivity.class);
        i.putExtra("seconds", wolfTime);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent results) {
        super.onActivityResult(requestCode, resultCode, results);
        if (resultCode == RESULT_OK) {
            wolfTime = results.getIntExtra("seconds", 60);
        }
    }

    public void clickedStart(View btn) {
        isRunning = true;
        durationMillis = wolfTime * 1000;
        btnPause.setText(R.string.pause);
        updateDisplay();
        showDieRollMessage();
        expireMillis = SystemClock.elapsedRealtime() + durationMillis;
        handler.removeCallbacks(timerTick);
        handler.postDelayed(timerTick, INTERVAL);
    }

    private Runnable timerTick = new Runnable() {
        @Override
        public void run() {
            if (!isPaused) {
                durationMillis = expireMillis - SystemClock.elapsedRealtime();
                if (durationMillis <= 0) {
                    durationMillis = 0;
                    int numCards = wolfStrength - shieldStrength;
                    shieldStrength -= wolfStrength;
                    if (shieldStrength < 0) {
                        shieldStrength = 0;
                    }
                    defendAndDiscardCard(numCards);
                    updateDisplay();
                    playSound(R.raw.wolf);
                }
                txtClock.setText(displayMillis(durationMillis));
            }
            handler.postDelayed(timerTick, INTERVAL);
        }
    };

    protected void playSound(int resId) {
        Uri uri = Uri.parse("android.resource://com.strongholdgames.timer/" + resId);
        soundPlayer.reset();
        soundPlayer.setLooping(false);
        soundPlayer.setOnCompletionListener(null);
        try {
            soundPlayer.setDataSource(this, uri);
            soundPlayer.prepare();
        } catch (IOException e) {
            Log.e(TAG, "Unable to play sound " + resId + ": " + e.getMessage());
            return;
        }
        soundPlayer.start();
    }

    public void clickedAddDefense(View btn) {
        ++shieldStrength;
        updateDisplay();
    }

    public void clickedPauseResume(View btn) {
        isPaused = !isPaused;
        if (isPaused) {
            btnPause.setText(R.string.resume);
        } else {
            btnPause.setText(R.string.pause);
            expireMillis = SystemClock.elapsedRealtime() + durationMillis;
        }
        updateDisplay();
    }

    public void clickedDefend(View btn) {
        defendAndDiscardCard(0);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        wolfStrength = progress;
        String wsText = Integer.toString(wolfStrength);
        txtWolfStrength.setText(wolfStrength == 10 ? "Infinite" : wsText);
        txtClock.setText(displayMillis(wolfTime * 1000));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    protected String displayMillis(long lMillis) {
        lMillis /= 1000L;
        long minutes = lMillis / 60L;
        long seconds = lMillis % 60L;
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    private void updateDisplay() {
        btnStart.setVisibility(isRunning ? View.INVISIBLE : View.VISIBLE);
        sbWolfStrength.setVisibility(isRunning ? View.INVISIBLE : View.VISIBLE);
        btnWolfFreq.setVisibility(isRunning ? View.INVISIBLE : View.VISIBLE);
        txtWolfStrength.setVisibility(isRunning ? View.INVISIBLE : View.VISIBLE);
        txtWolfStrengthLabel.setVisibility(isRunning ? View.INVISIBLE : View.VISIBLE);
        btnAddDefense.setVisibility(isRunning ? View.VISIBLE : View.INVISIBLE);
        btnAddDefense.setEnabled(!isPaused);
        txtDefenseConsoleLabel.setVisibility(isRunning ? View.VISIBLE : View.INVISIBLE);
        txtDefenseConsole.setVisibility(isRunning ? View.VISIBLE : View.INVISIBLE);
        txtPressOnly.setVisibility(isRunning ? View.VISIBLE : View.INVISIBLE);
        btnPause.setVisibility(isRunning ? View.VISIBLE : View.INVISIBLE);
        btnDefend.setVisibility(isRunning ? View.VISIBLE : View.INVISIBLE);
        btnDefend.setEnabled(!isPaused);
        txtDefenseConsole.setText(Integer.toString(shieldStrength));
    }

    private void defendAndDiscardCard(int numberOfCards) {
        if (numberOfCards > 0) {
            showDieRollMessageWithTitle("Attacked!", "Discard " + numberOfCards + " cards");
        } else {
            showDieRollMessage();
        }
        expireMillis = SystemClock.elapsedRealtime() + (wolfTime * 1000);
        if (durationMillis > 0) {
            expireMillis -= durationMillis;
        }
    }

    private void showDieRollMessageWithTitle(String title, String additionalMessage) {
        lastDieRoll = rand.nextInt(8) + 1;
        String message = "Move wolf " + lastDieRoll + " space";
        if (lastDieRoll > 1) {
            message += "s";
        }
        if (additionalMessage != null) {
            message += "\n" + additionalMessage;
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", null);
        alertDialog = builder.create();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                alertDialog = null;
            }
        });
        alertDialog.show();
    }

    private void showDieRollMessage() {
        showDieRollMessageWithTitle("Move Wolf", null);
    }
}
