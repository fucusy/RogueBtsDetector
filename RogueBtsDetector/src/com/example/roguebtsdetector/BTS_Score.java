package com.example.roguebtsdetector;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BTS_Score extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bts_score);
        
        Button buttonAbout = (Button) findViewById(R.id.button1);
        buttonAbout.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   // TODO: force recalculate, where is analysis class?
     	   }
     	   }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bts_score, menu);
        return true;
    }
}
