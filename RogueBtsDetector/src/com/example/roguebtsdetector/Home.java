package com.example.roguebtsdetector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;



public class Home extends Activity 
{
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_home);
   }

   public void switchToScore(View view)
   {
       Intent intent = new Intent(Home.this, BTS_Score.class);
       startActivity(intent);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
       getMenuInflater().inflate(R.menu.activity_home, menu);
       return true;
   }
   
}
