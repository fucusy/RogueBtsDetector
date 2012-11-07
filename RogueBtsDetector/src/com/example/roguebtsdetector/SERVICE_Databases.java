package com.example.roguebtsdetector;

public class SERVICE_Databases {
    private DB_OpenCellId openCellId;
    private DB_OpenBMap openBMap;
    private String mcc, mnc, lac, cellid, latitude, longitude;

    
    public SERVICE_Databases() {
   
         openCellId = new DB_OpenCellId();
         openBMap = new DB_OpenBMap();
     
     
        // TODO Auto-generated constructor stub
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

    

}
