package in.file.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class FileReader {
    private static FileSearch fileSearch = new FileSearch();

    public static List<String> readFromFile(String fileName) {
        File source = new File(fileName);
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(source.toPath());
        } catch (IOException e) {
            System.out.println(Color.ANSI_RED + exceptionParser(e.toString()));
        }
        return lines;
    }

    public static List<String> readFromSTDIN() {
        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) input.add(scanner.nextLine());
        } catch (NoSuchElementException e) {
            return input;
        }
    }

    public static void recursiveFileSearch(String key, String directory) {
        File source = new File(directory);
        try (Stream<Path> pathStream = Files.walk(source.toPath())) {
            pathStream.filter(path -> path.toFile().isFile())
                    .map(path -> {
                        return Color.ANSI_CYAN + path + ":" + Color.ANSI_RESET + fileSearch.search(key, path.toString());
                    }).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(Color.ANSI_RED + exceptionParser(e.toString()));
        }

    }

    public static void setFileSearch(FileSearch fileSearch) {
        FileReader.fileSearch = fileSearch;
    }

    /*
     * example: java.nio.file.NoSuchFileException:
     * This function will remove java.nio.file and returns NoSuchFileException:...
     * */
    private static String exceptionParser(String ex) {
        return Arrays.stream(ex.split("\\.")).skip(3).reduce("", (a, b) -> a + " " + b);
    }
}
