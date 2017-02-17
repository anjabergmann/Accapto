package org.accapto.helper;

import java.io.File;

import org.apache.commons.cli.*;

public class InputParser {

	private String[] args;		// Input arguments from command line
	private Logger logger;		// Logger object to write log messages to sys.out and to log file
	private boolean verbose;	// Option verbose
	private String inputArg;	// Path to respectively name of input file
	private File inputFile;		// Input file 
	private String outputArg;	// Path where the app scaffold will be created
	
	public InputParser(String[] args){
		this.args = args;
		parseInput();
	}

	

	/**
	 * Parses input file and params. Then uses checkInputFile() to check if input file exists.
	 */
	public void parseInput(){
		Options options = new Options();
		createOptions(options);
		parse(options);
		checkInputFile();
	}
	
	
	/**
	 * Checks if input file exists. Ends programme, if it doesn't. 
	 */
	private void checkInputFile() {
		inputFile = new File(inputArg);
		
		if (inputFile.exists()){
			logger.log("INFO Input file " + inputFile.getPath() + " was found.");
		} else {
			logger.logErr("ERROR File " + inputFile.getPath() + " could not be found. Please check if the file exists and that you entered the right path and file name.");
			System.exit(1);
			return;
		}
	}


	/**
	 * Creates arguments that can be interpreted by accapto
	 * @param options
	 */
	public void createOptions(Options options){
		
		Option help = new Option("h", "help", false, "shows usage of accapto and possible options");
		options.addOption(help);
		
		Option help2 = new Option("?", "help", false, "shows usage of accapto and possible options");
		options.addOption(help2);
		
		Option verbose = new Option("v", "verbose", false, "print additional status information");
		options.addOption(verbose);
		
		Option input = new Option("i", "input", true, "input file path (must be of type .xml)");
		options.addOption(input);

		Option output = new Option("o", "output", true, "path where the app will be created (default: accapto directory)");
		options.addOption(output);
	}
	
	
	/**
	 * Puts passed arguments into designated class variables
	 * @param options
	 */
	public void parse(Options options){
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("help") || (!cmd.hasOption("input") && !cmd.hasOption("function") && !cmd.hasOption("showfunctions"))){
				formatter.printHelp("accapto", options);
				System.exit(1);
				return;
			} 
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("accapto", options);
			System.exit(1);
			return;
		}


		// Get options from command line
		inputArg = cmd.getOptionValue("input");
		outputArg = cmd.getOptionValue("output");
		verbose = cmd.hasOption("verbose");
		
		// Create a logger object to write log messages to log file and to sys.out
		logger = new Logger(verbose);

		//Set a default output path if no specific output path is given
		if(outputArg == null){
			outputArg = "..";
		}
		
		logger.onlyFile("INFO Accapto was called with following options: ");
		logger.onlyFile("     Verbose is set " + Boolean.toString(verbose));
		logger.onlyFile("     Input file: " + inputArg);
		logger.onlyFile("     Output path: " + outputArg);
		logger.onlyFile("--------------------------------------------------");
	}
	
	
	
	
	
	
	// ------ Getters ------------------------------------------------------------------

	public File getInputFile(){
		return inputFile;
	}

	public Logger getLogger(){
		return logger;
	}
	
	public boolean isVerbose() {
		return verbose;
	}

	public String getInputArg() {
		return inputArg;
	}

	public String getOutputArg() {
		return outputArg;
	}

}

