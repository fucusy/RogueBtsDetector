/*
 * This is the main application activity class. The actual app
 * should open up a map, show the user's current location, the BTS station the user
 * is connected to, and the neighboring stations... if a rogue BTS is found, it should be
 * visibly marked as such.
 * 
 */

package com.example.roguebtsdetector;
import com.example.roguebtsdetector.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.app.Activity;

public class RogueBtsDetector extends Activity {    
    
    /*
     * (non-Javadoc)
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     * 
     * This method gets called when this activity first gets created (installed).
     * This is where we want to initialize variables, bind to the service, set the content view (map)
     * and other things...
     */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);       
        
        Button buttonScore = (Button) findViewById(R.id.goScore);
        buttonScore.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   Intent i = new Intent(RogueBtsDetector.this, BTS_Score.class); 
     		   startActivity(i);
     		   }
     	   }
        );
        
        
        Button buttonPrefs = (Button) findViewById(R.id.button2);
        buttonPrefs.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   Intent i = new Intent(RogueBtsDetector.this, Preference.class); 
     		   startActivity(i);
     		   }
     	   }
        );
        
        Button buttonAbout = (Button) findViewById(R.id.button3);
        buttonAbout.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   Intent i = new Intent(RogueBtsDetector.this, About.class); 
     		   startActivity(i);
     		   }
     	   }
        );
        
        Button buttonMap = (Button) findViewById(R.id.map);
        buttonMap.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   Intent i = new Intent(RogueBtsDetector.this, BtsMap.class); 
     		   startActivity(i);
     		   }
     	   }
        );
        

    }

    
   

} 


