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
public class Layout extends FileModel {
	
	private String additionalLayout;
	
	public Layout(AppType app, ScreenType screen, List<String> functions,Logger logger, MethodGenerator methodGenerator){
		super(app, screen, functions, logger, methodGenerator);
	}

	
	@Override
	public void generateValues(){
		additionalLayout = getAdditionalLayout();
	}

	@Override
	public void fillVars(){
		vars.put("additionalLayout", additionalLayout);
	}
	
	private String getAdditionalLayout() {
		String temp = "";

		for(String function : functions){
			temp += methodGenerator.lookForMethod(function, "layout");
		}
		
		return temp;
	}
	
}
