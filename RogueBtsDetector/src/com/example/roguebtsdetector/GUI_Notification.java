package com.example.roguebtsdetector;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class GUI_Notification {

	public void notify_user(Context c, String reason)
	{
		Notification n = new Notification.Builder(c)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("Rogue BTS Alert")
			.setContentText(reason)
			.build();
		
		Intent i = new Intent(c, GUI_RogueBtsDetector.class);
		
		PendingIntent p = PendingIntent.getActivity(c, 0, i, 0);
		
		NotificationManager m = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
		
		m.notify(R.drawable.ic_launcher, n);
		
		
	}

}
