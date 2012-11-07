/*
 * This class provides functions that interface with the OpenBMap API.
 * we can retrieve the current network location given the BTS identifiers.
 * 
 */


package com.example.roguebtsdetector;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class DB_OpenBMap {

    private String myLatitude, myLongitude;
    private boolean error;
    
    
    
    public DB_OpenBMap() {
        myLatitude = "";
        myLongitude = "";
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
   

    public int getLocation(String mcc, String mnc, String lac, String cellid)
    {
        String res;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost request = new HttpPost(R.string.open_b_map_location_url + "");

        //add post arguments
        List<NameValuePair> args = new ArrayList<NameValuePair>(2);
        args.add(new BasicNameValuePair("mcc", mcc));
        args.add(new BasicNameValuePair("mnc", mnc));
        args.add(new BasicNameValuePair("lac", lac));
        args.add(new BasicNameValuePair("cell_id", cellid));
        
        try 
        {
            request.setEntity(new UrlEncodedFormEntity(args));     
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(request);
            res = EntityUtils.toString(response.getEntity()); 

        }
        catch(Exception e)
        {
            error = true;
            return -1;
        }
        
        extractLocation(res);
        
        return 1;
    }
    
    private void extractLocation(String response)
    {
        int latIndex;
        int lonIndex;
        String[] res = response.split(">");
        if(res.equals(response))
        {
            error = true;
            return;
        }
    
        latIndex =  response.indexOf("lat=");
        response = response.substring(latIndex+5);
        myLatitude = response.substring(0, response.indexOf('\"'));
        
        lonIndex = response.indexOf("lng=");
        response = response.substring(lonIndex);
        myLongitude = response.substring(0, response.indexOf('\"'));
        
        
    }
    
    
}

    
    