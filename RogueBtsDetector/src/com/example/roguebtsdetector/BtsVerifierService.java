/*
 * This service is what will be running our logic. It's a service because we want the app to
 * work at all times, and run everytime the user changes locations.
 * 
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

    @Override
    public IBinder onBind(Intent intent) {
        return btsVerifierBinder;
    }

 


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BtsService", "started");
        
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

        GsmCellLocation gsmCellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
        
        cellid = Integer.toString(gsmCellLocation.getCid());
        lac = Integer.toString(gsmCellLocation.getLac());
        
        
      
        //createFakeProvider(locationManager);

        //verifyBts(gsmLoc);

        //Log.i("BtsService", "Received start id " + startId + ": " + intent);
        
        // We want this service to run indefinitely, so return sticky.
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        // Tell the user we stopped.
    }


  

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.



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
  
    
    
    public String getOpenBMapLocation()
    {
        
        
        openBMap.getLocation(mcc, mnc, lac, cellid);
        
        if(openBMap.err())
            return "-1:-1";
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return openBMap.latitude() + ":" + openBMap.longitude();
        
    }
    
    public String getOpenCellIdLocation()
    {
        
        
        openCellId.getLocation(mcc,mnc,lac,cellid);
        
        if(openCellId.err())
            return "-1:-1";
            
        //if(res == openCellId.ERROR || res == openCellId.CONNECTION_ERR)
        //    return res;
        
        return openCellId.latitude() + ":" + openCellId.longitude();
        
    }


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

    
    
    


    public int isRogue(GsmCellLocation loc){
 
        
        if(!validId(loc.getCid()))
            return 1;

        if(!validLocation(loc.getLac()))
            return 2;

        return 0;

    }

    public boolean validId(int cid){
        return true;
    }

    public boolean validLocation(int lac){
        return true;
    }

    public void alertUser(int status){

    }


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
