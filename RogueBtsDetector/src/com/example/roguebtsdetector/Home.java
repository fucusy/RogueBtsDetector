package com.example.roguebtsdetector;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Home extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button button = (Button)findViewById(R.id.button1);
        button.setOnClickListener((android.view.View.OnClickListener) switchToScore);
    }
    
    private OnClickListener switchToScore = new OnClickListener() {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			//create a new intent that will launch the new 'page'
	        Intent i = new Intent(Home.this, BTS_Score.class);
	        startActivity(i);
			
		}
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
}
