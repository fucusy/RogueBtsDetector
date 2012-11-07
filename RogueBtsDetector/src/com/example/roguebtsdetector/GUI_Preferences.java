package com.example.roguebtsdetector;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class GUI_Preferences extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        
        Button buttonAbout = (Button) findViewById(R.id.apply);
        buttonAbout.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   EditText interval = (EditText) findViewById(R.id.editText1);
     		   CheckBox isOn = (CheckBox) findViewById(R.id.checkBox1);
     		   
     		  // TODO: find where the timer is set and how to turn off service
     	   }
     	}
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_preference, menu);
        return true;
    }
}
