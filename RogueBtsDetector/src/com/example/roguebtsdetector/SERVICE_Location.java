package com.example.roguebtsdetector;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

public class SERVICE_Location extends Activity {
	
	private int runningStatus = 0;
	private int globalDelay = 0;
	private final ExecutorService Tpool;
	private ServiceToken curLog = new ServiceToken();

	public SERVICE_Location () {
		
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
	
	public class gsmObj {
	    String cid;
	    String lac;
	    String mcc;
	    String mnc;
	    
	    public gsmObj clone() {
	        gsmObj gsm_copy = new gsmObj();
	        gsm_copy.cid = this.cid;
	        gsm_copy.lac = this.lac;
	        gsm_copy.mcc = this.mcc;
	        gsm_copy.mnc = this.mnc;
	        
	        return gsm_copy;
	    }
	}
	
	
	public class ServiceToken {
		public int status = 0; //0 if empty, 1 if containing info
		public int servicetype = 0;
        public cdmaObj cdma;
        public gsmObj gsm;
		
		
		
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
            copyToken.gsm = curLog.gsm.clone();

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
		private TelephonyManager tm;
		
		LoggingRunnable(Context mContext){
			this.myContext = mContext;
		}
		
		@Override
		public void run () {
			int type;
			int state;
			int serviceType;
            TelephonyManager tm;
			CellLocation cl;
			//Everything inside running status is a function so it can be called by events
			//as well as by this timer.
			while(runningStatus == 1)
			{
								
				try{
					tm = (TelephonyManager) this.myContext.getSystemService(Context.TELEPHONY_SERVICE);	
				}
				catch (IllegalStateException e){
					//TODO fix this hack with a good catch
					return;
				}
	
				
				//if tm is populated there must be a network type
				
				serviceType = tm.getNetworkType();
                cl = tm.getCellLocation();
				
				
				if (serviceType == TelephonyManager.NETWORK_TYPE_CDMA){
					CDMA_populate(tm, serviceType);
					CDMA_Listener(tm, serviceType);
				}
				
				else if(cl instanceof GsmCellLocation){
				    GSM_populate(tm, serviceType);				    
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
		
		private void GSM_populate(TelephonyManager tm, int serviceType)
		{
		    GsmCellLocation GSMcl = (GsmCellLocation) tm.getCellLocation();
		    
		    synchronized(curLog){
	            curLog.setServicetype(serviceType);
		        curLog.gsm.cid = String.valueOf(GSMcl.getCid());
                curLog.gsm.lac = String.valueOf(GSMcl.getLac());             
                curLog.gsm.mcc = tm.getNetworkOperator();
                curLog.gsm.mnc = tm.getNetworkOperator();
                curLog.status = 1;
		      
		    }
		}
	}
}
	


