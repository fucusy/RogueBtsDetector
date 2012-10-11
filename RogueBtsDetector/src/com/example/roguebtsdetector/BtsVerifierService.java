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
import android.content.Intent;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class BtsVerifierService extends Service {

    private GsmCellLocation gsmCellLocation;
    private TelephonyManager telephonyManager;
    private LocationManager locationManager;
    private final IBinder btsVerifierBinder = new BtsVerifierBinder();
    private OpenCellId openCellId = new OpenCellId();
    private OpenBMap openBMap = new OpenBMap();
    private String mcc, mnc, lac, cellid, latitude, longitude;

  
    public class BtsVerifierBinder extends Binder {
        BtsVerifierService getService() {
            return BtsVerifierService.this;
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
        
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

        GsmCellLocation gsmCellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
        
        cellid = Integer.toString(gsmCellLocation.getCid());
        lac = Integer.toString(gsmCellLocation.getLac());
        
        
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



    
    private void verifyBts(GsmCellLocation loc)
    {
        Log.i("BtsService", "verifyBts");

        /*
        int status = isRogue(loc);

        if(status != 0)
        {
            alertUser(status);
        }	    
         */
    }
  
    
    /*
     * Get the current BTS tower's location from the OpenBMap Database
     * returns the location as a string "latitude:longitude"
     */
    public String getOpenBMapLocation()
    {
        
        
        openBMap.getLocation(mcc, mnc, lac, cellid);
        
        if(openBMap.err())
            return "-1:-1";
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return openBMap.latitude() + ":" + openBMap.longitude();
        
    }
    
    
    
    /*
     * Get the current BTS tower's location from the OpenCellID Database
     * returns the location as a string "latitude:longitude"
     */
    public String getOpenCellIdLocation()
    {
        
        openCellId.getLocation(mcc,mnc,lac,cellid);
        
        if(openCellId.err())
            return "-1:-1";
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return openCellId.latitude() + ":" + openCellId.longitude();
        
    }


    /*
     * Get the measurements the OpenCellID Database used to calculate the current location.
     */
    public String[] getOpenCellIdMeasurements()
    {
        
        String[] err = {};        
        openCellId.getMeasures(mcc, mnc, lac, cellid);
        
        if(openCellId.err())
            return err;
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return openCellId.measurements();
        
    }

    
    
    /*
     *  Get the device's neighboring BTS stations using the OpenCellID database.
     */
    public String[] getOpenCellIdNeighbors(int limit)
    {
        
        String[] err = {};
        
        openCellId.setBbox(Double.valueOf(latitude), Double.valueOf(longitude));
        openCellId.getNeighbors(mcc, mcc, limit);
        
        if(openCellId.err())
            return err;
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return openCellId.neighbors();
        
    }

    
    
    /*
     * Decides whether the current BTS is rogue or not
     */
    public int isRogue(GsmCellLocation loc){
         

        return 0;

    }

    
    
  
    /*
     * Alerts the user that the current BTS tower is rogue.
     * Does so via notification? toast? ...
     */
    public void alertUser(int status){

    }


    /*
     * Creates a fake location provider... so that we can test things.
     * 
     */
    public void createFakeProvider(LocationManager locman)
    {

        Log.i("BtsService", "fakeProvider");

        /*
        locman.addTestProvider(provider, false, false, true, false, false, false, false, 0, 5);
        locman.setTestProviderLocation(provider, fakeLocation);
        locman.setTestProviderEnabled(provider, true);
         */


    }


    
}
