package com.example.roguebtsdetector;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;



public class GUI_Home extends Activity 
{
	Button button;
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_home);
       
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
       getMenuInflater().inflate(R.menu.activity_home, menu);
       return true;
   }
   
}
