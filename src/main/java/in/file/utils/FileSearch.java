package in.file.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileSearch {
    private int after;
    private int before;

    private boolean caseSensitive = true;

    /*
     * This param for checking whether o/p is for console or file.
     * If o/p is for writing into file, then we don't need to color
     * the matched string(in other words we don't want to add ANSI_COLOR_CODE to matched string),
     * Since files are not able to process ANSI_COLOR_CODES.
     * */
    private boolean outputForFile = false;

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

    public String search(String key, String fileName)  {
        File source = new File(fileName);
        if (!source.exists()) return Color.ANSI_RED + "No file found!";
        if (!source.isFile()) return Color.ANSI_YELLOW + "Can't search other than file!";
        if (!source.canRead())
            return Color.ANSI_RED + "Can't have permission to read file > " + Color.ANSI_GREEN + fileName;
        try{
            return search(key, Files.readAllLines(source.toPath()));
        }catch (IOException e){
            return Color.ANSI_RED + "Error Occurred! - " + e.getMessage();
        }
    }

    public String search(String key, List<String> lines) {
        if (lines == null || key.isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        //Following two params for checking whether the line already exits in the result or not.
        String previousLine = "", afterLine = "";
        for (int currentIndex = 0; currentIndex < lines.size(); currentIndex++) {
            String line = lines.get(currentIndex);
            if ((caseSensitive && line.contains(key)) || (!caseSensitive && line.toLowerCase().contains(key.toLowerCase()))) {
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
        //to remove appended last \n
        removeLastCharacter(result, "\n");
        return result.toString();
    }

    //there could be a multiple matches in single line.
    //ex: 'doesn’t allow the file to be read. It returns 0 if the file doesn’t exist or the last', let key = 'the'
    public String getAllMatchesOfLine(String line, String key) {
        StringBuilder result = new StringBuilder();
        for (String word : line.split(" "))
            if ((caseSensitive && word.contains(key)) || (!caseSensitive && word.toLowerCase().contains(key.toLowerCase())))
                result.append(colorMatchedString(word, key)).append(" ");
            else result.append(word).append(" ");
        removeLastCharacter(result," ");
        return result.toString();
    }

    //there could be a multiple matches in single word.
    //ex: <xmlVersionxml@#xml^^^XMl> , let key = 'xml'
    public String colorMatchedString(String word, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            /*
             * This if condition is for coloring the multiple matched string in given word. ex: filereader, key = 'er'
             * There are 3 'and' conditions in if statement.
             * 1.(!outputForFile) - if o/p is for file then no need to add color to string.
             * 2.(caseSensitive && word.charAt(i) == key.charAt(0) or
             *      !caseSensitive && (word.charAt(i) + "").equalsIgnoreCase(key.charAt(0) + "")
             *    - These two conditions or mutually exclusive.
             *    - if caseSensitive is true I need to make strict comparison over Current character
             *          and first character of key.
             *    - if it's not caseSensitive then ignore case of characters.
             * 3.(i + key.length() <= word.length()) - ensuring that I don't get index out of bounds, since inside the
             *        function there's need of substring() method.
             * */
            if ((!outputForFile)
                    && (caseSensitive && word.charAt(i) == key.charAt(0)
                    || !caseSensitive && (word.charAt(i) + "").equalsIgnoreCase(key.charAt(0) + ""))
                    && (i + key.length() <= word.length())) {
                String subString = word.substring(i, i + key.length());
                if ((caseSensitive && subString.equals(key)) || (!caseSensitive && subString.equalsIgnoreCase(key))) {
                    result.append(Color.ANSI_RED).append(subString).append(Color.ANSI_RESET);
                    i = i + key.length() - 1;
                } else result.append(word.charAt(i));
            } else result.append(word.charAt(i));
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

    public void setOutputForFile(boolean outputForFile) {
        this.outputForFile = outputForFile;
    }

    private void removeLastCharacter(StringBuilder result, String lastCharacter){
        int index = result.lastIndexOf(lastCharacter);
        if (index > 0) result.delete(index, result.length());
    }
}