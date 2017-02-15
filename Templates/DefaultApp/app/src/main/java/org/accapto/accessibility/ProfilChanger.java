package org.accapto.accessibility;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.accapto.R;


/**
 * @author KrainzE
 *         <p/>
 *         Helper class to encapsulate change the theme based on a profile
 *         The last selected profile is also prsisted with the help of the ProfileSettings
 */
public class ProfilChanger {

    protected ProfileBaseActivity contextActivity;
    private ProfileSettings settings;
    private String profile = null;


    /**
     * Profile Changer uses the available Profiles to change the Theme of an Activity
     *
     * @param a Activiy, which changes the theme based on a profile
     */
    public ProfilChanger(Activity a) {

        this.contextActivity = (ProfileBaseActivity) a;
        settings = new ProfileSettings(a);

    }


    /**
     * Changes the theme of an activity based on an given profile and saves the profile in the settings
     *
     * @param profile String to select a defined profile see class Profiles
     */
    public void changeProfile(String profile) {

        if (profile.equals(Profiles.PROFILE_NORMAL)) {
            this.contextActivity.setTheme(R.style.pons_normal);
        } else if (profile.equals(Profiles.PROFILE_BLIND)) {
            this.contextActivity.setTheme(R.style.pons_blind);
        } else if (profile.equals(Profiles.PROFILE_DEFECT)) {
            this.contextActivity.setTheme(R.style.pons_defect);
        }

        // recall oncreate
        this.contextActivity.onCreate(new Bundle());

        // persit settings
        settings.setProfile(profile);
    }


    /**
     * load stored settings to the activity
     */
    public void loadSavedProfile() {

        if (profile == null) {
            profile = settings.getProfile();
        }
        Log.i("PROFILE", "profile" + profile);

        if (profile.equals(Profiles.PROFILE_NORMAL)) {
            this.contextActivity.setTheme(R.style.pons_normal);
        } else if (profile.equals(Profiles.PROFILE_DEFECT)) {
            this.contextActivity.setTheme(R.style.pons_defect);
        } else if (profile.equals(Profiles.PROFILE_BLIND)) {
            this.contextActivity.setTheme(R.style.pons_blind);
        }
    }

}
