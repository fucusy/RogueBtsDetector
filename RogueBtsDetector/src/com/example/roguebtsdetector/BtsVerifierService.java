/*
 * This service is what will be running our logic. It's a service because we want the app to
 * work at all times, and run everytime the user changes locations.
 * 
 */

package com.example.roguebtsdetector;

import	android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationProvider;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class BtsVerifierService extends Service {

    private GsmCellLocation gsmLoc;
    private TelephonyManager telMan;
    private LocationManager locMan;
    private final IBinder btsVerifierBinder = new BtsVerifierBinder();


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
        Log.i("BtsService", "startedd");
        
        
        locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locMan == null)
        {
            Log.i("BtsService", "locMan NULL");
        }
        else
        {
            createFakeProvider(locMan);

        }
        //telMan = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //gsmLoc = (GsmCellLocation) tm.getCellLocation();
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
