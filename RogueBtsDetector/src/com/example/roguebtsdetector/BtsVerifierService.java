/*
 * This service is what will be running our logic. It's a service because we want the app to
 * work at all times, and run everytime the user changes locations.
 * 
 */

package com.example.roguebtsdetector;

import	android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class BtsVerifierService extends Service {

    private GsmCellLocation loc;
    private TelephonyManager tm;
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
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        loc = (GsmCellLocation) tm.getCellLocation();
        verifyBts(loc);

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


}
