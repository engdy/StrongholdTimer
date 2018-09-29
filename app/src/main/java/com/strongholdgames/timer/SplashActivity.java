package com.strongholdgames.timer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SHG_" + SplashActivity.class.getSimpleName();
    boolean active = true;
    int splashTime = 5000;
    Thread splashThread;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setOnClickListener(this);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Log.d(TAG, "Density = " + metrics.density);
        Log.d(TAG, "DPI = " + metrics.densityDpi);
        Log.d(TAG, "wdp = " + (int)(metrics.widthPixels / metrics.density));
        Log.d(TAG, "hdp = " + (int)(metrics.heightPixels / metrics.density));
        RelativeLayout layout = findViewById(R.id.layout);
        Log.d(TAG, "Layout = " + layout.getTag());

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (active && waited < splashTime) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    Log.d(TAG, "Interrupted");
                } finally {
                    finish();
                    startActivity(new Intent(getApplicationContext(), GameSelectActivity.class));
                }
            }
        };
        splashThread.start();
    }

    @Override
    public void onClick(View view) {
        splashThread.interrupt();
    }
}
