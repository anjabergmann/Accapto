package org.accapto.helper;

import java.io.File;

import org.apache.commons.cli.*;

public class InputParser {

	private Logger logger;
	private FunctionDisplay functionDisplay;
	private String[] args;
	private File inputFile;
	
	private boolean verbose;
	private String inputArg;
	private String outputArg;
	
	public InputParser(String[] args){
		this.args = args;
		parseInput();
	}

	

	/**
	 * Parses input file and params 
	 * @return Returns true if input file of type .xml is found
	 * 
	 */
	public void parseInput(){
		
		Options options = new Options();
		createOptions(options);
		parse(options);
		
		checkInputFile();
		
	}
	
	
	/**
	 * Checks if input file exists. Ends programme if it doesn't. 
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
		
		Option functions = new Option("s", "showfunctions", false, "show pre-implemented functions");
		options.addOption(functions);
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
			} else if (cmd.hasOption("showfunctions")){
				functionDisplay = new FunctionDisplay();
				functionDisplay.listMethods();
				System.exit(1);
				return;
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("accapto", options);
			System.exit(1);
			return;
		}


		inputArg = cmd.getOptionValue("input");
		outputArg = cmd.getOptionValue("output");
		verbose = cmd.hasOption("verbose");
		logger = new Logger(verbose);
		

		
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

