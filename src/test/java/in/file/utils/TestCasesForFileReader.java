package in.file.utils;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mockStatic;

public class TestCasesForFileReader {

    @Test
    public void testForRecursiveFileSearch() {
        MockedStatic<FileReader> fileReaderMock = mockStatic(in.file.utils.FileReader.class);
        //FileReader.recursiveFileSearch(..) doesn't return anything(void).
        fileReaderMock.when(() -> FileReader.recursiveFileSearch("pub", "src")).then((Answer<Void>) invocationOnMock -> null);
    }

    @Test
    public void testForReadFromFile() {
        Path tempSource = Paths.get("tempDataZ.txt");
        //Content of file where key will be searched.
        String fileContent = "<xmlVersionxml@#xml^^^XMl> </xmlVersion&Xml>\nolder VMs the conversion";
        if (!FileWriter.write(fileContent, tempSource.toString())) fail();
        List<String> lines = FileReader.readFromFile(tempSource.toString());
        assertFalse(lines.isEmpty());
        //Here, it prints(not throws) exception if it triggers any IOException.
        lines = FileReader.readFromFile("/data.txt");
        assertTrue(lines.isEmpty());

        //deleting created temporary source
        try {
            Files.deleteIfExists(tempSource);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

}
