/*
 * This is the main application activity class. The actual app
 * should open up a map, show the user's current location, the BTS station the user
 * is connected to, and the neighboring stations... if a rogue BTS is found, it should be
 * visibly marked as such.
 * 
 */

package com.example.roguebtsdetector;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GUI_RogueBtsDetector extends Activity {    
    
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
     		   Intent i = new Intent(GUI_RogueBtsDetector.this, GUI_Score.class); 
     		   startActivity(i);
     		   }
     	   }
        );
        
        
        Button buttonPrefs = (Button) findViewById(R.id.button2);
        buttonPrefs.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   Intent i = new Intent(GUI_RogueBtsDetector.this, GUI_Preferences.class); 
     		   startActivity(i);
     		   }
     	   }
        );
        
        Button buttonAbout = (Button) findViewById(R.id.button3);
        buttonAbout.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){

              // Intent i = new Intent(RogueBtsDetector.this, About.class); 
     		  // startActivity(i);
     	       
     	      Log.i("BtsService", "stopping service");

              Intent service = new Intent(GUI_RogueBtsDetector.this, SERVICE_Verifier.class);
              stopService(service);
     	       
     		   }
     	   }
        );
        
        Button buttonMap = (Button) findViewById(R.id.map);
        buttonMap.setOnClickListener(new OnClickListener(){
     	   @Override 
     	   public void onClick(View v){
     		   //Intent i = new Intent(RogueBtsDetector.this, BtsMap.class); 
     		  // startActivity(i);
     		   
     	       // Jenna !!!!!!! can you move this to its own button.
     	       // button: "startService" in click do this..
     	      if(!isMyServiceRunning()){
     	         Log.i("BtsService", "starting service");

     	          Intent service = new Intent(GUI_RogueBtsDetector.this, SERVICE_Verifier.class);
     	          startService(service);
     	          
     	      }
     	      
     	      
     	   }
     	}
        );
        
        
        /*
        Log.i("BtsService", "app opened!!!");

        
        Context c = getApplicationContext();
        Log.i("BtsService", c.getPackageName());

        Intent service = new Intent(c, BtsVerifierService.class);
        Log.i("BtsService", service.getPackage());

        c.startService(service);
        */
    }

    
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.roguebtsdetector.btsverifierservice".equals(service.service.getClassName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
   

} 


