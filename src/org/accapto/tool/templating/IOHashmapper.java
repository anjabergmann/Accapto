package org.accapto.tool.templating;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.xml.bind.JAXBElement;

import org.accapto.helper.Logger;
import org.accapto.model.ActionType;
import org.accapto.model.InputType;
import org.accapto.model.OutputType;
import org.accapto.model.TransitionType;

/**
 * Creates the hashmap for input, output, action and transition types. 
 * @author Anja
 *
 */
public class IOHashmapper extends Hashmapper {

	// Values that are needed for the template
	private String name;
	private String nameNoSpace;
	private String description;
	private String function;
	private String transition;
	
	private JAXBElement<?> element; // Element from input xml file
	private String type; // Type of the element (input, output, action, transition)


	public IOHashmapper(JAXBElement<?> element, Logger logger){
		super(logger);
		this.element = element;
		this.type = "";

		determineType();
		generateValues();
		fillVars();
	}


	@Override
	public void generateValues() {
		name = getName();
		nameNoSpace = getNameNoSpace();
		description = getDescription();
		function = getFunction();
		transition = getTransition();
	}

	@Override
	public void fillVars() {
		// Not all values are needed for all element types but for convenience all types are treated the same 
		//(having unneeded key-value-pairs in the hashmap will NOT cause exceptions)
		vars.put("name", name);
		vars.put("namenospace", nameNoSpace);
		vars.put("description", description);
		vars.put("function", function);
		vars.put("transition", transition);
	}


	/**
	 * Returns a string with the complete layout of an <input>, <output>, or <action> element 
	 * @return
	 */
	public String getLayout(){
		OutputStream outputStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(outputStream);
		String template = "default.ftl"; // Define an empty default file in case non of the following if statements is fulfilled (which should not happen, but ...)

		//Choose appropriate template file
		if(type.equals("action")){
			template = "layout_button.ftl";
			logger.log("     Adding layout for button \"" + ((ActionType) element.getValue()).getFunction() + "\"");
		} else if (type.equals("output")){
			logger.log("     Adding layout for output element \"" + ((OutputType) element.getValue()).getName() + "\"");
			template = "layout_output.ftl";
		} else if (type.equals("input")){
			logger.log("     Adding layout for input element \"" + ((InputType) element.getValue()).getName() + "\"");
			template = "layout_input.ftl";
		} else if (type.equals("transition")){
			logger.log("     Adding layout for transition element \"" + (((TransitionType) element.getValue()).getName()) + "\"");
			template = "layout_transition.ftl";
		}

		processTemplating(template, vars, writer);

		return outputStream.toString();
	}



	/**
	 * Determines of which type the element is (input, output, action, or transition)
	 */
	private void determineType(){
		if((element.getName().toString().equals("{org.accapto}action"))){
			type = "action";
		} else if (element.getName().toString().equals("{org.accapto}output")){
			type = "output";
		} else if (element.getName().toString().equals("{org.accapto}input")){
			type = "input";
		} else if (element.getName().toString().equals("{org.accapto}transition")){
			type = "transition";
		}
	}
	
	
	private String getName(){
		String temp = "";
		if(type.equals("action")){
			temp = ((ActionType)element.getValue()).getName();
		} else if (type.equals("output")){
			temp = ((OutputType)element.getValue()).getName();
		} else if (type.equals("input")){
			temp = ((InputType)element.getValue()).getName();
		} else if (type.equals("transition")){
			temp = ((TransitionType)element.getValue()).getName();
		}
		return temp;
	}

	private String getNameNoSpace(){
		// Simply remove whitespaces from the name
		return name.replaceAll("\\s","");
	}

	private String getDescription(){
		String temp = "";
		if(type.equals("action")){
			temp = ((ActionType)element.getValue()).getDescription();
		} else if (type.equals("output")){
			temp = ((OutputType)element.getValue()).getDescription();
		} else if (type.equals("input")){
			temp = ((InputType)element.getValue()).getDescription();
		} else if (type.equals("transition")){
			temp = ((TransitionType)element.getValue()).getDescription();
		}
		return temp;
	}

	private String getFunction(){
		if(type.equals("action")){
			return ((ActionType)element.getValue()).getFunction();
		} 
		return "";
	}

	private String getTransition(){
		if(type.equals("transition")){
			return ((TransitionType)element.getValue()).getTarget();
		}
		return "";
	}
}
