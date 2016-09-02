package com.frank.simplelauncher;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private String appName = "";
	private String packageName = "";
	private String className = "";
	private String versionName = "";
	private Integer versionCode = 0;
	private Drawable icon = null;
	
	public String getAppName(){
		return appName;
	}
	public void setAppName(String appname){
		this.appName = appname;
	}
	
	public String getPackageName(){
		return packageName;
	}
	public void setPackageName(String packagename){
		this.packageName = packagename;
	}
	
	public String getClassName(){
		return className;
	}
	
	public String getVersionName(){
		return versionName;
	}
	public void setVersionName(String versionname){
		this.versionName = versionname;
	}
	
	public Integer getVersionCode(){
		return versionCode;
	}
	public void setVersionCode(Integer versioncode){
		this.versionCode = versioncode;
	}
	
	public Drawable getIcon(){
		return icon;
	}
	public void setIcon(Drawable icon){
		this.icon = icon;
	}
}