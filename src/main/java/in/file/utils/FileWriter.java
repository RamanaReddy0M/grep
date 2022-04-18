package in.file.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class FileWriter {
    public static boolean write(String output, String fileName) {
        if (output.isEmpty()) return false;
        File destination = new File(fileName);
        try {
            Files.write(destination.toPath(), output.getBytes());
        } catch (IOException e) {
            System.out.println(Color.ANSI_RED + exceptionParser(e.toString()));
            return false;
        }
        return true;
    }

    /*
     * example: java.nio.file.AccessDeniedException:
     * This function will remove java.nio.file and returns AccessDeniedException:...
     * */
    private static String exceptionParser(String ex) {
        return Arrays.stream(ex.split("\\.")).skip(3).reduce("", (a, b) -> a + " " + b);
    }
}
