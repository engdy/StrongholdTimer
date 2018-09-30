package com.strongholdgames.gameassistant;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class TimePickerActivity extends Activity {
    private static final String TAG = "SHG_" + TimePickerActivity.class.getSimpleName();

    private Button btnPlusMinute;
    private Button btnMinusMinute;
    private Button btnPlusSecond;
    private Button btnMinusSecond;
    private EditText txtMinute;
    private EditText txtSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_time_picker);
        Bundle extras = getIntent().getExtras();
        int seconds = extras.getInt("seconds");
        btnPlusMinute = findViewById(R.id.btnPlusMinute);
        btnMinusMinute = findViewById(R.id.btnMinusMinute);
        btnPlusSecond = findViewById(R.id.btnPlusSecond);
        btnMinusSecond = findViewById(R.id.btnMinusSecond);
        txtMinute = findViewById(R.id.txtMinute);
        txtSecond = findViewById(R.id.txtSecond);
        txtMinute.setText(String.format("%02d", seconds / 60));
        txtSecond.setText(String.format("%02d", seconds % 60));
    }

    public void clickedOK(View btn) {
        Intent i = new Intent();
        int seconds = Integer.parseInt(txtMinute.getText().toString()) * 60 + Integer.parseInt(txtSecond.getText().toString());
        i.putExtra("seconds", seconds);
        setResult(RESULT_OK, i);
        finish();
    }

    public void clickedCancel(View btn) {
        setResult(RESULT_CANCELED, null);
        finish();
    }

    public void clickedPlus(View btn) {
        if (btn == btnPlusMinute) {
            int minutes = (Integer.parseInt(txtMinute.getText().toString()) + 1) % 60;
            txtMinute.setText(String.format("%02d", minutes));
        } else {
            int seconds = (Integer.parseInt(txtSecond.getText().toString()) + 1) % 60;
            txtSecond.setText(String.format("%02d", seconds));
        }
    }

    public void clickedMinus(View btn) {
        if (btn == btnMinusMinute) {
            int minutes = (Integer.parseInt(txtMinute.getText().toString()) + 59) % 60;
            txtMinute.setText(String.format("%02d", minutes));
        } else {
            int seconds = (Integer.parseInt(txtSecond.getText().toString()) + 59) % 60;
            txtSecond.setText(String.format("%02d", seconds));
        }
    }
}
