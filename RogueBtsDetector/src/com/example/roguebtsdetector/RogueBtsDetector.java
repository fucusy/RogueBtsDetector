package com.example.roguebtsdetector;

import java.util.*;

import android.graphics.drawable.*;

import com.example.roguebtsdetector.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Overlay;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.location.Location;
import android.location.LocationProvider;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import com.google.android.maps.MapActivity;
import android.content.Context;
import android.location.Criteria;
import android.util.Log;
import android.widget.Toast;
import java.util.*;

public class RogueBtsDetector extends MapActivity implements LocationListener {

	 private TextView latituteField;
	  private TextView longitudeField;
	  private LocationManager locationManager;
	  private String provider = "raull";
	  private Location fakeLocation  = new Location(provider);
	  private LocationProvider loc;
	  
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
      
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        // Define the criteria how to select the location provider -> use
        // default
       
        fakeLocation.setLatitude(44.4406);
        fakeLocation.setLongitude(79.9961);
        
        /*
        locationManager.addTestProvider(provider, false, false, true, false, false, false, false, 0, 5);
        locationManager.setTestProviderLocation(provider, fakeLocation);
        locationManager.setTestProviderEnabled(provider, true);
       */
      
        //Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (fakeLocation != null) {
          System.out.println("Provider " + provider + " has been selected.");
          // onLocationChanged(fakeLocation);
        } else {
          latituteField.setText("Location not available");
          longitudeField.setText("Location not available");
        }
        
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
        RogueBtsItemizedOverlay itemizedoverlay = new RogueBtsItemizedOverlay(drawable, this);
    
        GeoPoint point = new GeoPoint(19240000,-99120000);
    	OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");

    	itemizedoverlay.addOverlay(overlayitem);
    	mapOverlays.add(itemizedoverlay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_hello_google_maps, menu);
        return true;
    }
    
    @Override
    public void onLocationChanged(Location location) {
      double lat = location.getLatitude();
      double lng = location.getLongitude();
      //latituteField.setText(String.valueOf(lat));
     // longitudeField.setText(String.valueOf(lng));
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

    
	
} 


