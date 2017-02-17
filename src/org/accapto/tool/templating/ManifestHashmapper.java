package org.accapto.tool.templating;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.accapto.helper.Logger;
import org.accapto.model.AppType;
import org.accapto.model.ScreenType;

/**
 * Creates the hashmap for the manifest file.
 * @author Anja
 */
public class ManifestHashmapper extends Hashmapper{

	private String packageString;
	private String activities;
	private String permissions;

	private AppType app;
	private String intent;

	public ManifestHashmapper(AppType app, Logger logger){
		super(logger);
		this.app = app;
		generateValues();
		fillVars();
	}


	@Override
	public void generateValues() {
		packageString = getPackageString();
		activities = getActivities();
		permissions = getPermissions();

	}

	@Override
	public void fillVars() {
		vars.put("package", packageString);
		vars.put("activities", activities);
		vars.put("permissions", permissions);
	}

	private String getActivities() {
		OutputStream outputStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(outputStream);
		//Add intent to the first Screen
		intent = "\t\t\t<intent-filter>\n"
				+ "\t\t\t\t<action android:name=\"android.intent.action.MAIN\" />\n"
				+ "\t\t\t\t<category android:name=\"android.intent.category.LAUNCHER\" />\n"
				+ "\t\t\t</intent-filter>";

		for(ScreenType screen: app.getScreen()) {
			Map<String, Object> temp = new HashMap<>();
			temp.put("activity", screen.getName().substring(0, 1).toUpperCase() + screen.getName().substring(1) + "Activity");
			temp.put("intent", intent);
			processTemplating("manifest_activity.ftl", temp, writer);
			intent = "";
		}

		return outputStream.toString();
	}


	private String getPackageString() {
		return app.getPackage();
	}

	//To be implemented if needed
	private String getPermissions(){
		return "";
	}

	public void setIntent(){
		vars.put("intent", intent);
	}

}
