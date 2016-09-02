package com.karabow.screendecor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.Button;

public class PowerConnectionReceiver extends BroadcastReceiver {

	public static KarabowDreamService.Bouncer bouncer;
	public static Button btnPauseResume;
	public static int minimumBatteryLevel;
	@Override
	public void onReceive(Context context, Intent intent) {
		
//		int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
//				status == BatteryManager.BATTERY_STATUS_FULL;
//		
//		int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
//		boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
//		boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
//		boolean wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;
//		
		int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		float batteryPct = level / (float)scale;
		
		@SuppressWarnings("deprecation")
		int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		if(sdkVersion >= Build.VERSION_CODES.KITKAT){
			if (batteryPct < minimumBatteryLevel){
				if(bouncer != null)
					bouncer.pauseAnimation();
				if(btnPauseResume != null)
					btnPauseResume.setText(R.string.resume_anim);
			}
		}
	}
}
