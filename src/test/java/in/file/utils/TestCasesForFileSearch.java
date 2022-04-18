package in.file.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestCasesForFileSearch {

    @Test
    public void testToSearchFileWithKey() throws InterruptedException {
        //Creating temporary file
        FileSearch fileSearch = new FileSearch();
        fileSearch.setOutputForFile(true);
        Path tempSource = Paths.get("tempDataZ.txt");
        //Content of file where key will be searched.
        String fileContent = "<xmlVersionxml@#xml^^^XMl> </xmlVersion&Xml>\nolder VMs the conversion";
        if (!FileWriter.write(fileContent, tempSource.toString())) fail();
        //test 1 - case-sensitive
        String key = "xMl";
        String expectedResult = "";
        String actualResult = fileSearch.search(key, tempSource.toAbsolutePath().toString());
        assertEquals(expectedResult, actualResult);
        //test 2 - case-Insensitive
        fileSearch.setCaseSensitive(false);
        key = "xMl";
        expectedResult = "<xmlVersionxml@#xml^^^XMl> </xmlVersion&Xml>";
        actualResult = fileSearch.search(key, tempSource.toAbsolutePath().toString());
        assertEquals(expectedResult, actualResult);
        //test 3 - case-sensitive && after 1 line
        fileSearch.setCaseSensitive(true);
        fileSearch.setAfter(1);
        key = "Version";
        expectedResult = "<xmlVersionxml@#xml^^^XMl> </xmlVersion&Xml>\nolder VMs the conversion";
        actualResult = fileSearch.search(key, tempSource.toAbsolutePath().toString());
        assertEquals(expectedResult, actualResult);

        //deleting created temporary source
        try {
            Files.deleteIfExists(tempSource);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }
}
