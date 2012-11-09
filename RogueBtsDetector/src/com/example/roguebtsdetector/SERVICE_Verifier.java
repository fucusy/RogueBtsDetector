/*
 * This service is what will be running our logic. It's a service because we want the app
 * to be running at all times, even when the user does not have the app explicitly open.
 * Because it makes sense, and to save power, this service will only hook on to the "location change"
 * event. that means it will only do work, when the user changes his/her network location.
 * This service figures out which tower the user is connected to, and then verifies that the tower
 * is legitimate. If the tower is not legitimate, the user is warned. This service interfaces with the
 * app activity, so that the activity has access to the data collected/analyzed by the service.
 */

package com.example.roguebtsdetector;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.example.roguebtsdetector.SERVICE_Location.ServiceToken;

public class SERVICE_Verifier extends Service {

    
    private SERVICE_Location btsLocation = new SERVICE_Location();
    private TelephonyManager telephonyManager;
    private LocationManager locationManager;
    private final IBinder btsServiceBinder = new BtsServiceBinder();
    private ServiceToken serviceToken;
    private DB_OpenCellId openCellId = new DB_OpenCellId();
    private DB_OpenBMap openBMap = new DB_OpenBMap();
    
    
 // Define a listener that responds to location updates
    
    
    private LocationListener locationListener = new LocationListener() 
    {
        public void onLocationChanged(Location location) {
          // Called when a new location is found by the network location provider.
            btsLocation.one_time_refresh();
            serviceToken = btsLocation.getToken();
            verify(serviceToken, pollDatabases(serviceToken));
          
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    
      public class BtsServiceBinder extends Binder {
          SERVICE_Verifier getService() {
              return SERVICE_Verifier.this;
          }
      }
      
      
      /*
       * (non-Javadoc)
       * @see android.app.Service#onBind(android.content.Intent)
       */
      @Override
      public IBinder onBind(Intent intent) {
          return btsServiceBinder;
      }
      
      
   
    
    /*
     * (non-Javadoc)
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     * 
     * This function is called when the service is explicitly started. Here we want to initialize
     * some long term objects. Here we also verify the tower the user is connected to is valid.
     * 
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BtsService", "started");
         
          
        Context cont = getApplicationContext();
        
        //telephonyManager = (TelephonyManager) cont.getSystemService(TELEPHONY_SERVICE);
        btsLocation.ServicesStart(6500, cont);
        
        // Register the listener with the Location Manager to receive location updates
        locationManager = (LocationManager) cont.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        
        

        return START_STICKY;
    }


    
    @Override
    public void onDestroy() {
        // Tell the user we stopped.
    }


    public Map<String, double[]> pollDatabases(ServiceToken token){
        /*
        String mcc      = token.gsm.mcc;
        String mnc      = token.gsm.mnc;
        String cellid   = token.gsm.cid;
        String lac      = token.gsm.lac;
        */
        Map <String, double[]> results = new HashMap<String, double[]>();

        /*
        results.put("openCellId", openCellId.getLocation(mcc, mnc, lac, cellid));
        results.put("openBMap", openBMap.getLocation(mcc, mnc, lac, cellid));
          */     
        return results;
    }
  
   
    public void verify(ServiceToken token, Map<String, double[]> location)
    {
        
        
    }
    
  
    /*
     * Alerts the user that the current BTS tower is rogue.
     * Does so via notification? toast? ...
     */
  
    public void alertUser(int status){

    }


}
    
