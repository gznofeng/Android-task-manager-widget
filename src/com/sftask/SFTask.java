package com.sftask;

import java.util.ArrayList;
import java.util.List;

import com.sftask.model.Programe;
import com.sftask.utils.LogUtils;
import com.sftask.view.ProgrameListView;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SFTask extends ListActivity {
	/** Called when the activity is first created. */
	ActivityManager mManager;
	private static final String LogTag = "SFTASK";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			mManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			Log.d(LogTag, "mManager is null?" + mManager.toString());
			List<RunningAppProcessInfo> mRunningTasks = mManager
					.getRunningAppProcesses();
			List<Programe> list = new ArrayList<Programe>();

			for (RunningAppProcessInfo runningAppProcessInfo : mRunningTasks) {
				Log.d(LogTag, "mRunningTasks size?" + runningAppProcessInfo);
				Programe pr = new Programe();
				// pr.setIcon(getInfo(runningAppProcessInfo.processName).loadIcon(this.getPackageManager()));
				if (getInfo(runningAppProcessInfo.processName) != null) {
					pr.setName(getInfo(runningAppProcessInfo.processName)
							.loadLabel(this.getPackageManager()).toString());
					list.add(pr);
				}
			}
			Log.d(LogTag, "mRunningTasks size?" + mRunningTasks.size());
			// setContentView(R.layout.programe_list_view);
			ProgrameListView view = new ProgrameListView(list, this);
			getListView().setAdapter(view);

		} catch (Exception e) {
			LogUtils.error(e);
		}
	}

	public ApplicationInfo getInfo(String name) {
		try {
			PackageManager pm = this.getApplicationContext()
					.getPackageManager();
			List<ApplicationInfo> appList = pm
					.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
			if (name == null) {
				return null;
			}
			for (ApplicationInfo appinfo : appList) {
				if (name.equals(appinfo.processName)) {
					return appinfo;
				}
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
		}
		return null;
	}
}