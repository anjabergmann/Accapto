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

	private boolean verbose;	// If option verbose is set at accapto call, information is printed to sys.out; otherwise only to log file
	private File logfile;		// File in which all the log messages are written
	private PrintWriter writer;	// Writer to write log messages into file
	private ArrayList<String> messages;	// Save the messages that are logged before the log file is created
	private boolean logging;	// If logging is set to false, it means that the log file is not yet created and the log messages are only saved in the messages ArrayList; as soon as the log file is created, logging is set to true and the log messages are instead written to the log file
	private SimpleDateFormat df;	//Date format for time stamps
	
	public Logger(boolean verbose){
		this.verbose = verbose;
		this.messages = new ArrayList<String>();
		this.logging = false;
		this.df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ");
	}
	
	/*
	 * Creates a log file and PrinterWriter to write messages to log file.
	 */
	public void initLog(String path){
		logfile = new File(path + File.separator + "accapto.log");
		logging = true;
		// Loop through saved messages and write them into log file
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
	 * Writes log message into file and prints it to standard out if verbose is set true.
	 * @param message Message to be logged. 
	 */
	public void log(String message){
		
		// Write log message to sys.out if verbose is set true
		if (verbose){
			System.out.println(message);
		}
		
		//Add time stamp to the message
		message = df.format(new Timestamp(System.currentTimeMillis())) + message;
		
		//If log file is already created, write message to log file ...
		if(logging){
			try{	
				PrintWriter writer = new PrintWriter(new FileWriter(logfile, true)); 
				writer.println(message);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		// ... otherwise save message in messages ArrayList
		} else {
			messages.add(message);
		}
	}
	
	
	/**
	 * Writes log message into file and prints it to standard error, independent of verbose argument.
	 * @param message Message to be logged. 
	 */
	public void logErr(String message){
		// Write error to sys.err; always print errors, also if verbose is set false 
		System.err.println(message);
		
		//Add time stamp to message
		message = df.format(new Timestamp(System.currentTimeMillis())) + message;

		//If log file is already created, write message to log file ...
		if(logging){
			try{	
				writer = new PrintWriter(new FileWriter(logfile, true)); 
				writer.println(message);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		//... otherwise save message in messages ArrayList
		} else {
			messages.add(message);
		}
	}
	
	/**
	 * Writes log message only into log file and doesn't print it out.
	 * @param message Message to be logged. 
	 */
	public void onlyFile(String message){
		// Add ime stamp to message
		message = df.format(new Timestamp(System.currentTimeMillis())) + message;
		//If log file is already created, write message to log file ...
		if(logging){
			try{	
				writer = new PrintWriter(new FileWriter(logfile, true)); 
				writer.println(message);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		//... otherwise save message in messages ArrayList
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
