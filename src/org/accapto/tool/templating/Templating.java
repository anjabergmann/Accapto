package org.accapto.tool.templating;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import org.accapto.helper.Logger;
import org.accapto.model.*;

/**
 * Creates domain-specific scaffold by a Java Model.
 * 
 * @author Martin Fruhmann, Elmar Krainz
 *
 */
public class Templating {

	private Configuration cfg; // Freemarker configuration
	private AppType app;
	private PrintWriter output;
	private List<String> functions;
	private Logger logger;
	private Path appdir;
	private Path activitydir;
	private Path manifestdir;
	private MethodGenerator methodGenerator;
	private Activity activity;
	private Layout layout;
	private Manifest manifest;

	private final String ACTIVITY = "activity";
	private final String LAYOUT = "layout";
	private final String MANIFEST = "manifest";


	public Templating(AppType app, Logger logger, String appPath, String activityPath) {
		this.logger = logger;
		this.app = app;
		this.appdir = FileSystems.getDefault().getPath(appPath);
		this.activitydir = FileSystems.getDefault().getPath(activityPath);
		this.manifestdir = FileSystems.getDefault().getPath(appPath, "app" + File.separator + "src" + File.separator + "main");
		this.functions = new ArrayList<>();
		this.methodGenerator = new MethodGenerator(logger);

		// Configuration of the template engine
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
	 * Generates AndroidManifest.xml, activity and layout files.
	 * 
	 */
	public void startTemplating(){

		manifest = new Manifest(app, functions, logger, methodGenerator);
		createFileOutputStream(null, MANIFEST);
		processTemplating("manifest.ftl", manifest.getVars(), output);
		
		for(ScreenType screen: app.getScreen()) {
			logger.log("--------------------------------------------------");
			logger.log("INFO Creating Files for screen: " + screen.getName() + " ...");
			
			readFunctions(screen);
			activity = new Activity(app, screen, functions, logger, methodGenerator);
			layout = new Layout(app, screen, functions, logger, methodGenerator);
			
			createFileOutputStream(screen, ACTIVITY);
			processTemplating("activity.ftl", activity.getVars(), output);
			createFileOutputStream(screen, LAYOUT);
			processTemplating("layout_base.ftl", layout.getVars(), output);
		}
	}
	
	/**
	 * Save all functions of a screen into an ArrayList. 
	 * @param screen
	 */
	@SuppressWarnings("rawtypes")
	private void readFunctions(ScreenType screen){
		functions = new ArrayList<>();
		// Add Functions to Activity
		if(!screen.getContent().isEmpty()) {
			for(Object o: screen.getContent()) {
				if (o instanceof JAXBElement<?>){
						JAXBElement element = (JAXBElement) o;
					if (element.getName().toString().equals("{org.accapto}action")){
						String function = ((ActionType) element.getValue()).getFunction();
						logger.log("     Adding function: " + function);
						functions.add(function);
					} else if (((JAXBElement) o).getName().toString().equals("{org.accapto}output")){
						String name = ((OutputType) element.getValue()).getName();
						logger.log("     Creating output field \"" + name + "\" ...");
					} else if (((JAXBElement) o).getName().toString().equals("{org.accapto}input")){
						String name = ((InputType) element.getValue()).getName();
						logger.log("     Creating input field \"" + name + "\" ...");
					}
				}
			}
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
