package com.strongholdgames.gameassistant;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class GenericTimerActivity extends TimerActivity {
    private static final String TAG = "SHG_" + GenericTimerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_generic_timer);
        btnStartStop = findViewById(R.id.btnStart);
        btnStartStop.setEnabled(true);
        btnStartStop.setText(R.string.start);
        btnReset = findViewById(R.id.btnReset);
        txtTimeLeft = findViewById(R.id.txtTime);
        setDuration(300);
        setTickingSound(R.raw.ticking);
        setFinishSound(R.raw.buzzer);
        setBackgroundSound(R.raw.generic_background);
        reset();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent results) {
        Log.d(TAG, "onActivityResult(" + requestCode + ", " + resultCode + ", " + results + ")");
        super.onActivityResult(requestCode, resultCode, results);
        if (resultCode == RESULT_OK) {
            int seconds = results.getIntExtra("seconds", 300);
            if (seconds >= 1) {
                Log.d(TAG, "seconds = " + seconds);
                setDuration(seconds);
            } else {
                Log.d(TAG, "Ignoring seconds <= 0");
            }
        }
        reset();
    }

    public void clickedSetTime(View btn) {
        Log.d(TAG, "clickedSetTime");
        int seconds = getDuration();
        Intent i = new Intent(this, TimePickerActivity.class);
        i.putExtra("seconds", seconds);
        Log.d(TAG, "Setting " + seconds + " seconds");
        startActivityForResult(i, 0);
    }
}
