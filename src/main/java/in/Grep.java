package in;

import in.file.utils.Color;
import in.file.utils.FileReader;
import in.file.utils.FileSearch;
import in.file.utils.FileWriter;

import org.apache.commons.cli.*;
import java.io.File;


public class Grep {
    static FileSearch fileSearch = new FileSearch();
    static Options options = new Options();
    static Option caseInSensitive = new Option("i", null, false, "-i -> Case-insensitive.");
    static Option afterLine = new Option("A", null, true, "-A <number> -> Displays nth line after matched string.");
    static Option beforeLine = new Option("B", null, true, "-B <number> -> Displays nth line before matched string.");
    static Option outputToFile = new Option("O", null, true, "-O <filename> -> Search results will be written to file.");
    static Option help = new Option("h", "help", false, "-h | --help");
    static {
        // Options class is required to create commandline object.
        options.addOption(caseInSensitive);
        options.addOption(afterLine);
        options.addOption(beforeLine);
        options.addOption(outputToFile);
        options.addOption(help);
    }

    public static void main(String[] args){

        if (args.length < 1) return;
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            CommandLine commandLine = commandLineParser.parse(options, args);
            //displaying usage && options
            if(commandLine.hasOption(help.getOpt())){
                printHelp();
                return;
            }
            int searchStringIndex = getSearchStringIndex(args);
            String key = args[searchStringIndex];
            //following three if statements construct fileSearch object by option -i, -A, -B
            if (commandLine.hasOption(caseInSensitive.getOpt())) fileSearch.setCaseSensitive(false);
            if (commandLine.hasOption(afterLine.getOpt()))
                fileSearch.setAfter(Integer.parseInt(commandLine.getOptionValue(afterLine.getOpt())));
            if (commandLine.hasOption(beforeLine.getOpt()))
                fileSearch.setBefore(Integer.parseInt(commandLine.getOptionValue(beforeLine.getOpt())));
            //If there's no input after search-string then (source) read from user(STDIN).
            if ((searchStringIndex + 1) == args.length) {
                display(fileSearch.search(key, FileReader.readFromSTDIN()));
                return;
            }
            //after search string there could be a file or directory.
            if ((searchStringIndex + 1) < args.length) {
                String fileOrDirectory = args[searchStringIndex + 1];
                //if it is File then there are 2 possibilities
                //1.No arguments after the filename.
                //2. -O option after the filename.
                if (isFile(fileOrDirectory)) {
                    //1.No arguments after the filename.
                    /*
                    * Regular way for writing result into file : key data.txt -O out.txt
                    * If you think; "-O out.tx 'key' data.txt" this could be a valid statement then
                    * use the condition '!commandLine.hasOption(outputToFile.getOpt())' as below.
                    * If you don't then remove '!commandLine.hasOption(outputToFile.getOpt())'
                    * */
                    if (((searchStringIndex + 2) == args.length) && !commandLine.hasOption(outputToFile.getOpt()))
                        display(fileSearch.search(key, fileOrDirectory));
                    else {
                        //2. -O option after the filename.
                        if (commandLine.hasOption(outputToFile.getOpt())) {
                            fileSearch.setOutputForFile(true);
                            String outputFileName = commandLine.getOptionValue(outputToFile.getOpt());
                            FileWriter.write(fileSearch.search(key, fileOrDirectory), outputFileName);
                        }
                    }
                } else if (isDirectory(fileOrDirectory)) {
                    if ((searchStringIndex + 2) == args.length) FileReader.recursiveFileSearch(key, fileOrDirectory);
                }
            }
        } catch (Exception e) {
            System.out.println(Color.ANSI_RED + e);
        }

    }

    //ex: -i -B 1 xml data.txt
    private static int getSearchStringIndex(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (options.hasOption(args[i])) {
                if (options.getOption(args[i]).hasArg()) i++;
                continue;
            }
            if (isSearchString(args[i])) return i;
        }
        return -1;
    }

    private static boolean isSearchString(String str) {
        return !isFlag(str) && !isFile(str);
    }

    private static boolean isFile(String str) {
        return new File(str).isFile();
    }

    private static boolean isDirectory(String str) {
        return new File(str).isDirectory();
    }

    private static boolean isFlag(String str) {
        return str.startsWith("-");
    }

    private static void printHelp(){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("-i | -A <num> | -B <num> | <search-key> | <search-key> <filename>" +
                " | <search-key> <Input-Filename> -O <Output-Filename>" ,options);
    }

    private static void display(String output){
        System.out.println(output);
    }
}
