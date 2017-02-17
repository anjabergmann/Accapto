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

	private String packageString;
	private String activity;
	private String imports;
	private String variables;
	private String layout;
	private String methods;
	private ScreenType screen;



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

	private String getImports(){
		String temp = "";
		//Not working for some reason ...
		if (screen.getTransition() != null){
			temp = "import android.view.View;\n"
					+ "import android.content.Intent;"; 
		}
		return temp;
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
				//Create a method stub for all <action> objects
				if (o instanceof JAXBElement<?> && (((JAXBElement<?>)o).getName().toString().equals("{org.accapto}action") || ((JAXBElement<?>)o).getName().toString().equals("{org.accapto}transition"))){
					@SuppressWarnings("rawtypes")
					JAXBElement element = (JAXBElement) o;
					if (((JAXBElement<?>)o).getName().toString().equals("{org.accapto}action")){
						String function = ((ActionType) element.getValue()).getFunction();
						temp += "\tpublic void " + function + "() {\n\t\t//TODO: auto-generated method stub\n\t}\n\n";
						logger.log("     Adding method stub for method " + function + "()");
					} else {
						//transition
						String target = ((TransitionType) element.getValue()).getTarget();
						temp += "\tpublic void goTo" + target + "(View view){\n\t\tIntent intent" + target + " = new Intent(this, " + target +"Activity.class);\n\t\tstartActivity(intent" + target + ");\n\t}";
						addImports();
						logger.log("     Adding intent for transition goTo" + target);
					}
				}
			}
		}

		return temp;
	}

}
