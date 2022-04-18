package in.file.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TestCaseForFileWriter {

    @Test
    public void testForWriteMethod() {
        boolean isWritten = FileWriter.write("The content in file,\n I'm going to be deleted.", "out.txt");
        assertTrue(isWritten);
        //attempt to create file at root level, probably throws AccessDeniedException
        isWritten = FileWriter.write("The content in file,\n I'm going to be deleted.", "/out.txt");
        assertFalse(isWritten);
        try {
            Files.deleteIfExists(Paths.get("out.txt"));
        } catch (IOException e) {
            fail();
        }
    }
}
