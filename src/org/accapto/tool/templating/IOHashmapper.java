package org.accapto.tool.templating;

import javax.xml.bind.JAXBElement;

import org.accapto.helper.Logger;
import org.accapto.model.ActionType;
import org.accapto.model.InputType;
import org.accapto.model.OutputType;

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

}
