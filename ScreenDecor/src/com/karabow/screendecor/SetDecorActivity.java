package com.karabow.screendecor;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.View.OnClickListener;
import android.widget.Toast;

public class SetDecorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.main);
		this.findViewById(R.id.btn_close).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				SetDecorActivity.this.finish();
			}
		});
	}
	
	public void setScreen(View view) {
		try {
			Intent intent;
			if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
				intent = new Intent(Settings.ACTION_DREAM_SETTINGS);
			} else {
				intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
			}
			this.startActivity(intent);
		}
		catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void setWall(View view) {
		try {
			Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, 
					new ComponentName(this, KarabowWallpaperService.class));
			this.startActivity(intent);
		}
		catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}