package org.accapto.accessibility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Wrapper class to store profiles in SharedPrefs
 * 
 * @author KrainzE
 * 
 */
public class ProfileSettings {

	// Prefs Keys & values
	final static String KEY_PROFILE = "profile";
	final static String PROFILE_NORMAL="normal";

	private static final String SHARED_SETTINGS = "Profile_Settings";

	private SharedPreferences sPrefs;
	private Editor edPrfs;

	private String profile;


	/**
	 * Constructor to to access settings storage
	 * @param c Context of App component
	 */
	public ProfileSettings(Context c) {
		super();

		sPrefs = c.getSharedPreferences(SHARED_SETTINGS, 0);
		loadSettings();
	}



	//--- get profile from settings
	private void loadSettings() {
		// get from shared Prefs
		this.profile = (sPrefs.getString(KEY_PROFILE, PROFILE_NORMAL));
	}


	// -- save Settings to shared prefs
	private void saveSettings() {

		// save to shared Prefs
		edPrfs = sPrefs.edit(); // Prefs Editor

		// values
		edPrfs.putString(KEY_PROFILE, this.profile);
		edPrfs.commit();
	}


	// getter & Setter for Profile
	/**
	 * Getter for Profile in teh Settingswrapper
	 * @return Sting of saved profile
	 */
	public String getProfile() {
		return sPrefs.getString(KEY_PROFILE, "normal");
	}



	/**
	 * set profile to Settingswarpper an persit value
	 * @param valProfile String of Profile
	 */
	public void setProfile(String valProfile) {
		profile= valProfile;

		// save value to the persitance container (Shared Prefs)
		saveSettings();
	}


}
