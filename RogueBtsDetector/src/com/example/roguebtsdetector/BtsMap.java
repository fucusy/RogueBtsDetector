package com.example.roguebtsdetector;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BtsMap extends MapActivity  {

    private List<Overlay> mapOverlays;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bts_map);
        
    }
    
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
//    	inflater.inflate(R.menu.activity_rogue_bts_detector, menu);
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    /*This method detects clicks on the menu items.
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menuitem:
                startActivity(new Intent(this, Preference.class));
        }
        return true;
    }
        
	
	/*
     * showTower, adds a tower overlay with the specified info to the mapview.
     */

    public void showTower(int cid, int lat, int lon){

        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
        BtsItemizedOverlay itemizedoverlay = new BtsItemizedOverlay(drawable, this);
        GeoPoint point = new GeoPoint(lat,lon);
        OverlayItem overlayitem = new OverlayItem(point,"Tower "+cid,"You are connected to this tower");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
    }


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	} 


}
