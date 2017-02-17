package org.accapto.tool;

import java.io.File;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.accapto.helper.Logger;
import org.accapto.model.*;

/**
 * Creates a Java Model from a given XML File.
 * 
 * @author Elmar Krainz
 *
 */
public class ModelParser {

	// the package where the Java Classes are located
	private static final String SCHEMA = "org.accapto.model";

	private File xmlFile;	// XML input file
	private File xsdFile;	// XSD file for validating XML
	private AppType app;	// Java Model Container
	private Logger logger;
	
	public ModelParser(File xmlFile, Logger logger){
		this.xmlFile = xmlFile;
		this.xsdFile = new File("modelxml", "accapto_model.xsd");
		this.logger = logger;
	}

	
	public AppType parseDSL(){


		logger.log( "INFO Reading DSL file " + xmlFile.getName() + " ("+ xmlFile.length() + " Bytes) ..." );
		try {
			
			validateAgainstXSD(xmlFile, xsdFile);
			
			// Read Java Model Schema
			logger.onlyFile("INFO Reading DSL schema ...");
			JAXBContext jaxbContext = JAXBContext.newInstance(SCHEMA);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// Unmarshall XML file
			logger.onlyFile("INFO Marshalling DSL file ...");
			Object o = jaxbUnmarshaller.unmarshal(xmlFile);
			logger.onlyFile("     " + o);

			logger.onlyFile("INFO Creating model of app ...");
			@SuppressWarnings("unchecked")
			JAXBElement<AppType> root = (JAXBElement<AppType>) jaxbUnmarshaller.unmarshal(xmlFile);
			app = root.getValue();
			logger.onlyFile("     " + app);

			// Write information of model to console
			logger.log("--------------------------------------------------");
			logger.log("APP MODEL: ");
			logger.log("     Name: " + app.getAppname());
			logger.log("     Package: " + app.getPackage());
			//logger.log("     Number of screens: " + app.getScreen().size());

			// Write specified screens to console
			if (app.getScreen().size()>0){
				logger.log("     Screens: ");
				ScreenType screen = app.getScreen().get(0);
				for (int i = 0; i < app.getScreen().size(); i++) {
					screen = app.getScreen().get(i);
					logger.log("        > " + screen.getName());
				}
				logger.log("--------------------------------------------------");
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return app;		
	}

	
	
	private boolean validateAgainstXSD(File xmlFile, File xsdFile){
		try{
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new StreamSource(xsdFile));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlFile));
			logger.log("INFO Input file is valid.");
			return true;
		}catch(Exception ex){
			logger.logErr("ERROR Input file not valid. Please check input file and try again.");
			System.exit(1);
			return false;
		}
	}
	
	
	public AppType getApp() {
		return app;
	}
}
