package com.strongholdgames.gameassistant;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SpaceCadetsActivity extends TimerActivity {
    private static final String TAG = "SHG_" + SpaceCadetsActivity.class.getSimpleName();
    private static final int THREE_FOUR = 1;
    private static final int FIVE_MORE = 0;
    private static final int WITHOUT_SCIENCE = 0;
    private static final int WITH_SCIENCE = 1;
    int numPlayers = FIVE_MORE;
    int withScience = WITHOUT_SCIENCE;
    int currentStep = 0;
    int currentNemesis = -1;
    Button btnNext;
    Button btnPrev;
    Button btnBeginMission;
    Button btnViewTutorials;
    Button btnCommBreak;
    TextView txtNemesisInstruct;
    TextView txtNotTimed;
    ImageView imgSC;
    boolean isShowBeginMission = false;
    boolean isBeginMission = false;
    boolean isCommBreak = false;
    BitmapDrawable drSpaceCadets;
    ImageView imgStep;
    int[][][] imgSteps = {
            {
                    {
                            R.drawable.sc_step_5_1,
                            R.drawable.sc_step_5_2,
                            R.drawable.sc_step_5_3,
                            R.drawable.sc_step_5_4,
                            R.drawable.sc_step_5_5,
                            R.drawable.sc_step_5_6,
                            R.drawable.sc_step_5_7,
                            R.drawable.sc_step_5_8,
                            R.drawable.sc_step_5_9
                    },
                    {
                            R.drawable.sc_step_34_0,
                            R.drawable.sc_step_34_1,
                            R.drawable.sc_step_34_2,
                            R.drawable.sc_step_34_3,
                            R.drawable.sc_step_34_4,
                            R.drawable.sc_step_34_5,
                            R.drawable.sc_step_34_6,
                            R.drawable.sc_step_34_7,
                            R.drawable.sc_step_34_8,
                            R.drawable.sc_step_34_9
                    }
            },
            {
                    {
                            R.drawable.sc_step_5_1,
                            R.drawable.sc_step_1_2,
                            R.drawable.sc_step_5_2,
                            R.drawable.sc_step_5_3,
                            R.drawable.sc_step_5_4,
                            R.drawable.sc_step_5_5,
                            R.drawable.sc_step_5_6,
                            R.drawable.sc_step_5_7,
                            R.drawable.sc_step_5_8,
                            R.drawable.sc_step_5_9
                    },
                    {
                            R.drawable.sc_step_34_0,
                            R.drawable.sc_step_1_2,
                            R.drawable.sc_step_34_1,
                            R.drawable.sc_step_34_2,
                            R.drawable.sc_step_34_3,
                            R.drawable.sc_step_34_4,
                            R.drawable.sc_step_34_5,
                            R.drawable.sc_step_34_6,
                            R.drawable.sc_step_34_7,
                            R.drawable.sc_step_34_8,
                            R.drawable.sc_step_34_9
                    }
            }
    };
    int[][][] stepDuration = {
            {
                    {
                            180, 0, 30, 0, 30, 30, 0, 30, 30
                    },
                    {
                            180, 0, 30, 30, 0, 30, 30, 0, 30, 30
                    }
            },
            {
                    {
                            180, 30, 0, 30, 0, 30, 30, 0, 30, 30
                    },
                    {
                            180, 30, 0, 30, 30, 0, 30, 30, 0, 30, 30
                    }
            }
    };
    int[] imgNemesis = {
            R.drawable.sc_step_34_7a,
            R.drawable.sc_step_34_7b,
            R.drawable.sc_step_34_7c,
            R.drawable.sc_step_34_7d,
            R.drawable.sc_step_34_7e,
            R.drawable.sc_step_34_7f,
            R.drawable.sc_step_34_7g,
            R.drawable.sc_step_34_7h,
            R.drawable.sc_step_34_7i
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_space_cadets);
        btnStartStop = findViewById(R.id.btnStart);
        btnStartStop.setEnabled(true);
        btnStartStop.setText(R.string.start);
        btnBeginMission = findViewById(R.id.btnBeginMission);
        btnViewTutorials = findViewById(R.id.btnViewTutorials);
        btnCommBreak = findViewById(R.id.btnCommBreak);
        btnReset = findViewById(R.id.btnReset);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        imgSC = findViewById(R.id.imgSC);
        drSpaceCadets = (BitmapDrawable)imgSC.getDrawable();
        imgStep = findViewById(R.id.imgStep);
        imgStep.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!isNemesisStep()) {
                        return true;
                    }
                    int numButton = (int) (10.0 * event.getX() / v.getWidth());
                    Log.d(TAG, "numButton = " + numButton);
                    if (numButton > 0) {
                        currentNemesis = numButton - 1;
                        if (currentNemesis >= imgNemesis.length) {
                            currentNemesis = imgNemesis.length - 1;
                        }
                        reset();
                    }
                }
                return true;
            }
        });
        txtTimeLeft = findViewById(R.id.txtTime);
        txtNemesisInstruct = findViewById(R.id.txtNemesisInstruct);
        txtNotTimed = findViewById(R.id.txtNotTimed);
        final Context context = this;
        final SharedPreferences prefs = context.getSharedPreferences("defaults", 0);
        numPlayers = prefs.getInt("numPlayers", THREE_FOUR);
        withScience = prefs.getInt("withScience", WITHOUT_SCIENCE);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.start_prompt);
        dialog.setTitle(R.string.configuration);
        final ToggleButton tbPlayerCount = dialog.findViewById(R.id.tbPlayerCount);
        final ToggleButton tbScience = dialog.findViewById(R.id.tbScience);
        tbPlayerCount.setChecked(numPlayers == FIVE_MORE);
        tbScience.setChecked(withScience == WITH_SCIENCE);
        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPlayers = (tbPlayerCount.isChecked()) ? FIVE_MORE : THREE_FOUR;
                withScience = (tbScience.isChecked()) ? WITH_SCIENCE : WITHOUT_SCIENCE;
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt("numPlayers", numPlayers);
                edit.putInt("withScience", withScience);
                edit.apply();
                currentStep = nemesisStep();
                reset();
                dialog.dismiss();
            }
        });
        dialog.show();
        setDuration(stepDuration[0][0][0]);
        setTickingSound(R.raw.ticking);
        setFinishSound(R.raw.buzzer);
        setBackgroundSound(R.raw.sc_background);
        reset();
    }

    private void nemesisReset() {
        Log.d(TAG, "nemesisReset()");
        super.reset();
        currentStep = nemesisStep();
        imgStep.setImageResource(imgSteps[withScience][numPlayers][currentStep]);
        btnStartStop.setEnabled(false);
        btnStartStop.setVisibility(View.INVISIBLE);
        btnReset.setEnabled(false);
        btnReset.setVisibility(View.INVISIBLE);
        btnCommBreak.setEnabled(false);
        btnCommBreak.setVisibility(View.INVISIBLE);
        btnNext.setEnabled(false);
        btnPrev.setEnabled(false);
        btnBeginMission.setEnabled(false);
        btnBeginMission.setVisibility(View.INVISIBLE);
        btnViewTutorials.setEnabled(true);
        btnViewTutorials.setVisibility(View.VISIBLE);
        txtTimeLeft.setVisibility(View.INVISIBLE);
        txtNemesisInstruct.setVisibility(View.VISIBLE);
        txtNotTimed.setVisibility(View.INVISIBLE);
    }

    private void showBeginMission() {
        Log.d(TAG, "showBeginMission()");
        super.reset();
        imgStep.setImageResource(imgNemesis[currentNemesis]);
        isShowBeginMission = true;
        btnBeginMission.setBackgroundResource(R.drawable.bluebutton_gradient);
        btnBeginMission.setEnabled(true);
        btnBeginMission.setVisibility(View.VISIBLE);
    }

    private void updateSCImage() {
        Log.d(TAG, "updateSCImage()");
        Bitmap bmOrig = drSpaceCadets.getBitmap();
        Bitmap bmMerge = Bitmap.createBitmap(bmOrig.getWidth(), bmOrig.getHeight(), bmOrig.getConfig());
        Canvas canvas = new Canvas(bmMerge);
        canvas.drawBitmap(bmOrig, new Matrix(), null);
        Paint paint = new Paint();
        switch (currentNemesis) {
            case 0:
            case 1:
                paint.setARGB(229, 26, 113, 58);
                break;
            case 2:
                paint.setARGB(229, 168, 9, 18);
                break;
            case 3:
            case 4:
            case 5:
                paint.setARGB(229, 187, 151, 10);
                break;
            default:
                paint.setARGB(229, 10, 123, 170);
        }
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int width = bmOrig.getWidth();
        int height = bmOrig.getHeight();
        int bannerTop = (height * 4) / 5;
        canvas.drawRect(new Rect(0, bannerTop, width, height), paint);
        String sNem = "Nemesis: " + (currentNemesis - 2);
        paint.setARGB(255, 255, 255, 255);
        paint.setTextAlign(Paint.Align.CENTER);
        int scaledSize = getResources().getDimensionPixelSize(R.dimen.nemfontsize);
        paint.setTextSize(scaledSize);
        Rect bounds = new Rect();
        paint.getTextBounds(sNem, 0, sNem.length(), bounds);
        int textHeight = bounds.bottom - bounds.top;
        int textX = width / 2;
        int textY = (height - bannerTop + textHeight) / 2 + bannerTop;
        canvas.drawText(sNem, textX, textY, paint);
        imgSC.setImageBitmap(bmMerge);
    }

    @Override
    protected void reset() {
        Log.d(TAG, "reset()");
        if (currentNemesis < 0) {
            nemesisReset();
            return;
        }
        if (!isShowBeginMission) {
            showBeginMission();
            return;
        }

        setDuration(stepDuration[withScience][numPlayers][currentStep]);

        //defaults
        txtTimeLeft.setTextColor(Color.WHITE);
        btnCommBreak.setEnabled(false);
        btnCommBreak.setVisibility(View.INVISIBLE);

        if (isDiscussStep()) {
            if (isCommBreak) {
                btnCommBreak.setBackgroundResource(R.drawable.orangebutton_gradient);
                txtTimeLeft.setTextColor(Color.parseColor("#E25326"));
                setDuration(120);
            } else {
                btnCommBreak.setBackgroundResource(R.drawable.blackbutton_gradient);
            }
            btnCommBreak.setText(R.string.commbreakdown);
            btnCommBreak.setEnabled(true);
            btnCommBreak.setVisibility(View.VISIBLE);
        } else if (isJumpStep()) {
            btnCommBreak.setEnabled(true);
            btnCommBreak.setVisibility(View.VISIBLE);
            btnCommBreak.setText(R.string.plus30);
            btnCommBreak.setBackgroundResource(R.drawable.blackbutton_gradient);
        }

        if (isNemesisStep() && currentNemesis >= 0) {
            imgStep.setImageResource(imgNemesis[currentNemesis]);
            updateSCImage();
        } else {
            imgStep.setImageResource(imgSteps[withScience][numPlayers][currentStep]);
        }

        if (!isBeginMission) {
            return;
        }

        super.reset();

        if (stepDuration[withScience][numPlayers][currentStep] > 0) {
            txtTimeLeft.setVisibility(View.VISIBLE);
            txtNotTimed.setVisibility(View.INVISIBLE);
            btnReset.setEnabled(true);
            btnReset.setVisibility(View.VISIBLE);
            btnStartStop.setEnabled(true);
            btnStartStop.setText(R.string.start);
            btnStartStop.setBackgroundResource(R.drawable.blackbutton_gradient);
        } else {
            txtTimeLeft.setVisibility(View.INVISIBLE);
            txtNotTimed.setVisibility(View.VISIBLE);
            btnReset.setEnabled(false);
            btnReset.setVisibility(View.INVISIBLE);
            btnStartStop.setEnabled(true);
            btnStartStop.setText(R.string.next);
            btnStartStop.setBackgroundResource(R.drawable.bluebutton_gradient);
        }
    }

    public void clickedPrev(View btn) {
        Log.d(TAG, "clickedPrev()");
        if (--currentStep < 0) {
            currentStep = stepDuration[withScience][numPlayers].length - 1;
        }
        reset();
    }

    public void clickedNext(View btn) {
        Log.d(TAG, "clickedNext()");
        if (++currentStep >= stepDuration[withScience][numPlayers].length) {
            currentStep = 0;
        }
        reset();
    }

    public void clickedBeginMission(View btn) {
        Log.d(TAG, "clickedBeginMission()");
        isBeginMission = true;
        currentStep = 0;
        btnStartStop.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.VISIBLE);
        btnNext.setEnabled(true);
        btnPrev.setEnabled(true);
        btnBeginMission.setEnabled(false);
        btnBeginMission.setVisibility(View.INVISIBLE);
        txtTimeLeft.setVisibility(View.VISIBLE);
        txtNemesisInstruct.setVisibility(View.INVISIBLE);
        btnViewTutorials.setEnabled(false);
        btnViewTutorials.setVisibility(View.INVISIBLE);
        reset();
    }

    @Override
    public void clickedStart(View btn) {
        Log.d(TAG, "clickedStart()");
        if (stepDuration[withScience][numPlayers][currentStep] > 0) {
            super.clickedStart(btn);
        } else {
            clickedNext(btn);
        }
    }

    private boolean isNemesisStep() {
        return currentStep == nemesisStep();
    }

    private int nemesisStep() {
        return 6 + withScience + numPlayers;
    }

    private boolean isJumpStep() {
        return currentStep == jumpStep();
    }

    private int jumpStep() {
        return 7 + withScience + numPlayers;
    }

    private boolean isDiscussStep() {
        return currentStep == 0;
    }

    public void clickedTutorials(View btn) {
        Log.d(TAG, "clickedTutorials");
        Intent i = new Intent(this, TutorialActivity.class);
        startActivity(i);
    }

    public void clickedCommBreak(View btn) {
        Log.d(TAG, "clickedCommBreak()");
        if (currentStep == 0) { // Discussion step - Comm breakdown button
            if (isCommBreak) {
                isCommBreak = false;
                txtTimeLeft.setTextColor(Color.WHITE);
            } else {
                isCommBreak = true;
                txtTimeLeft.setTextColor(Color.parseColor("#E25326"));
            }
            reset();
        } else { // Jump step - +30 sec button
            addDuration(30);
        }
    }
}
