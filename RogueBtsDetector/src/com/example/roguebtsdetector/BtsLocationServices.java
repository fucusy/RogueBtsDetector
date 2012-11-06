package com.example.roguebtsdetector;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.telephony.*;
import android.telephony.cdma.CdmaCellLocation;
import android.util.Log;

public class BtsLocationServices extends Activity {
	
	private int runningStatus = 0;
	private int globalDelay = 0;
	private final ExecutorService Tpool;
	private ServiceToken curLog = new ServiceToken();

	public BtsLocationServices () {
		
		Tpool = Executors.newSingleThreadExecutor();
	}
	
	
	public class cdmaObj {
		int base_id;
		int base_lat;
		int base_long;
		int net_id;
		int sys_id;
		int dbm;
		int ecio;
		
		public cdmaObj clone() {
			
			cdmaObj cdma_copy = new cdmaObj();
			cdma_copy.base_id = this.base_id;
			cdma_copy.base_lat = this.base_lat;
			cdma_copy.base_long = this.base_long;
			cdma_copy.net_id = this.net_id;
			cdma_copy.sys_id = this.sys_id;
			cdma_copy.dbm = this.dbm;
			cdma_copy.ecio = this.ecio;
			
			return cdma_copy;
		}
	}
	
	public class ServiceToken {
		private int status = 0; //0 if empty, 1 if containing info
		private int servicetype = 0;
        private cdmaObj cdma;
		
		
		
		public int getStatus() {
			return status;
		}
		
		public void setStatus(int status) {
			this.status = status;
		}

		public int getServicetype() {
			return servicetype;
		}

		public void setServicetype(int servicetype) {
			this.servicetype = servicetype;
		}
		
		public ServiceToken deepcopy (){
			ServiceToken copyToken = new ServiceToken();
			copyToken = curLog;
			copyToken.status = curLog.status;
			copyToken.servicetype = curLog.servicetype;
			copyToken.cdma = curLog.cdma.clone();
			return copyToken;
		}
	}
	
	
	public boolean changeRefresh (int newDelay){
		return true;
	}
	
	
	
	
	
	public ServiceToken getToken(){
		return curLog.deepcopy();
	}
	

	public boolean one_time_refresh(){
		return true;
	}

	
	
	//Delay is the delay in seconds between location refreshes
	public boolean ServicesStart (int delay, Context appContext) {
		
		globalDelay = delay;
		

		if (runningStatus == 1)
			return true;
		else
		{
			runningStatus = 1;
			Runnable worker = new LoggingRunnable(appContext);
			Tpool.execute(worker);	
		}
		return true;
	}
	
	public boolean ServicesStop(Context appContext)
	{
		return true;
	}
	
	public ServiceToken getLog () {
		return curLog;
	}
	
	
//private Runnable loggingLoop (int delay) {
//
//}

	private class LoggingRunnable implements Runnable {
	
		private Context myContext;
		
		LoggingRunnable(Context mContext){
			this.myContext = mContext;
		}
		
		@Override
		public void run () {
			int type;
			int state;
			int serviceType;
			//Everything inside running status is a function so it can be called by events
			//as well as by this timer.
			while(runningStatus == 1)
			{
				//private TelephonyManager TM;
				//TelephonyManager TM = new TelephonyManager();
				
				TelephonyManager tm;
				
				try{
					tm = (TelephonyManager) this.myContext.getSystemService(Context.TELEPHONY_SERVICE);	
				}
				catch (IllegalStateException e){
					//TODO fix this hack with a good catch
					return;
				}
	
				
				//if tm is populated there must be a network type
				serviceType = tm.getNetworkType();
				
				if (serviceType == TelephonyManager.NETWORK_TYPE_CDMA)
				{
					CDMA_populate(tm, serviceType);
					CDMA_Listener(tm, serviceType);
				}
	
	
				try {
					Thread.sleep(globalDelay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
			
		private void CDMA_populate(TelephonyManager tm, int serviceType){
			CdmaCellLocation CDMAcl;
			
			synchronized(curLog){
				CDMAcl = (CdmaCellLocation) tm.getCellLocation();
				
				curLog.setServicetype(serviceType);
	    		curLog.cdma.base_id = CDMAcl.getBaseStationId();
	    		curLog.cdma.base_lat = CDMAcl.getBaseStationLatitude();
	    		curLog.cdma.base_long = CDMAcl.getBaseStationLongitude();
	    		curLog.cdma.net_id = CDMAcl.getNetworkId();
	    		curLog.cdma.sys_id = CDMAcl.getSystemId();
	    		curLog.status = 1;
			}
		}
		private void CDMA_Listener(TelephonyManager tm, int serviceType){

		}
	}
}
	


