package com.strongholdgames.timer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TimeNSpaceActivity extends TimerActivity {
    private static final String TAG = "SHG_" + TimeNSpaceActivity.class.getSimpleName();
    private Button btnTimerOne;
    private Button btnStartGameClock;
    private TNSTimer timerOne;
    private TNSTimer timerTwo;

    class TNSTimer extends CountDownTimer {
        private Button myButton;

        public TNSTimer(Button btn) {
            super(60_000, 250);
            myButton = btn;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            myButton.setText(displayMillis(millisUntilFinished));
        }

        @Override
        public void onFinish() {
            myButton.setEnabled(true);
            myButton.setText(R.string.done);
            playSound(R.raw.buzzer);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_timenspace);
        btnTimerOne = findViewById(R.id.btnTimerOne);
        btnStartGameClock = findViewById(R.id.btnStartGameClock);
        txtTimeLeft = findViewById(R.id.txtTimeLeft);
        txtTimeLeft.setVisibility(View.GONE);
        btnStartGameClock.setVisibility(View.VISIBLE);
        btnStartStop = findViewById(R.id.btnStartStop);
    }

    @Override
    protected void onDestroy() {
        if (timerOne != null) {
            timerOne.cancel();
        }
        if (timerTwo != null) {
            timerTwo.cancel();
        }
        super.onDestroy();
    }

    public void clickedStartGameClock(View btn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.startgameclock);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.shorttimername, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDuration(720);
                startGameTimer(R.raw.tns12minute);
                dialog.cancel();
            }
        });
        builder.setNeutralButton(R.string.longtimername, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDuration(1800);
                startGameTimer(R.raw.tns30minute);
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startGameTimer(int soundtrackID) {
        btnStartGameClock.setVisibility(View.GONE);
        txtTimeLeft.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences("defaults", Context.MODE_PRIVATE);
        if (prefs.getBoolean("backgroundSound", false)) {
            setBackgroundSound(soundtrackID);
            playBackgroundSound();
        }
        clickedStart(null);
    }

    public void clickedTimer(View btn) {
        TNSTimer myTimer;
        Log.d(TAG, "Clicked timer");
        if (btn == btnTimerOne) {
            timerOne = new TNSTimer((Button)btn);
            myTimer = timerOne;
        } else {
            timerTwo = new TNSTimer((Button)btn);
            myTimer = timerTwo;
        }
        btn.setEnabled(false);
        myTimer.start();
    }
}
