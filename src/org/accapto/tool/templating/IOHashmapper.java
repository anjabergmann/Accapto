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
 * Creates the hashmap for input, output and action types. 
 * @author Anja
 *
 */
public class IOHashmapper extends Hashmapper {

	private String name;
	private String nameNoSpace;
	private String description;
	private String function;
	private String transition;
	private String type;
	private JAXBElement<?> element;


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
		vars.put("name", name);
		vars.put("namenospace", nameNoSpace);
		vars.put("description", description);
		vars.put("function", function);
		vars.put("transition", transition);
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

	/**
	 * Returns a string with the layout of an <input>, <output>, or <action> element 
	 * @return
	 */
	public String getLayout(){
		OutputStream outputStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(outputStream);
		String template = "default.ftl";

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
	 * Determine of which type the element is (input, output, action, or transition)
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
}
