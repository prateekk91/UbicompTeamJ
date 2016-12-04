package com.example.marcel.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Marcel on 11/20/2016.
 */

public class Exercise2Activity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise2);
        Log.d("exercise2 selected", "Bicep Curl");
    }
}
