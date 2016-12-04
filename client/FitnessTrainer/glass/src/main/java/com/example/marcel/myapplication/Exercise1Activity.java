package com.example.marcel.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;

/**
 * Created by Marcel on 11/20/2016.
 */

public class Exercise1Activity extends Activity{

    WebView webView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String exercise = getIntent().getStringExtra("ExerciseName");
        setContentView(R.layout.activity_exercise1);
        Log.d("exercise1 selected", "Shoulder Press");
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setPadding(0, 0, 0, 0);
        webView.setInitialScale(getScale());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html?exerciseName=" + exercise);
    }

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(1280.0);
        val = val * 100d;
        return val.intValue();
    }
}