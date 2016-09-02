package com.karabow.screendecor;

import android.os.Bundle;
//import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		this.addPreferencesFromResource(R.xml.settings);
		
//		//add a validator to the number of devices pref so that it only accepts numbers
//		Preference devicePref = this.getPreferenceScreen()
//				.findPreference(this.getString(R.string.key_max_num_of_wall_images));
//		//add the validator
//		devicePref.setOnPreferenceChangeListener(numberCheckListener);
	}
	
//	Preference.OnPreferenceChangeListener numberCheckListener = new
//			Preference.OnPreferenceChangeListener(){
//		public boolean onPreferenceChange(Preference pref, Object newValue){
//			//check that the string is an integer
//			if (newValue != null && newValue.toString().length() > 0 &&
//					newValue.toString().matches("\\d*")){
//				return true;
//			}
//			//if not create a message to the user
//			Toast.makeText(SettingsActivity.this, R.string.invalid_input, Toast.LENGTH_SHORT)
//				.show();
//			return false;
//		}
//	};
}