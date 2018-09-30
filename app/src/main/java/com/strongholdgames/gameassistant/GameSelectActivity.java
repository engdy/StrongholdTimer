package com.strongholdgames.gameassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GameSelectActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SHG_" + GameSelectActivity.class.getSimpleName();
    private TextView txtPromotion;
    private ToggleButton btnSoundtrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_select);
        txtPromotion = findViewById(R.id.txtPromotion);
        txtPromotion.setOnClickListener(this);
        btnSoundtrack = findViewById(R.id.btnSoundtrack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        updatePromotion();
        SharedPreferences prefs = getSharedPreferences("defaults", Context.MODE_PRIVATE);
        boolean isSoundtrack = prefs.getBoolean("backgroundSound", false);
        Log.d(TAG, "backgroundSound = " + isSoundtrack);
        btnSoundtrack.setChecked(isSoundtrack);
    }

    private void updatePromotion() {
        Thread th = new Thread(new Runnable() {
            private String promotion = "Thanks for your support of Stronghold Games!";

            @Override
            public void run() {
                URL shgurl;
                try {
                    shgurl = new URL("http://www.strongholdgames.com/exclusives/index.txt");
                    BufferedReader in = new BufferedReader(new InputStreamReader(shgurl.openStream()));
                    promotion = in.readLine();
                    in.close();
                } catch (MalformedURLException e) {
                    Log.e(TAG, "Malformed URL Exception: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                Log.d(TAG, "Promotion = '" + promotion + "'");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtPromotion.setText(promotion);
                    }
                });
            }
        });
        th.start();
    }

    public void clickedArticle27(View btn) {
        Log.d(TAG, "clickedArticle27");
        Intent i = new Intent(this, Article27Activity.class);
        startActivity(i);
    }

    public void clickedSpaceCadets(View btn) {
        Log.d(TAG, "clickedSpaceCadets");
        Intent i = new Intent(this, SpaceCadetsActivity.class);
        startActivity(i);
    }

    public void clickedSimpleTimer(View btn) {
        Log.d(TAG, "clickedSimpleTimer");
        Intent i = new Intent(this, GenericTimerActivity.class);
        startActivity(i);
    }

    public void clickedSpaceNTime(View btn) {
        Log.d(TAG, "clickedSpaceNTime");
        Intent i = new Intent(this, TimeNSpaceActivity.class);
        startActivity(i);
    }

    public void clickedSpaceSheep(View btn) {
        Log.d(TAG, "clickedSpaceSheep");
        Intent i = new Intent(this, SpaceSheepActivity.class);
        startActivity(i);
    }

    public void clickedGoingGone(View btn) {
        Log.d(TAG, "clickedGoingGone");
        Intent i = new Intent(this, GoingGoingGoneActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick()");
        Intent i = new Intent(this, WebActivity.class);
        startActivity(i);
    }

    public void clickedSoundtrack(View view) {
        ToggleButton btn = (ToggleButton)view;
        Log.d(TAG, "Turning background sound " + (btn.isChecked() ? "ON" : "OFF"));
        SharedPreferences prefs = getSharedPreferences("defaults", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("backgroundSound", btn.isChecked());
        edit.apply();
    }
}
