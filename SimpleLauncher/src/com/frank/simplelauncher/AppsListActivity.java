package com.frank.simplelauncher;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class AppsListActivity extends Activity {

	private PackageManager manager;
	private ArrayList<AppInfo> apps;
	
	private ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.activity_apps_list);

			loadApps();
			loadListView();
			addClickListener();
		}
		catch(Exception e){
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	private void loadApps(){
		manager = getPackageManager();
		apps = new ArrayList<AppInfo>();
		
		Intent i = new Intent(Intent.ACTION_MAIN, null);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		
		List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
		for(ResolveInfo ri : availableActivities){
			AppInfo app = new AppInfo();
			app.setAppName(ri.loadLabel(manager).toString());
			app.setPackageName(ri.activityInfo.packageName);
			app.setIcon(ri.activityInfo.loadIcon(manager));
			apps.add(app);
		}
		
//		List<PackageInfo> packs = manager.getInstalledPackages(0);
//		for(int i = 0; i < packs.size(); i++){
//	        PackageInfo packInfo = packs.get(i);
//	        
//	        if(/*(!getSysPackages) && */(packInfo.versionName == null)){
//	            continue ;
//	        }
//	        
//	        AppInfo app = new AppInfo();
//	        
//	        app.setAppName(packInfo.applicationInfo.loadLabel(getPackageManager()).toString());
//	        app.setPackageName(packInfo.packageName);
//	        app.setVersionName(packInfo.versionName);  
//	        app.setVersionCode(packInfo.versionCode);  
//	        app.setIcon(packInfo.applicationInfo.loadIcon(getPackageManager()));
//	        
//	        apps.add(app);
//	    }
	}
	private void loadListView(){
		try{
			list = (ListView)findViewById(R.id.apps_list);
			
			AppListAdapter adapter = new AppListAdapter(this);
			adapter.setData(apps);
			
			list.setAdapter(adapter);
		}
		catch(Exception e){
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void addClickListener() {
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
				Intent i = manager.getLaunchIntentForPackage(apps.get(pos).getPackageName().toString());
				AppsListActivity.this.startActivity(i);
			}
		});
	}
}