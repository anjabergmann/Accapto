package org.accapto.accessibility;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;




/**
 * @author KrainzE
 *         <p/>
 *         Base class for dynamic change of activity theme
 */
public class ProfileBaseActivity extends Activity {

    private static final String TAG = "PROFILE";
    protected ProfilChanger profileChanger;


    /**
     * is public not protected to use call it in ProfilChanger
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (profileChanger == null) {

            Log.d(TAG, "create profilechanger");

            profileChanger = getProfileChanger();
            profileChanger.loadSavedProfile();
        }

    }


    // 	get or create instance of Profilchanger
    protected ProfilChanger getProfileChanger() {
        if (this.profileChanger == null) {
            this.profileChanger = new ProfilChanger(this);
        }
        return this.profileChanger;
    }


}
