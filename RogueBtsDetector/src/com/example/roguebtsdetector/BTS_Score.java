package com.example.roguebtsdetector;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BTS_Score extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bts_score);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bts_score, menu);
        return true;
    }
}
