package org.accapto.accessibility;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.accapto.R;

import java.util.Locale;



public class A12ySettingsActivity extends ProfileBaseActivity implements TextToSpeech.OnInitListener {

	RadioGroup radioGroupProfile;
	private ProfilChanger pc;

	private int DATA_CHECK_CODE = 0;
	private TextToSpeech tts;
	private boolean isTalkBackActive;
	private boolean TextToSpeechActive;
	private SharedPreferences prefs;
	private CheckBox cbxTextToSpeech, cbxTalkBack;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Fix the orientation on the current one
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		prefs= PreferenceManager.getDefaultSharedPreferences(this);
		setTextToSpeechActive(prefs.getBoolean("isTextToSpeechActive",false));

		//for Text to Speech engine
		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, DATA_CHECK_CODE);
		tts=new TextToSpeech(this,this);

		setContentView(R.layout.activity_select_theme);

		radioGroupProfile = (RadioGroup) findViewById(R.id.radioGroupProfile);

		cbxTextToSpeech=(CheckBox) findViewById(R.id.cbxTextToSpeech);
		cbxTalkBack=(CheckBox) findViewById(R.id.cbxTalkBack);


		//when another radiobutton is checked, the profile in the Profile Changer gets changed
		radioGroupProfile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton) group.findViewById(checkedId);

				if (null != rb && checkedId > -1) {
					pc = getProfileChanger();
					if (radioGroupProfile.getCheckedRadioButtonId() == R.id.radioButton1) {
						Toast.makeText(getApplicationContext(), "Standard", Toast.LENGTH_LONG).show();
						//Problem, dass der ausgewaehlte Radiobutton nicht geaendert wird, tritt erst auf, wenn man changeProfile aufruft
						pc.changeProfile(Profiles.PROFILE_NORMAL);
					} else if (radioGroupProfile.getCheckedRadioButtonId() == R.id.radioButton3) {
						Toast.makeText(getApplicationContext(), "Schwarz-Weiss", Toast.LENGTH_LONG).show();
						pc.changeProfile(Profiles.PROFILE_BLIND);
					} else if (radioGroupProfile.getCheckedRadioButtonId() == R.id.radioButton4) {
						Toast.makeText(getApplicationContext(), "Gelb-Schwarz", Toast.LENGTH_LONG).show();
						pc.changeProfile(Profiles.PROFILE_DEFECT);
					}

				}

			}
		});

		//sets the Shared Preferences for TextToSpeech depending on the state of the Checkbox
		cbxTextToSpeech.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					prefs.edit().putBoolean("isTextToSpeechActive",true).commit();
					Log.i("PREFERNCES", "true");
				}else if(!isChecked){
					prefs.edit().putBoolean("isTextToSpeechActive",false).commit();
					Log.i("PREFERNCES", "false");
				}
			}
		});


		//onClickListener da sonst immer in die Setting gegangen wird
		cbxTalkBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent dialogIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
				dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(dialogIntent);

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		changeCheckedTextToSpeech();
		changeCheckedTalkBack();
	}

	public void reStarter(View v) {
		restart();
	}
	public void backSave(View v) {
		v.announceForAccessibility("foobar");//this.getText(R.string.rounting_start));
		restart();
	}

	/**
	 * checks if the Shared Preference of TextToSpeech is active or not
	 * changes the value of the checkbox according the Shared Preferene
	 */
	public void changeCheckedTextToSpeech(){
		Boolean checked=prefs.getBoolean("isTextToSpeechActive",false);
		if(checked){
			cbxTextToSpeech.setChecked(true);
		}else
			cbxTextToSpeech.setChecked(false);
	}

	/**
	 * checks the status of TalkBack and changes the value of the checkbox
	 */
	public void changeCheckedTalkBack(){
		AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
		isTalkBackActive = am.isEnabled();
		if(isTalkBackActive){
			cbxTalkBack.setChecked(true);
		}else
			cbxTalkBack.setChecked(false);

	}


/*	public void checkTalkBack(View v){
		AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
		isTalkBackActive = am.isEnabled();

		Log.i("TalkBack aktiviert:", isTalkBackActive + "");

		if(isTalkBackActive==false){
			AlertDialog.Builder alert = new AlertDialog.Builder(ThemeSelectorActivity.this,R.style.pons_dialog);
			alert.setMessage(R.string.start_TalkBack);
			alert.setPositiveButton(R.string.answer_ja, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent dialogIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
					dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(dialogIntent);
				}
			});
			alert.setNegativeButton(R.string.answer_nein, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			alert.show();

		}else if(isTalkBackActive){
			AlertDialog.Builder alert = new AlertDialog.Builder(ThemeSelectorActivity.this,R.style.pons_dialog);
			alert.setMessage(R.string.stop_TalkBack);
			alert.setPositiveButton(R.string.answer_ja, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent dialogIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
					dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(dialogIntent);
				}
			});
			alert.setNegativeButton(R.string.answer_nein, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			alert.show();
		}
	}
*/
	private void restart() {
		Intent i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
/*
	public void openProfileChanger(View v){
		v.announceForAccessibility(this.getString(R.string.route_selector));
		Intent i=new Intent(this, RoutingProfileActivity.class);
		startActivity(i);
	}
*/
	public boolean getTextToSpeechBoolean(){
		return TextToSpeechActive;
	}

	public void setTextToSpeechActive(boolean textToSpeechActive){this.TextToSpeechActive=textToSpeechActive;}

	@Override
	public void onInit(int status) {
		if(TextToSpeechActive) {
			if (status == TextToSpeech.SUCCESS) {
				tts.setLanguage(Locale.GERMAN);
			// ==========ACHTUNG=========	tts.speak(this.getString(R.string.TTS_Text), TextToSpeech.QUEUE_FLUSH, null);
			} else if (status == TextToSpeech.ERROR) {
				Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
		tts.stop();
		tts.shutdown();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(tts != null) {
			tts.stop();
			tts.shutdown();
		}

	}

	@Override
	protected void onPause(){
		super.onPause();
		tts.stop();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		restart();
	}
}