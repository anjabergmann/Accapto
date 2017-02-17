package org.accapto.tool.templating;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.accapto.helper.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Generates a hashmap for Freemarker template engine. 
 * The keys in the generated hashmap are the placeholders from a specific Freemarker template. 
 * The values are the Strins that will be put into the template instead of the placeholder. 
 * @author Anja
 *
 */
public abstract class Hashmapper {

	protected Map<String, Object> vars;
	protected Logger logger;
	protected Configuration cfg;
	
	/**
	 * Generates a standard configuration for Freemarker. 
	 * @param logger
	 */
	public Hashmapper(Logger logger){
		this.logger = logger;
		this.vars = new HashMap<>();
		
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
	 * Generates the values that will be put into the Freemarker template.
	 */
	public abstract void generateValues();
	
	/**
	 * Fills the hashmap with the appropriate keys for a Freemarker template and the generated values. 
	 */
	public abstract void fillVars();
	
	
	
	/**
	 * Starts freemarker templating engine for specified template & templating model.
	 * Writes Output into specified outputStream
	 * 
	 * @param templateString used Template
	 * @param root Templating Model
	 * @param outputstream OutputStream
	 */
	protected void processTemplating(String templateString, Map<String, Object> root, Writer outputstream) {
		try {
			Template temp = cfg.getTemplate(templateString);
			temp.process(root, outputstream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}	
	
	
	
	// --- Getter and setter -------------------------------------------------
	
	public Map<String, Object> getVars() {
		return vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}
}
