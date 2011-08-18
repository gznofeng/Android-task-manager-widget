package com.sftask.receiver;

import com.sftask.appwidget.ExampleAppWidgetProvider;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("AppReceiver", "intent=" + intent.getAction());
		if (intent.getAction().equals(Intent.ACTION_MAIN)) {
			Intent cintent = new Intent(context, ExampleAppWidgetProvider.class);
			cintent.setAction(ExampleAppWidgetProvider.COMMAND_REFRESH);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, cintent, PendingIntent.FLAG_UPDATE_CURRENT);
		}

	}

}
