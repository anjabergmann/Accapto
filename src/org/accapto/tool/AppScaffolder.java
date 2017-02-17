package org.accapto.tool;

import java.io.File;
import java.io.IOException;

import org.accapto.helper.Logger;
import org.accapto.model.AppType;
import org.accapto.tool.templating.Templating;
import org.apache.commons.io.FileUtils;

/**
 * Create Scaffold for android Gradle Project
 * @author Anja
 */
public class AppScaffolder {

	private AppType app; // App model
	private String packageName; // Package of the app
	private Logger logger; // Logger object to write log messages into file and to sys.out
	private String appPath; // Path where app will be created
	private String activityPath; // Path to the activity files
	private String testPath; // Path to the test files

	public AppScaffolder(AppType app, Logger logger, String appPath){
		this.app = app;
		this.packageName = app.getPackage();
		this.logger = logger;
		this.appPath = appPath + File.separator + app.getAppname();
	}


	/**
	 * Generates a project that contains all necessary files and directories for a basic android app without business logic. 
	 */
	public void generate() {

		if (app.getAppname() != null && packageName!= null) {

			logger.log("INFO Creating app scaffold ...");

			copyAppFolder(); // Copy template app folder from Templates/DefaultApp to the given output path
			createPackageFolders(); // Add folders for the packages in 
			logger.initLog(appPath); // Create the log file at the app path

			// Add files to the project structure (manifest, activities, layouts)
			Templating templating = new Templating(app, logger, appPath, activityPath); 
			templating.startTemplating();
		}
		
		// Print some log messages
		logger.log("--------------------------------------------------");
		logger.onlyFile("INFO App scaffold created at " + appPath + ".");
		//Also write it to sys.out if verbose is not set
		System.out.println("INFO App scaffold created at " + appPath + ".");
	}
	
	
	/**
	 * Copys the template app folder to the given app directory
	 */
	public void copyAppFolder(){
		logger.onlyFile("     Setting up app folder at " + appPath + " ...");
		
		File srcDir = new File("." + File.separator + "Templates" + File.separator + "DefaultApp"); //Template app folder
		File destDir = new File(appPath); //Destination directory 
		
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
	

}