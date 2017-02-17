package org.accapto.tool.templating;

import javax.xml.bind.JAXBElement;

import org.accapto.helper.Logger;
import org.accapto.model.ScreenType;

/**
 * Creates the hashmap for a layout file. 
 * @author Anja
 *
 */
public class LayoutHashmapper extends Hashmapper {

	private String layout;
	private ScreenType screen;

	public LayoutHashmapper(ScreenType screen, Logger logger){
		super(logger);
		this.screen = screen;
		generateValues();
		fillVars();
	}



	@Override
	public void generateValues(){
		layout = getLayout();
	}

	@Override
	public void fillVars(){
		vars.put("layout", layout);
	}

	private String getLayout() {
		String temp = "";

		if(!screen.getContent().isEmpty()) {
			//Loop through screen objects
			for(Object o: screen.getContent()) {
				if (o instanceof JAXBElement<?>){
					//Generate layout for <input>, <output>, and <action> objects
					@SuppressWarnings("rawtypes")
					JAXBElement element = (JAXBElement) o;
					IOHashmapper iohashmapper = new IOHashmapper(element, logger);
					temp += iohashmapper.getLayout();
				}
			}
		}

		return temp;
	}

}
