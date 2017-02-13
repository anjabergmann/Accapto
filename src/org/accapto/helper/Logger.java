package org.accapto.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Simple class to write messages to a log file.
 * If verbose is set to true, the messages are also printed out on the console. 
 * @author Anja
 *
 */
public class Logger {

	private boolean verbose;
	private File logfile;
	private PrintWriter writer;
	private ArrayList<String> messages;
	private boolean logging;
	private SimpleDateFormat df;
	
	public Logger(boolean verbose){
		this.verbose = verbose;
		this.messages = new ArrayList<String>();
		this.logging = false;
		this.df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ");
	}
	
	
	public void initLog(String path){
		logfile = new File(path + File.separator + "accapto.log");
		logging = true;
		for (String s : messages){
			try{
				PrintWriter writer = new PrintWriter(new FileWriter(logfile, true)); 
				writer.println(s);
				writer.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
		
	
	/**
	 * Writes log message into file and prints it to standard out if verbose is true.
	 * @param message Message to be logged. 
	 */
	public void log(String message){
		
		if (verbose){
			System.out.println(message);
		}
		
		message = df.format(new Timestamp(System.currentTimeMillis())) + message;
		
		if(logging){
			try{	
				PrintWriter writer = new PrintWriter(new FileWriter(logfile, true)); 
				writer.println(message);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			messages.add(message);
		}
	}
	
	
	/**
	 * Writes log message into file and prints it to standard error, independent of verbose argument.
	 * @param message Message to be logged. 
	 */
	public void logErr(String message){

		System.err.println(message);
		
		message = df.format(new Timestamp(System.currentTimeMillis())) + message;

		if(logging){
			try{	
				writer = new PrintWriter(new FileWriter(logfile, true)); 
				writer.println(message);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			messages.add(message);
		}
	}
	
	/**
	 * Writes log message only into log file and doesn't print it out.
	 * @param message Message to be logged. 
	 */
	public void onlyFile(String message){
		message = df.format(new Timestamp(System.currentTimeMillis())) + message;
		if(logging){
			try{	
				writer = new PrintWriter(new FileWriter(logfile, true)); 
				writer.println(message);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			messages.add(message);
		}
	}


	
	// -------- Getters and setters --------------------------------------------------------------------
	
	public void setVerbose(boolean v){
		verbose = v;
	}

	public boolean getVerbose(){
		return verbose;
	}
	
	public void setLogfile(File f){
		logfile = f;
	}
	
	public File getLogFile(){
		return logfile;
	}
	

}
