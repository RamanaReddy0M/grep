import in.file.utils.Color;
import in.file.utils.FileSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Grep {
    static FileSearch fileSearch = null;
    static String filename = null;

    public static void main(String[] args) throws IOException {
        if(args.length < 1) return;
        String firstArg = args[0];
        //for the first arg there 3 possibilities; can be - flag, filename or search string
        if(isFlag(firstArg)){
            System.out.println("To be processed...");
        }else if(isSearchString(firstArg)){
            if(args.length > 1)
                filename = args[1];
            else return;
            fileSearch = new FileSearch();
            System.out.println(fileSearch.search(firstArg, filename));
        }else if(isFile(firstArg)){
            readFile(filename);
        }
    }

    private static boolean isSearchString(String str){
        return !isFile(str) && !isDirectory(str) && !isFlag(str);
    }
    private static boolean isFile(String str){
        return new File(str).isFile();
    }

    private static boolean isDirectory(String str){
        return new File(str).isDirectory();
    }

    private static boolean isFlag(String str){
        return str.startsWith("-");
    }

    private static void readFile(String filename) throws IOException {
        Path source = Paths.get(filename);
        if(Files.exists(source)){
            try(Stream<String> lines = Files.lines(source)){
                lines.forEach(System.out::println);
            }
        }else System.out.println(Color.ANSI_RED + "No file found!");
    }
}
