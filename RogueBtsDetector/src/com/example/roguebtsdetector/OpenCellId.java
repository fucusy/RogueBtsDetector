package com.example.roguebtsdetector;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class OpenCellId {


    private String openCellIdUrl, myLatitude, myLongitude, latmin, latmax, lonmin, lonmax;
    private boolean error;
    private String[] neighbors, measurements;
    private static final int LOCATION = 0, NEIGHS = 1, MEASURES = 2;
    private static final double BBOX_LENGTH = .5;
    public static final int CONNECTION_ERR = -2 , ERROR = -1;

    public OpenCellId() 
    {
        myLatitude = "";
        myLongitude = "";
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

    public String latitude()
    {
        return myLatitude;
    }
    
      
    public String longitude()
    {
        return myLongitude;
    }

    public String[] neighbors()
    {
        return neighbors;
    }
   
  
    public String[] measurements()
    {
        return measurements;
    }
   

    
    public int getLocation(String mcc, String mnc, String lac, String cellid)
    {
        openCellIdUrl = 
                R.string.open_cell_id_url 
                +"get?mnc=" + mnc
                +"&mcc=" + mcc
                +"&lac=" + lac
                +"&cellid=" + cellid
                +"&fmt=txt";

        return openCellIdRequest(LOCATION);

    }


    public int getMeasures(String mcc, String mnc, String lac, String cellid)
    {
        openCellIdUrl  = 
                R.string.open_cell_id_url 
                +"getMeasures?mnc=" + mnc
                +"&mcc=" + mcc
                +"&lac=" + lac
                +"&cellid=" + cellid
                +"&fmt=txt";

        return openCellIdRequest(MEASURES);


    }

    
    public void setBbox(double lat, double lon)
    {
            latmin = Double.toString(lat - BBOX_LENGTH);
            latmax = Double.toString(lat + BBOX_LENGTH);
            lonmin = Double.toString(lon - BBOX_LENGTH);
            lonmax = Double.toString(lon + BBOX_LENGTH);
       
    }

    public int getNeighbors(String mcc, String mnc, int limit)
    {

        if(limit > 200)
            limit = 200;      

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

        return openCellIdRequest(NEIGHS);

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
                    myLatitude = tmp[0];
                    myLongitude = tmp[1];
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
