package org.accapto.tool.templating;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.accapto.helper.Logger;
import org.accapto.model.AppType;
import org.accapto.model.ScreenType;

public class Manifest extends FileModel{

	private String packageString;
	private String activities;
	private String intent;
	private String permissions;
	
	public Manifest(AppType app, List<String> functions,Logger logger, MethodGenerator methodGenerator) {
		super(app, app.getScreen().get(0), functions, logger, methodGenerator);
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

	//TODO!!!
	private String getPermissions(){
		return "<uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\" />";
	}
	
	public void setIntent(){
		vars.put("intent", intent);
	}
	
}
