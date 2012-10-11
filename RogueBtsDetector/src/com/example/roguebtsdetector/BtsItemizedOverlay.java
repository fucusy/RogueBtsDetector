/*
 * This is a GUI class. it creates the overlays to be shown on the main mapactivity.
 * Overlays include the users current location, the current BTS, and neighboring BTS towers.
 */

package com.example.roguebtsdetector;

import android.graphics.drawable.Drawable;
import android.content.Context;
import android.app.AlertDialog;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

public class BtsItemizedOverlay extends ItemizedOverlay {

    private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
    public  Context context;

    public BtsItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }


    public BtsItemizedOverlay(Drawable defaultMarker, Context cntxt) {
        super(boundCenterBottom(defaultMarker));
        context = cntxt;
    }

    @Override
    protected OverlayItem createItem(int i) {
        return overlays.get(i);
    }

    @Override
    public int size() {
        return overlays.size();
    }

    
    public void addOverlay(OverlayItem overlay) {
        overlays.add(overlay);
        populate();
    }

    /*
     * (non-Javadoc)
     * @see com.google.android.maps.ItemizedOverlay#onTap(int)
     * 
     * Defines what happens when the user taps on the overlay...
     * Presumably we want to show relevant BTS info
     */
    @Override
    protected boolean onTap(int index) {
        OverlayItem item = overlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context); 
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.show();

        return true;
    }


}
