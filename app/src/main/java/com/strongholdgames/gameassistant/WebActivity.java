package com.strongholdgames.timer;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebActivity extends Activity {
    private static final String TAG = "SHG_" + WebActivity.class.getSimpleName();
    Button btnBack;
    Button btnForward;
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        wv = findViewById(R.id.wvSHG);
        wv.setWebViewClient(new SHGWebViewClient());
        btnBack = findViewById(R.id.btnBack);
        btnForward = findViewById(R.id.btnForward);
        btnBack.setEnabled(false);
        btnForward.setEnabled(false);
        wv.loadUrl("http://www.strongholdgames.com/exclusive/");
    }

    private class SHGWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            checkButtons();
        }
    }

    public void checkButtons() {
        btnBack.setEnabled(wv.canGoBack());
        btnForward.setEnabled(wv.canGoForward());
    }

    public void clickedBack(View btn) {
        wv.goBack();
        checkButtons();
    }

    public void clickedForward(View btn) {
        wv.goForward();
        checkButtons();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "Back!");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
