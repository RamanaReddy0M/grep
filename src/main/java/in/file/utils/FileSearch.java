package in.file.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileSearch {
    private int after;
    private int before;

    private boolean caseSensitive = true;

    public FileSearch() {
        this.after = this.before = 0;
    }

    public FileSearch(boolean caseSensitive) {
        this();
        this.caseSensitive = caseSensitive;
    }

    public FileSearch(int after, int before, boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        this.after = after;
        this.before = before;
    }

    public static void main(String[] args) throws IOException {
        FileSearch fileSearch = new FileSearch(0, 0, false);
        System.out.println(fileSearch.search("the", "src"));
    }

    public String search(String key, String fileName) throws IOException {
        File source = new File(fileName);
        if (!source.exists()) return Color.ANSI_RED + "No file found!";
        if (!source.isFile()) return Color.ANSI_YELLOW + "Can't search other than file.";
        List<String> lines = Files.readAllLines(source.toPath());
        StringBuilder result = new StringBuilder();
        //Following two params for checking whether the line already exits in the result or not.
        String previousLine = "", afterLine = "";
        for (int currentIndex = 0; currentIndex < lines.size(); currentIndex++) {
            String line = lines.get(currentIndex);
            if (line.toLowerCase().contains(key.toLowerCase())) {
                if (before > 0 && currentIndex - before > -1 && !previousLine.equals(lines.get(currentIndex - before)))
                    result.append(lines.get(currentIndex - before)).append("\n");
                previousLine = lines.get(currentIndex);
                if (!afterLine.equals(previousLine))
                    result.append(getAllMatchesOfLine(line, key)).append("\n");
                if (after > 0 && currentIndex + after < lines.size()) {
                    afterLine = lines.get(currentIndex + after);
                    if (afterLine.contains(key)) result.append(getAllMatchesOfLine(afterLine, key)).append("\n");
                    else result.append(afterLine).append("\n");
                }
            }
        }
        return result.toString();
    }

    public String getAllMatchesOfLine(String line, String key) {
        StringBuilder result = new StringBuilder();
        for (String word : line.split(" "))
            if (word.toLowerCase().contains(key.toLowerCase()))
                result.append(colorMatchedString(word, key)).append(" ");
            else result.append(word).append(" ");
        return result.toString();
    }

    public String colorMatchedString(String word, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if ((caseSensitive && word.charAt(i) == key.charAt(0) ||
                    !caseSensitive && (word.charAt(i) + "").equalsIgnoreCase(key.charAt(0) + ""))
                    && i + key.length() < word.length()) {
                String subString = word.substring(i, i + key.length());
                if (subString.equals(key) || (!caseSensitive && subString.equalsIgnoreCase(key))) {
                    result.append(Color.ANSI_RED).append(subString).append(Color.ANSI_RESET);
                    i = i + key.length() - 1;
                }
            }else result.append(word.charAt(i));
        }
        return result.toString();
    }

    public void setAfter(int after) {
        this.after = after;
    }

    public void setBefore(int before) {
        this.before = before;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}