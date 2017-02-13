package org.accapto.tool;

import java.io.File;
import java.io.IOException;

import org.accapto.helper.Logger;
import org.accapto.model.AppType;
import org.accapto.tool.templating.Templating;
import org.apache.commons.io.FileUtils;

/**
 * Create Scaffold for android Gradle Project
 *
 */
public class AppScaffolder {

	private String appName;
	private String packageName;
	private AppType app;
	private Logger logger;
	private String appPath;
	private String activityPath;
	private String testPath;

	public AppScaffolder(AppType app, Logger logger, String appPath){
		this.app = app;
		this.appName = app.getAppname();
		this.packageName = app.getPackage();
		this.logger = logger;
		this.appPath = appPath + File.separator + appName;
		
	}


	/**
	 * Generates a project that contains all necessary files and directories for a basic android app without business logic. 
	 */
	public void generate() {

		if (appName != null && packageName!= null) {

			logger.log("INFO Creating app scaffold ...");

			copyAppFolder();
			createPackageFolders();
			logger.initLog(appPath);

			Templating templating = new Templating(app, logger, appPath, activityPath);
			templating.startTemplating();
		}

		logger.log("--------------------------------------------------");
		logger.log("INFO App scaffold created.");	
	}
	
	
	/**
	 * Copys the template app folder
	 */
	public void copyAppFolder(){
		logger.onlyFile("     Setting up app folder at " + appPath + " ...");
		File srcDir = new File("." + File.separator + "Templates" + File.separator + "DefaultApp");
		File destDir = new File(appPath);
		try {
			FileUtils.copyDirectory(srcDir, destDir);
			logger.onlyFile("     Sucessfully set up app folder.");
		} catch (IOException e) {
			e.printStackTrace();
			logger.logErr(e.toString());
			System.exit(1);
		}
	}
	
	
	/**
	 * Creates folders for the packages in app/src/main/java and app/src/test/java
	 */
	private void createPackageFolders() {
		//Creating package folders ------------------------------------------------------------------
		logger.onlyFile("     Creating package directories ...");
		
		activityPath = appPath + File.separator 
				+ "app" + File.separator 
				+ "src" + File.separator 
				+ "main" + File.separator 
				+ "java" + File.separator
				+ packageName.replace('.', File.separatorChar);
		testPath = appPath + File.separator 
				+ "app" + File.separator 
				+ "src" + File.separator 
				+ "test" + File.separator 
				+ "java" + File.separator
				+ packageName.replace('.', File.separatorChar);
		
		logger.onlyFile("         " + activityPath);
		logger.onlyFile("         " + testPath);
		
		new File(activityPath).mkdirs();
		new File(testPath).mkdirs();
		logger.onlyFile("     Successfully created package directories.");
	}
	

	
	//----- Getters and setters --------------------------------------


	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}