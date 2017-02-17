package org.accapto.tool.templating;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import org.accapto.helper.Logger;
import org.accapto.model.*;

/**
 * Creates domain-specific scaffold by a Java Model.
 * 
 * @author Martin Fruhmann, Elmar Krainz, Anja Bergmann
 *
 */
public class Templating {

	private Configuration cfg;	// Freemarker configuration
	private AppType app;		// App model
	private PrintWriter output;	// Writer to write strings to files
	private Logger logger;		// Logger object to write log messages to sys.out and to log file
	private Path appdir;		// Directory where the app scaffold is located
	private Path activitydir;	// Directory inside the app scaffold folder where the activity files will be located
	private Path manifestdir;	// Directory inside the app scaffold where the manifest file will be located
	private ActivityHashmapper activity;	// Hashmapper object for creating activity files
	private LayoutHashmapper layout;		// Hashmapper object for creating layout files
	private ManifestHashmapper manifest;	// Hashmapper object for creating manifest files

	private final String ACTIVITY = "activity";
	private final String LAYOUT = "layout";
	private final String MANIFEST = "manifest";


	public Templating(AppType app, Logger logger, String appPath, String activityPath) {
		this.logger = logger;
		this.app = app;
		this.appdir = FileSystems.getDefault().getPath(appPath);
		this.activitydir = FileSystems.getDefault().getPath(activityPath);
		this.manifestdir = FileSystems.getDefault().getPath(appPath, "app" + File.separator + "src" + File.separator + "main");

		// Configuration of the Freemarker template engine
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		try {
			cfg.setDirectoryForTemplateLoading(new File("templates"));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Generates AndroidManifest.xml, activity files, and layout files.
	 */
	public void startTemplating(){

		//Create AndroidManifest.xml
		manifest = new ManifestHashmapper(app, logger);
		createFileOutputStream(null, MANIFEST);
		processTemplating("manifest.ftl", manifest.getVars(), output);

		//Loop through all screens
		for(ScreenType screen: app.getScreen()) {
			logger.log("--------------------------------------------------");
			logger.log("INFO Creating Files for screen: " + screen.getName() + " ...");

			// Create an activity file for the current screen
			activity = new ActivityHashmapper(app.getPackage(), screen, logger); 
			createFileOutputStream(screen, ACTIVITY);
			processTemplating("activity.ftl", activity.getVars(), output);
			
			// Create a layout file for the current screen
			layout = new LayoutHashmapper(screen, logger); 
			createFileOutputStream(screen, LAYOUT);
			processTemplating("layout_base.ftl", layout.getVars(), output);
		}
	}



	/**
	 * Starts freemarker templating engine for specified template & templating model.
	 * Writes Output into specified outputStream
	 * 
	 * @param templateString used Template
	 * @param root Templating Model
	 * @param outputstream OutputStream
	 */
	private void processTemplating(String templateString, Map<String, Object> root, Writer outputstream) {
		try {
			Template temp = cfg.getTemplate(templateString);
			temp.process(root, outputstream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Creates an OutputStreamWriter for the different files specified by the type.
	 * 
	 * @param a ScreenType from Java Model
	 * @param type Type of file
	 * @return OutputStreamWriter
	 */
	private void createFileOutputStream(ScreenType screen, String type) {

		File file = null;

		switch(type){
		case ACTIVITY:
			file = new File(activitydir + File.separator 
					+ screen.getName().substring(0, 1).toUpperCase() + screen.getName().substring(1) + "Activity.java");
			break;
		case LAYOUT:
			file = new File(appdir + File.separator 
					+ "app" + File.separator 
					+ "src" + File.separator
					+ "main" + File.separator
					+ "res" + File.separator
					+ "layout" + File.separator
					+ "activity_" + screen.getName().toLowerCase() + ".xml");
			break;
		case MANIFEST:
			file = new File(manifestdir + File.separator + "AndroidManifest.xml");
			break;
		}

		try {
			output = new PrintWriter(file);
			logger.onlyFile("     Created PrintStream at" + file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
