package org.accapto.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class FunctionDisplay {


	
	// return a list of all functions with 'accapto --showfunctions'
	public void listMethods() {
		
		String text = "";
		File file = new File("Methods" + File.separator + "methods");
		
		text = readFile(file);
		System.out.println(text);
		
	}
	
	
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
