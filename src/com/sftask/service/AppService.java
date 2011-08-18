package com.sftask.service;

import java.util.Timer;
import java.util.TimerTask;

import com.sftask.R;
import com.sftask.appwidget.ExampleAppWidgetProvider;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;

public class AppService extends Service {
	
	boolean running = false;
	boolean flag=true;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		registerReceiver(this.appReceiver, new IntentFilter(Intent.ACTION_MAIN));    
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
	}
	
	private BroadcastReceiver appReceiver = new BroadcastReceiver() {    
		public void onReceive(android.content.Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_MAIN)){
				Intent cintent = new Intent(context, ExampleAppWidgetProvider.class);
				cintent.setAction(ExampleAppWidgetProvider.COMMAND_REFRESH);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, cintent, PendingIntent.FLAG_UPDATE_CURRENT);
			}
		};	
	};

}
