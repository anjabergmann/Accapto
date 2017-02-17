package org.accapto.tool.templating;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.xml.bind.JAXBElement;

import org.accapto.helper.Logger;
import org.accapto.model.ActionType;
import org.accapto.model.InputType;
import org.accapto.model.OutputType;

/**
 * Creates the hashmap for input, output and action types. 
 * @author Anja
 *
 */
public class IOHashmapper extends Hashmapper {

	private String name;
	private String nameNoSpace;
	private String description;
	private String function;
	private JAXBElement<?> element;
	
	
	public IOHashmapper(JAXBElement<?> element, Logger logger){
		super(logger);
		this.element = element;
		generateValues();
		fillVars();
	}
	
	
	@Override
	public void generateValues() {
		name = getName();
		nameNoSpace = getNameNoSpace();
		description = getDescription();
		function = getFunction();
	}

	@Override
	public void fillVars() {
		vars.put("name", name);
		vars.put("namenospace", nameNoSpace);
		vars.put("description", description);
		vars.put("function", function);
	}
	
	
	private String getName(){
		if((element.getName().toString().equals("{org.accapto}action"))){
			return ((ActionType)element.getValue()).getName();
		} else if (element.getName().toString().equals("{org.accapto}output")){
			return ((OutputType)element.getValue()).getName();
		} else if (element.getName().toString().equals("{org.accapto}input")){
			return ((InputType)element.getValue()).getName();
		}
		return "";
	}
	
	private String getNameNoSpace(){
		return name.replaceAll("\\s","");
	}
	
	private String getDescription(){
		if((element.getName().toString().equals("{org.accapto}action"))){
			return ((ActionType)element.getValue()).getDescription();
		} else if (element.getName().toString().equals("{org.accapto}output")){
			return ((OutputType)element.getValue()).getDescription();
		} else if (element.getName().toString().equals("{org.accapto}input")){
			return ((InputType)element.getValue()).getDescription();
		}
		return "";
	}
	
	private String getFunction(){
		if((element.getName().toString().equals("{org.accapto}action"))){
			return ((ActionType)element.getValue()).getFunction();
		} 
		return "";
	}
	
	
	/**
	 * Returns a string with the layout of an <input>, <output>, or <action> element 
	 * @return
	 */
	public String getLayout(){
		OutputStream outputStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(outputStream);
		String template = "default.ftl";
		
		//Choose appropriate template file
		if((element.getName().toString().equals("{org.accapto}action"))){
			template = "layout_button.ftl";
			logger.log("     Adding layout for button \"" + ((ActionType) element.getValue()).getFunction() + "\"");
		} else if (element.getName().toString().equals("{org.accapto}output")){
			logger.log("     Adding layout for output element \"" + ((OutputType) element.getValue()).getName() + "\"");
			template = "layout_output.ftl";
		} else if (element.getName().toString().equals("{org.accapto}input")){
			logger.log("     Adding layout for input element \"" + ((InputType) element.getValue()).getName() + "\"");
			template = "layout_input.ftl";
		}
		
		processTemplating(template, vars, writer);
		
		return outputStream.toString();
	}

}
