package org.accapto.tool.modeling;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.accapto.model.*;

/**
 * Creates a Java Model from a given XML File.
 * 
 * @author Martin Fruhmann
 *
 */
public class ModelParser {
	
	// the package where the Java Classes are located
	private static final String SCHEMA = "whereami";
	
	// XML file to parse
	private File xmlFile;
	// Java Model Container
	private AppType app;
	
	public ModelParser(File dslFile){
		xmlFile = dslFile;
	}
	
	public AppType parseDSL(){

		if (xmlFile.exists()) {
			System.out.println( ".. read dsl file \n" + xmlFile.getName() + ", size "+ xmlFile.length() );
			try {
				// Read Java Model Schema
				System.out.println(".. read DSL schema");
				JAXBContext jaxbContext = JAXBContext.newInstance(SCHEMA);
				Unmarshaller jaxbUnmarshaller = jaxbContext
						.createUnmarshaller();

				// Unmarshall XML file
				Object o = jaxbUnmarshaller.unmarshal(xmlFile);
				System.out.println(".. marshalling dsl file");
				System.out.println(o);

				@SuppressWarnings("unchecked")
				JAXBElement<AppType> root = (JAXBElement<AppType>) jaxbUnmarshaller
						.unmarshal(xmlFile);
				app = root.getValue();

				System.out.println(".. Modell of App");
				System.out.println(app);
				
				// Write information of model to console
				System.out.println("\n APP MODEL:");
				System.out.println(app.getAppname());
				System.out.println(app.getPackage());
				System.out.println(app.getScreen().size() + " screens");
				
				// Write specified screens to console
				if (app.getScreen().size()>0){
				System.out.println("\nAll Screens:");
				ScreenType screen = app.getScreen().get(0);
					for (int i = 0; i < app.getScreen().size(); i++) {
						screen = app.getScreen().get(i);
						System.out.println(screen.getName());
					}
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("no file");
		}
		return app;		
	}

	public AppType getApp() {
		return app;
	}
}
