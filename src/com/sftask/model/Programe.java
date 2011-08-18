package com.sftask.model;

import android.graphics.drawable.Drawable;

public class Programe {
	//ͼ��   
    private Drawable icon;   
    
    private int iconSource;
    //������   
    private String name;
    //������
    private String packagename;

    private int pid;
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIconSource() {
		return iconSource;
	}
	public void setIconSource(int iconSource) {
		this.iconSource = iconSource;
	}

}
