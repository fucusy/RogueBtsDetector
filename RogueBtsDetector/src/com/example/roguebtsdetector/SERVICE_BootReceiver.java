/*
 * This is a receiver is what launches the service. We want the service to run everytime
 * the phone boots, and this receiver does just that.
 * 
 */

package com.example.roguebtsdetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SERVICE_BootReceiver extends BroadcastReceiver {

      @Override
      public void onReceive(Context context, Intent intent) {    
         Intent service = new Intent(context, SERVICE_Verifier.class);
         context.startService(service);
      
      }
    } 