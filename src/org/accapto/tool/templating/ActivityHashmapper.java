package org.accapto.tool.templating;

import java.util.List;

import org.accapto.helper.Logger;
import org.accapto.model.AppType;
import org.accapto.model.ScreenType;

/**
 * 
 * @author Anja
 *
 */
public class ActivityHashmapper extends Hashmapper {
	
	private String packageString;
	private String activity;
	private String imports;
	private String variables;
	private String layout;
	private String onCreate;
	private String methods;
	
	public ActivityHashmapper(AppType app, ScreenType screen, List<String> functions, Logger logger, MethodGenerator methodGenerator){
		super(app, screen, functions, logger, methodGenerator);
	}

	
	@Override
	public void generateValues(){
		packageString = getPackageString();
		activity = getActivity();
		imports = getImports();
		variables = getVariables();
		layout = getLayout();
		onCreate = getOnCreate();
		methods = getMethods();
	}
	
	@Override
	public void fillVars(){
		vars.put("package", packageString);
		vars.put("activity", activity);
		vars.put("imports", imports);
		vars.put("variables", variables);
		vars.put("layout", layout);
		vars.put("onCreate", onCreate);
		vars.put("methods", methods);
	}
	
	private String getPackageString(){
		return app.getPackage();
	}
	
	private String getActivity(){
		return screen.getName().substring(0, 1).toUpperCase() + screen.getName().substring(1) + "Activity";
	}
	
	private String getImports(){
		String temp = "";
		
		if (screen.getTransition() != null){
			temp = "import android.view.View;\n"
					+ "import android.content.Intent;"; 
		}
		
		for(String function : functions){
			temp += methodGenerator.lookForMethod(function, "imports");
		}
		
		return temp;
	}
	
	private String getVariables(){
		String temp = "";
		for (String function : functions){
			temp += methodGenerator.lookForMethod(function, "variables");
		}
		return temp;
	}
	
	private String getLayout(){
		return screen.getName().toLowerCase();
	}

	private String getOnCreate(){
		String temp = "";
		for(String function : functions){
			temp += methodGenerator.lookForMethod(function, "onCreate");
		}
		return temp;
	}
	
	private String getMethods(){
		String temp = "";
		for(String function : functions){
			temp += methodGenerator.lookForMethod(function, "code");
		}
		return temp;
	}
	
}
