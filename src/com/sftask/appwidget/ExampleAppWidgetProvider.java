/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sftask.appwidget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sftask.R;
import com.sftask.model.Programe;
import com.sftask.utils.AppUtils;
import com.sftask.utils.IOUtils;
import com.sftask.utils.LogUtils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

/**
 * A widget provider. We have a string that we pull from a preference in order
 * to show the configuration settings and the current time when the widget was
 * updated. We also register a BroadcastReceiver for time-changed and
 * timezone-changed broadcasts, and update then too.
 * 
 * <p>
 * See also the following files:
 * <ul>
 * <li>ExampleAppWidgetConfigure.java</li>
 * <li>ExampleBroadcastReceiver.java</li>
 * <li>res/layout/appwidget_configure.xml</li>
 * <li>res/layout/appwidget_provider.xml</li>
 * <li>res/xml/appwidget_provider.xml</li>
 * </ul>
 */
public class ExampleAppWidgetProvider extends AppWidgetProvider {

	// log tag
	private static final String TAG = ExampleAppWidgetProvider.class.getName();

	public static HashMap<Integer, Integer> layouts = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> texts = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> btns = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> icons = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> ignores = new HashMap<Integer, Integer>();
	
	private static final String PREFIX="com.sftask";
	//command
	public static String COMMAND_IGNORE=PREFIX+"_ignore";
	public static String COMMAND_KILL=PREFIX+"_kill";
	public static String COMMAND_REFRESH=PREFIX+"_refresh";
	
	public static String igonreFile = "/sdcard/sf-config.txt";

	public static final int maxTaskCount = 4;
	
	List<String> ignoreList=allIgonre();

	static {
		layouts = new HashMap<Integer, Integer>();
		texts = new HashMap<Integer, Integer>();
		btns = new HashMap<Integer, Integer>();
		layouts.put(1, R.id.app_layout1);
		layouts.put(2, R.id.app_layout2);
		layouts.put(3, R.id.app_layout3);
		layouts.put(4, R.id.app_layout4);
		layouts.put(5, R.id.app_layout5);
		texts.put(1, R.id.app_text1);
		texts.put(2, R.id.app_text2);
		texts.put(3, R.id.app_text3);
		texts.put(4, R.id.app_text4);
		texts.put(5, R.id.app_text5);
		icons.put(1, R.id.app_icon1);
		icons.put(2, R.id.app_icon2);
		icons.put(3, R.id.app_icon3);
		icons.put(4, R.id.app_icon4);
		icons.put(5, R.id.app_icon5);
		btns.put(1, R.id.app_btn_close1);
		btns.put(2, R.id.app_btn_close2);
		btns.put(3, R.id.app_btn_close3);
		btns.put(4, R.id.app_btn_close4);
		btns.put(5, R.id.app_btn_close5);
		ignores.put(1, R.id.app_btn_igonre1);
		ignores.put(2, R.id.app_btn_igonre2);
		ignores.put(3, R.id.app_btn_igonre3);
		ignores.put(4, R.id.app_btn_igonre4);
		ignores.put(5, R.id.app_btn_igonre5);
	}

	public void refresh(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		try {
			RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider);
			invisableAllLayout(view);
			List<Programe> apps = allPrograme(context);
			for (int i = 1; i <= apps.size(); i++) {
				Programe app = apps.get(i - 1);
				initLayout(context, view, i, app);
			}
			//setRefreshBtn(context, view);
			appWidgetManager.updateAppWidget(appWidgetIds, view);
		} catch (Exception e) {
			LogUtils.error(e);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.d(TAG, "onUpdate");
		refresh(context, appWidgetManager, appWidgetIds);
		try {
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);     
			Intent intent = new Intent(context, ExampleAppWidgetProvider.class);
			intent.setAction(COMMAND_REFRESH);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.setRepeating(AlarmManager.RTC, 0, 5000, pendingIntent);
		} catch (Exception e) {
			LogUtils.error(e);
		}
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider);
		final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), ExampleAppWidgetProvider.class.getName());
		final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		if (intent.getAction().equalsIgnoreCase(COMMAND_KILL)) {	
			final int index=intent.getExtras().getInt("index");
			final int pid=intent.getExtras().getInt("pid");
			hideLayout(view, index);
			appWidgetManager.updateAppWidget(appWidgetIds, view);
			final Context tcontext=context;
			final Intent tintent=intent;
			new Thread(){
				@Override
				public void run() {
					ActivityManager mManager = (ActivityManager) tcontext.getSystemService(tcontext.ACTIVITY_SERVICE);
					String packagename=tintent.getExtras().getString("packagename");
					android.os.Process.killProcess(pid);
					mManager.killBackgroundProcesses(packagename);	
					refresh(tcontext, appWidgetManager, appWidgetIds);
					super.run();
				}
			}.start();			
		}
		if (intent.getAction().equalsIgnoreCase(COMMAND_IGNORE)) {
			int index=intent.getExtras().getInt("index");
			int pid=intent.getExtras().getInt("pid");
			hideLayout(view, index);
			appWidgetManager.updateAppWidget(appWidgetIds, view);
			String packagename=intent.getExtras().getString("packagename");
			String processname=intent.getExtras().getString("processname");
			addIgnore(packagename);	
			addIgnore(processname);
			
			ignoreList=allIgonre();
			refresh(context, appWidgetManager, appWidgetIds);
		}
		if (intent.getAction().startsWith(COMMAND_REFRESH)||intent.getAction().equals(Intent.ACTION_MAIN)) {
			System.out.println("COMMAND_REFRESH");
			refresh(context, appWidgetManager, appWidgetIds);
		}		
	}

	public void invisableAllLayout(RemoteViews view) {
		for (int layoutKey : layouts.keySet()) {
			int layoutId = layouts.get(layoutKey);
			view.setViewVisibility(layoutId, View.GONE);
		}
	}

	public void initLayout(Context context, RemoteViews view, int i, Programe app) {
		if (i > maxTaskCount) {
			return;
		}
		showLayout(view, i);
		setIcon(view, i, app);
		setText(view, i, app.getName());
		setLinkBtn(context, view, i, app.getPid(), app.getPackagename());
		setIgnoreBtn(context, view, i, app.getPid(), app.getPackagename(),app.getName());
	}

	public void showLayout(RemoteViews view, int i) {
		if (!layouts.containsKey(i)) {
			return;
		}
		int layoutId = layouts.get(i);
		view.setViewVisibility(layoutId, View.VISIBLE);
	}

	public void hideLayout(RemoteViews view, int i) {
		if (!layouts.containsKey(i)) {
			return;
		}
		int layoutId = layouts.get(i);
		view.setViewVisibility(layoutId, View.GONE);
	}

	public void setIcon(RemoteViews view, int i, Programe app) {
		if (!icons.containsKey(i)) {
			return;
		}
		int iconId = icons.get(i);
		view.setImageViewResource(iconId, app.getIconSource());
	}

	public void setText(RemoteViews view, int i, String text) {
		if (!texts.containsKey(i)) {
			return;
		}
		int textId = texts.get(i);
		view.setTextViewText(textId, text);
	}

	public void setLinkBtn(Context context, RemoteViews view, int i, int pid, String packagename) {
		if (!btns.containsKey(i)) {
			return;
		}
		int btnId = btns.get(i);
		Intent intent = new Intent(context, ExampleAppWidgetProvider.class);
		intent.setAction(COMMAND_KILL);
		Bundle bundle=new Bundle();
		bundle.putInt("pid",pid);
		bundle.putString("packagename", packagename);
		bundle.putInt("index", i);
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(btnId, pendingIntent);
	}

	public void setIgnoreBtn(Context context, RemoteViews view, int i, int pid, String packagename,String processname) {
		if (!ignores.containsKey(i)) {
			return;
		}
		int btnId = ignores.get(i);
		Intent intent = new Intent(context, ExampleAppWidgetProvider.class);
		intent.setAction(COMMAND_IGNORE);
		Bundle bundle=new Bundle();
		bundle.putInt("pid",pid);
		bundle.putString("packagename", packagename);
		bundle.putString("processname", processname);		
		bundle.putInt("index", i);
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(btnId, pendingIntent);
	}

	public void addIgnore(String packagename) {
		File file = new File(igonreFile);
		List<String> contents = new ArrayList<String>();
		try {
			contents = IOUtils.readLines(file);
		} catch (Exception e) {
			LogUtils.error(e);
		}
		if(!contents.contains(packagename)){
			contents.add(packagename);
			IOUtils.writeLines(file, contents);
		}		
	}

	public List<String> allIgonre() {
		try {
			return IOUtils.readLines(new File(igonreFile));
		} catch (Exception e) {
			LogUtils.error(e);//fix
		}
		return new ArrayList<String>();

	}

	public void setRefreshBtn(Context context, RemoteViews view) {
		/*
		int btnId = R.id.app_btn_refresh;
		Intent intent = new Intent(context, ExampleAppWidgetProvider.class);
		intent.setAction(COMMAND_REFRESH);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(btnId, pendingIntent);
		*/
	}

	public List<Programe> allPrograme(Context context) {
		ActivityManager aManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		PackageManager pm = context.getApplicationContext().getPackageManager();
		// init application info
		List<ApplicationInfo> appInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		List<RunningAppProcessInfo> runningApps = aManager.getRunningAppProcesses();
		List<Programe> list = new ArrayList<Programe>();
		for (RunningAppProcessInfo runningApp : runningApps) {
			try {
				String processName = runningApp.processName;
				Programe pr = new Programe();
				ApplicationInfo appInfo = getInfo(appInfoList, processName);
				if (appInfo == null ||  appInfo.packageName== null) {
					continue;
				}
				String packageName=appInfo.packageName;
				if(packageName.equals(PREFIX)){
					continue;
				}
				if (AppUtils.isSystemApplication(context,packageName)) {
					continue;
				}
				if (ignoreList.contains(packageName)) {
					continue;
				}
				for (String igonre : ignoreList) {
					if (packageName.startsWith(igonre)) {
						continue;
					}
				}
				pr.setIconSource(appInfo.icon);
				pr.setName(appInfo.loadLabel(context.getPackageManager()).toString());
				pr.setPackagename(packageName);
				pr.setPid(runningApp.pid);
				list.add(pr);
			} catch (Exception e) {
				LogUtils.error(e);
			}
		}
		return list;
	}

	public ApplicationInfo getInfo(List<ApplicationInfo> appList, String name) {
		if (name == null) {
			return null;
		}
		for (ApplicationInfo appinfo : appList) {
			if (name.equals(appinfo.processName)) {
				return appinfo;
			}
		}
		return null;
	}
	
	public void updateApp(Context context,RemoteViews view){
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), ExampleAppWidgetProvider.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		appWidgetManager.updateAppWidget(appWidgetIds, view);
	}

}
