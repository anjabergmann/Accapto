package org.accapto.tool.templating;

import javax.xml.bind.JAXBElement;

import org.accapto.helper.Logger;
import org.accapto.model.ActionType;
import org.accapto.model.ScreenType;
import org.accapto.model.TransitionType;

/**
 * Creates the hashmap for an activity file. 
 * @author Anja
 */
public class ActivityHashmapper extends Hashmapper {

	// Values that are needed for the template
	private String packageString;
	private String activity;
	private String imports;
	private String variables;
	private String layout;
	private String methods;

	private ScreenType screen; // Model of the current screen



	public ActivityHashmapper(String packageString, ScreenType screen, Logger logger){
		super(logger);

		this.packageString = packageString;
		this.screen = screen;

		generateValues();
		fillVars();
	}


	@Override
	public void generateValues(){
		activity = getActivity();
		imports = getImports();
		variables = getVariables();
		layout = getLayout();
		methods = getMethods();
	}

	@Override
	public void fillVars(){
		vars.put("package", packageString);
		vars.put("activity", activity);
		vars.put("imports", imports);
		vars.put("variables", variables);
		vars.put("layout", layout);
		vars.put("methods", methods);
	}




	private String getActivity(){
		return screen.getName().substring(0, 1).toUpperCase() + screen.getName().substring(1) + "Activity";
	}

	//To be implemented if needed
	private String getImports(){
		return "";
	}


	private void addImports(){
		imports = "import android.view.View;\n"
				+ "import android.content.Intent;"; 
	}

	//To be implemented if needed
	private String getVariables(){
		return "";
	}

	private String getLayout(){
		return screen.getName().toLowerCase();
	}


	private String getMethods(){
		String temp = "";

		if(!screen.getContent().isEmpty()) {
			//Loop through screen objects
			for(Object o: screen.getContent()) {
				
				if (o instanceof JAXBElement<?>){
					JAXBElement<?> element = (JAXBElement<?>) o;
					
					//Create a method stub for all <action> objects
					if (((JAXBElement<?>)o).getName().toString().equals("{org.accapto}action")){
						String function = ((ActionType) element.getValue()).getFunction();
						temp += "\tpublic void " + function + "() {\n\t\t//TODO: auto-generated method stub\n\t}\n\n";
						logger.log("     Adding method stub for method " + function + "()");
					// Create intent for <transition> objects
					} else if (((JAXBElement<?>)o).getName().toString().equals("{org.accapto}transition")){
						String target = ((TransitionType) element.getValue()).getTarget();
						temp += "\tpublic void goTo" + target + "(View view){\n\t\tIntent intent" + target + " = new Intent(this, " + target +"Activity.class);\n\t\tstartActivity(intent" + target + ");\n\t}\n\n";
						addImports(); //Add the needed imports for a transition
						logger.log("     Adding intent for transition goTo" + target);
					}
				}
			}
		}

		return temp;
	}

}
