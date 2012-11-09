/*
 * This class provides functions that interface with the OpenCellID API.
 * This is the most useful of all APIs as it seems to have the most datapoints
 * as well as having additional functions.
 * 
 * 1) we can retrieve the current network location given the BTS identifiers
 * 2) we can retrieve the measurements used to calculate item 1)
 * 3) we can retrieve neighboring BTS locations given a min/max latitude/longitude
 */

package com.example.roguebtsdetector;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.roguebtsdetector.SERVICE_Location.ServiceToken;

public class DB_OpenCellId {


    private String openCellIdUrl, latmin, latmax, lonmin, lonmax;
    private double lat, lon;
    private boolean error;
    private String[] neighbors, measurements;
    private static final int LOCATION = 0, NEIGHS = 1, MEASURES = 2;
    private static final double BBOX_LENGTH = .5;
    public static final int CONNECTION_ERR = -2 , ERROR = -1;

    public DB_OpenCellId() 
    {
        lat = Double.NaN;
        lon = Double.NaN;          
        latmin = "";
        latmax = "";
        lonmin = "";
        lonmax = "";
        error = false;
    }

    public boolean err()
    {
        return error;
    }

    public double lat()
    {
        return lat;
    }
    
      
    public double lon()
    {
        return lon;
    }

    public String[] neighbors()
    {
        return neighbors;
    }
   
  
    public String[] measurements()
    {
        return measurements;
    }
   

    
    public double[] getLocation(String mcc, String mnc, String lac, String cellid)
    {
        double[] pair = new double[2];

        openCellIdUrl = 
                R.string.open_cell_id_url 
                +"get?mnc=" + mnc
                +"&mcc=" + mcc
                +"&lac=" + lac
                +"&cellid=" + cellid
                +"&fmt=txt";
        

        openCellIdRequest(LOCATION);
        
        if(error)
            return null;
        
        pair[0] = lat;
        pair[1] = lon;
                
        return pair;

    }


    public String[] getMeasures(String mcc, String mnc, String lac, String cellid)
    {
        openCellIdUrl  = 
                R.string.open_cell_id_url 
                +"getMeasures?mnc=" + mnc
                +"&mcc=" + mcc
                +"&lac=" + lac
                +"&cellid=" + cellid
                +"&fmt=txt";

        openCellIdRequest(MEASURES);
       
        if(error){
            return null;
        }
        else{
            return measurements;
        }
                         
    }


 
    
    
    public void setBbox(double latitude, double longitude)
    {
            latmin = Double.toString(latitude - BBOX_LENGTH);
            latmax = Double.toString(latitude + BBOX_LENGTH);
            lonmin = Double.toString(longitude - BBOX_LENGTH);
            lonmax = Double.toString(longitude + BBOX_LENGTH);
       
    }

    public String[] getNeighbors(String mcc, String mnc, int limit)
    {

        if(limit > 200)
            limit = 200;      

        setBbox(lat, lon);
        
        openCellIdUrl  = 
                R.string.open_cell_id_url 
                +"getInArea/?BBOX=" + latmin
                +"," + lonmin
                +"," + latmax
                +"," + lonmax
                +"&limit=" + limit;

        if(mcc != "")
            openCellIdUrl = openCellIdUrl + "&mcc=" + mcc;

        if(mnc != "")
            openCellIdUrl = openCellIdUrl + "&mnc=" + mnc;

        openCellIdRequest(NEIGHS);
        
        if(error){
            return null;
        }
        else{
            return neighbors;
        }
        
    }

    


    
    

    private int openCellIdRequest(int type)
    {
        String res;
        String [] tmp;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(openCellIdUrl);

        try
        {
            HttpResponse response = client.execute(request);
            res = EntityUtils.toString(response.getEntity()); 
        }
        catch(Exception e)
        {
            error = true;
            return CONNECTION_ERR;
        }   

        if(res.contains("error"))
        {
            error = true;
            return ERROR;
        
        }
        else
        {
            error = false;
            switch(type)
            {
                case LOCATION:
                    tmp = res.split(",");
                    lat = Double.parseDouble(tmp[0]);
                    lon = Double.parseDouble(tmp[1]);
                    break;
                case NEIGHS:
                    neighbors = res.split("\n\r");
                    // do something with this list !!!!!
                    // example list at:
                    //http://www.opencellid.org/cell/getInArea?BBOX=2.210836754705214,48.80850297346874,2.488529205537504,48.93716640758027&fmt=txt
                    break;
                case MEASURES:
                    //only returns in XML format.... 
                    measurements = res.split(">");
                    break;
                    
                   
            }
            
            return 1;
        }
    }



}
