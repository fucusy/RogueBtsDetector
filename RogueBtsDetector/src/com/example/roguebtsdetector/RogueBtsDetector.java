/*
 * This is the main application activity class. The actual app
 * should open up Maps, show the user's current location, the BTS station the user
 * is connected to, and the neighboring stations... 
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
import android.location.Location;
import android.location.LocationProvider;
import android.location.LocationListener;
import android.location.LocationManager;
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


    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

       // doBindService();
        
        //createFakeProvider();
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("com.example.roguebtsdetector.BtsVerifierService");
        startService(serviceIntent);

       // MapView mapView = (MapView) findViewById(R.id.mapview);
       // mapView.setBuiltInZoomControls(true);

        //mapOverlays = mapView.getOverlays();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_Rogue_Bts_Detector, menu);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    } 
 /*
    @Override
    public void onStart() {

        // Get the location manager
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int btsCid = 0, btsLat = 0, btsLon= 0;
        
        showTower(btsCid, btsLat, btsLon);

    }
*/
    public void showTower(int cid, int lat, int lon){

        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
        BtsItemizedOverlay itemizedoverlay = new BtsItemizedOverlay(drawable, this);
        GeoPoint point = new GeoPoint(lat,lon);
        OverlayItem overlayitem = new OverlayItem(point,"Tower "+cid,"You are connected to this tower");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
    } 


    public void createFakeProvider(LocationManager locman){

        /*
        locman.addTestProvider(provider, false, false, true, false, false, false, false, 0, 5);
        locman.setTestProviderLocation(provider, fakeLocation);
        locman.setTestProviderEnabled(provider, true);
         */


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

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
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


