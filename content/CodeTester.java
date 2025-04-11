package content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeTester {
    
    public static final Path BASE_PATH = Path.of("seetLabs");
    public static final Path PUBLIC_PATH = BASE_PATH.resolve("public");
    public static final Path unitTestSource = PUBLIC_PATH.resolve("test-cases");

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        String testFileName = "3-2_test_cases";
        runAllTests(readTestCasesFile(unitTestSource.resolve(testFileName)));
    }

    public static class ScannerUtil {
        public static Scanner createScanner(InputStream inputStream) {
            return new Scanner(inputStream, StandardCharsets.UTF_8.name());
        }
        public static Scanner createScanner(File file) throws FileNotFoundException {
            return new Scanner(file, StandardCharsets.UTF_8.name());
        }
    }

    public static ArrayList<String> readTestCasesFile(Path filepath) {
        ArrayList<String> contents = new ArrayList<String>();
        try {
            File file = filepath.toFile();
            Scanner scanner = ScannerUtil.createScanner(file);
            while (scanner.hasNextLine()) {
                contents.add(scanner.nextLine());
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return contents;
    }

    public static boolean runAllTests(List<String> tests) {
        for (String testLine : tests) {
            String[] parts = StringOperations.splitByUnquotedString(testLine, ":");
            String testName = parts[0], input = parts[1], output = parts[2];
            String programOutput;

            // use the input to run the user's defined functions
            programOutput = "";

            if (!programOutput.equals(output)) return false;
        }
        return true;
    }

}
