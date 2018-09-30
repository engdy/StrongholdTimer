package com.strongholdgames.gameassistant;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class Article27Activity extends TimerActivity {
    private static final String TAG = "SHG_" + Article27Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_article27);
        btnStartStop = findViewById(R.id.btnStart);
        btnStartStop.setEnabled(true);
        btnStartStop.setText(R.string.start);
        btnReset = findViewById(R.id.btnReset);
        txtTimeLeft = findViewById(R.id.txtTime);
        setDuration(300);
        setTickingSound(R.raw.ticking);
        setFinishSound(R.raw.buzzer);
        setSoundAtTime(R.raw.gavel5, 299);
        setSoundAtTime(R.raw.gavel4, 240);
        setSoundAtTime(R.raw.gavel3, 180);
        setSoundAtTime(R.raw.gavel2, 120);
        setSoundAtTime(R.raw.gavel1, 60);
        setSoundAtTime(R.raw.parliament, 10);
        setSoundAtTime(R.raw.gavel_final, 3);
        setBackgroundSound(R.raw.article27_background);
        reset();
    }
}
