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

    
    private SERVICE_Location btsLocationService = new SERVICE_Location();
    private GsmCellLocation gsmCellLocation;
    private TelephonyManager telephonyManager;
    private LocationManager locationManager;
    private final IBinder btsVerifierBinder = new BtsVerifierBinder();
    private DB_OpenCellId dB_OpenCellId = new DB_OpenCellId();
    private DB_OpenBMap dB_OpenBMap = new DB_OpenBMap();
    private String mcc, mnc, lac, cellid, latitude, longitude;
    private ServiceToken serviceToken;
    
    
    
 // Define a listener that responds to location updates
    
    
    private LocationListener locationListener = new LocationListener() 
    {
        public void onLocationChanged(Location location) {
          // Called when a new location is found by the network location provider.
            btsLocationService.one_time_refresh();
            serviceToken = btsLocationService.getToken();
           // pollDatabases(serviceToken);
          
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    
      public class BtsVerifierBinder extends Binder {
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
          return btsVerifierBinder;
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
        
        
        

        //GsmCellLocation gsmCellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
        
        //cellid = Integer.toString(gsmCellLocation.getCid());
        //lac = Integer.toString(gsmCellLocation.getLac());
        
        Context cont = getApplicationContext();
        
        //telephonyManager = (TelephonyManager) cont.getSystemService(TELEPHONY_SERVICE);
        btsLocationService.ServicesStart(6500, cont);
        
        // Register the listener with the Location Manager to receive location updates
        locationManager = (LocationManager) cont.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        
        
       
        
      /*
        createFakeProvider(locationManager);
        verifyBts(gsmLoc);
        Log.i("BtsService", "Received start id " + startId + ": " + intent);
      // We want this service to run indefinitely, so return sticky.
      */
        return START_STICKY;
    }


    
    @Override
    public void onDestroy() {
        // Tell the user we stopped.
    }


    /*

    private void verify(ServiceToken token)
    {
       int status = token.getStatus();
                
    }
    
    private void verifyBts(GsmCellLocation loc)
    {
        Log.i("BtsService", "verifyBts");

    
    }
  
    */
    /*
     * Get the current BTS tower's location from the OpenBMap Database
     * returns the location as a string "latitude:longitude"
     */
    
    /*
    public String getOpenBMapLocation()
    {
        
        openBMap.getLocation(mcc, mnc, lac, cellid);
        
        if(openBMap.err())
            return "-1:-1";
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return openBMap.latitude() + ":" + openBMap.longitude();
        
    }
    */
    
    
    /*
     * Get the current BTS tower's location from the OpenCellID Database
     * returns the location as a string "latitude:longitude"
     */

    public String getOpenCellIdLocation()
    {
        
        dB_OpenCellId.getLocation(mcc,mnc,lac,cellid);
        
        if(dB_OpenCellId.err())
            return "-1:-1";
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return dB_OpenCellId.latitude() + ":" + dB_OpenCellId.longitude();
        
    }


    /*
     * Get the measurements the OpenCellID Database used to calculate the current location.
     */
  
    
    public String[] getOpenCellIdMeasurements()
    {
        
        String[] err = {};        
        dB_OpenCellId.getMeasures(mcc, mnc, lac, cellid);
        
        if(dB_OpenCellId.err())
            return err;
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return dB_OpenCellId.measurements();
        
    }

    
    
    /*
     *  Get the device's neighboring BTS stations using the OpenCellID database.
     */
  
    public String[] getOpenCellIdNeighbors(int limit)
    {
        
        String[] err = {};
        
        dB_OpenCellId.setBbox(Double.valueOf(latitude), Double.valueOf(longitude));
        dB_OpenCellId.getNeighbors(mcc, mcc, limit);
        
        if(dB_OpenCellId.err())
            return err;
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return dB_OpenCellId.neighbors();
        
    }

    
    
    /*
     * Decides whether the current BTS is rogue or not
     */
  /*
    public int isRogue(GsmCellLocation loc){
         

        return 0;

    }
*/
    
    
  
    /*
     * Alerts the user that the current BTS tower is rogue.
     * Does so via notification? toast? ...
     */
  /*
    public void alertUser(int status){

    }
*/

}
    
