package org.accapto.tool.templating;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.accapto.helper.Logger;

/**
 * 
 * Class looks for files that contain the layout, permissions and code of a specific method.
 * If the files are not found, a method stub is generated. 
 * @author Anja
 *
 */
public class MethodGenerator {

	private Logger logger;
	private File file;
	private String text;
	
	public MethodGenerator(Logger logger){
		this.logger = logger;
		this.text = "";
	}
	

	/**
	 * Checks if the file for a specific method exist. 
	 * If the file is found, the method reads the information
	 * from the file and puts it into the variable text. 
	 * If no appropriate file is found, a method stub is generated, if the type is "code".
	 * 
	 * @param method Name of the method
	 * @param type Type of file that is looked for; possible options: code, imports, layout, onCreate, permissions, variables (type is not case sensitive)s
	 * 
	 * @return Returns either code, imports, layout, onCreate, permission or variables for the given method; 
	 * 		returns a method stub, if file cannot be found and type is "code";
	 * 		returns an empty string, if file cannot be found and type is not "code"
	 * 
	 */
	public String lookForMethod(String method, String type){

		text = "";
		file = new File("methods" + File.separator + method + "_" + type.toLowerCase());
		
		if (file.exists()){
			logger.onlyFile("     " + type + " file for method " + method + "found");
			text = readFile(file);
		} else {
			logger.onlyFile("     " + type + " file for method " + method + " not found");
			//Generate method stub if no code is found
			if (type.toLowerCase().equals("code")){
				text = "\tpublic void " + method + "() {\n\t\t//TODO: auto-generated method stub\n\t}";
			}
		}
		
		return text;
	}

	
	
	/**
	 * Reads text from the input file and writes it into the output variable.
	 * @param input File from which the text is read
	 * @param output String to which the read text is written
	 */
	public String readFile(File input){
		String output = "";
		
		try {
			FileReader fileReader = new FileReader(input);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String currentLine = "";
			
			while((currentLine = bufferedReader.readLine()) != null){
				output += currentLine + "\n";
			}
			
			bufferedReader.close();
			fileReader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return output;
	}
	
}
