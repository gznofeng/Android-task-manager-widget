package com.sftask.utils;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class AppUtils {

	public static final String TAG = AppUtils.class.getName();

	public static boolean isSystemApplication(Context context, String packageName) {
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo packageInfo = manager.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
			if (new File("/data/app/" + packageInfo.packageName + ".apk").exists()) {
				//Log.e(TAG, "Check application in the way of 1 ,package:" + packageName);//1
				return true;
			}
			if (packageName.startsWith("com.android") || packageName.startsWith("android") || packageName.startsWith("system")) {
				//Log.e(TAG, "Check application in the way of 2 ,package:" + packageName);//2
				return true;
			}
			if ((packageInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0) {
				//Log.e(TAG, "Check application in the way of 3 ,package:" + packageName);//3
				return true;
			}
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
				//Log.e(TAG, "Check application in the way of 4 ,package:" + packageName);//4
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
