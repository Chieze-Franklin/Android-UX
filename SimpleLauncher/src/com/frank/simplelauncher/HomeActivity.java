package com.frank.simplelauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}
	
	public void showApps(View v) {
		try{
			Intent i = new Intent(this, AppsListActivity.class);
			startActivity(i);
		}
		catch(Exception e){
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}