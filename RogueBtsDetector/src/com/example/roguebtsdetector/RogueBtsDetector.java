/*
 * This is the main application activity class. The actual app
 * should open up a map, show the user's current location, the BTS station the user
 * is connected to, and the neighboring stations... if a rogue BTS is found, it should be
 * visibly marked as such.
 * 
 */

package com.example.roguebtsdetector;
import java.util.*;
import android.graphics.drawable.*;
import com.example.roguebtsdetector.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Overlay;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.location.Location;
import android.location.LocationProvider;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.maps.MapActivity;
import android.content.Context;
import android.location.Criteria;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;
import java.util.*;
import android.content.Intent;
import android.app.Activity;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;

public class RogueBtsDetector extends MapActivity implements LocationListener {

    private int rogueStatus, myLatitude, myLongitude, cid;
    private boolean btsServiceIsBound;
    private List<Overlay> mapOverlays;
    private BtsVerifierService btsBoundService;
    //private LocationManager locationManager;
    //private String provider = "rogueBts";


    /*
     * (non-Javadoc)
     * @see com.google.android.maps.MapActivity#isRouteDisplayed()
     */
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    
    
    /*
     * (non-Javadoc)
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     * 
     * This method gets called when this activity first gets created (installed).
     * This is where we want to initialize variables, bind to the service, set the content view (map)
     * and other things...
     */
    
    Button button;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        doBindService();
        

        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   Intent i = new Intent(RogueBtsDetector.this, BTS_Score.class); 
     		   startActivity(i);
     		   }
     	   }
        );
        
        Button buttonPrefs = (Button) findViewById(R.id.button3);
        buttonPrefs.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   Intent i = new Intent(RogueBtsDetector.this, Preference.class); 
     		   startActivity(i);
     		   }
     	   }
        );
        
        Button buttonAbout = (Button) findViewById(R.id.button2);
        buttonAbout.setOnClickListener(new OnClickListener()
        {
     	   @Override 
     	   public void onClick(View v){
     		   Intent i = new Intent(RogueBtsDetector.this, About.class); 
     		   startActivity(i);
     		   }
     	   }
        );
        
        /*
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapOverlays = mapView.getOverlays();
        */

    }

    
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
//    	inflater.inflate(R.menu.activity_rogue_bts_detector, menu);
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    /*This method detects clicks on the menu items.
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menuitem:
                startActivity(new Intent(this, Preferences.class));
        }
        return true;
    }
    
    
    /*
     * (non-Javadoc)
     * @see android.location.LocationListener#onLocationChanged(android.location.Location)
     */
    @Override
    public void onLocationChanged(Location location) {

    }

    
    /*
     * +(non-Javadoc)
     * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    /*
     * (non-Javadoc)
     * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
     */
    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    
    /*
     * (non-Javadoc)
     * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
     */
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    } 
 

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onStart()
     * 
     * onStart is called every time the app is started. 
     * This is when we should gather information about neighboring BTS towers.
     * This should prompt the service to do just that, and then retrieve the data from the service.
     * Here we should call all functions that will show the user all relevant overlays.
     * */
    @Override
    public void onStart() {
        super.onStart();

        Toast.makeText(this, "app started",
                Toast.LENGTH_SHORT).show();
        // Get the location manager
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int btsCid = 0, btsLat = 0, btsLon= 0;
        
        //showTower(btsCid, btsLat, btsLon);

    } 
    
    
    /*
     * showTower, adds a tower overlay with the specified info to the mapview.
     */

    public void showTower(int cid, int lat, int lon){

        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
        BtsItemizedOverlay itemizedoverlay = new BtsItemizedOverlay(drawable, this);
        GeoPoint point = new GeoPoint(lat,lon);
        OverlayItem overlayitem = new OverlayItem(point,"Tower "+cid,"You are connected to this tower");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
    } 



    private ServiceConnection btsServiceConnection = new ServiceConnection() 
    {

        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            btsBoundService = ((BtsVerifierService.BtsVerifierBinder)service).getService();

            // Tell the user about this for our demo.
            Toast.makeText(RogueBtsDetector.this, R.string.bts_service_connected,  Toast.LENGTH_SHORT).show();
        }


        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            btsBoundService = null;
             Toast.makeText(RogueBtsDetector.this, R.string.bts_service_disconnected,   Toast.LENGTH_SHORT).show();
        }
    };

    
    
    /*
     * Establish a connection with the service.  
     */
    void doBindService() {
        bindService(new Intent(this, 
                BtsVerifierService.class), btsServiceConnection, Context.BIND_AUTO_CREATE);
        btsServiceIsBound = true;
    }

    
    
    void doUnbindService() {
        if (btsServiceIsBound) {
            // Detach our existing connection.
            unbindService(btsServiceConnection);
            btsServiceIsBound = false;
        }
    }

    
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }


} 


