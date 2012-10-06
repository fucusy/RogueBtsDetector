/*
 * This class will setup the overlays for each base station that we will
 * show on the Maps app.
 * 
 */

package com.example.roguebtsdetector;

import android.graphics.drawable.Drawable;
import android.content.Context;
import android.app.AlertDialog;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

public class BtsItemizedOverlay extends ItemizedOverlay {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    public  Context mContext;

    public BtsItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));

        // TODO Auto-generated constructor stub
    }

    public BtsItemizedOverlay(Drawable defaultMarker, Context context) {
        super(boundCenterBottom(defaultMarker));
        mContext = context;
    }

    @Override
    protected OverlayItem createItem(int i) {
        // TODO Auto-generated method stub
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return mOverlays.size();
    }

    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }

    @Override
    protected boolean onTap(int index) {
        OverlayItem item = mOverlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext); 
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.show();

        return true;
    }


}
