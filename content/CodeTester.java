package content;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import content.FileOperations.ScannerUtil;

//import seetLabs.public.java.LabLauncher; // broken because public is a reserved keyword

public class CodeTester {
    
    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        String testFileName = args[0];
        runAllTests(readTestCasesFile(FilePaths.TEST_CASE_DEST.resolve(testFileName)));

        // call launcher.getUserMethod("method name", return type, parameter types)
        // then launcher.launchMethod(parameter values)
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

    /* 
     * Tests look like this:
     *  
     * #tests co/io
     * testname : "input, input, ..." : "output"
     * testname : "method(input)" : "output"
     * testname : "" : "console output"
     */

    public static boolean runAllTests(List<String> tests) {
        for (String testLine : tests) {
            String[] parts = StringOperations.splitByUnquotedString(testLine, ":");
            String testName = parts[0], output = parts[2], input[], methodName = "";
            if (parts[1].contains("(")) { // calling a method
                methodName = parts[1].split("(")[0];
                input = parts[1].split("(")[1].replace(")","").split(",");
            }
            else input = parts[1].split(",");

            List<Object> parameters = Arrays.asList((Object[]) input); // Widening to object

            Class<?> returnType = output.getClass();
            
            boolean result = runTest(testName, methodName, returnType, parameters, parameters, output);

            if (!result) return false;
        }
        return true;
    }

    public static <T> boolean runTest(String testName, String methodName, Class<T> returnType, List<Object> args, List<Object> input, Object output) {
        LabLauncher launcher = new LabLauncher();

        if (!methodName.equals("")) {
            // using a user-defined method
            Class<?>[] parameterTypes = args.stream().map(Object::getClass).toArray(Class<?>[]::new);
            launcher.getUserMethod(methodName, returnType, parameterTypes);
            Object returned = launcher.launchMethod(input);

            return returned.equals(output);
        }
        else {
            // using console output
            JSOutputStream jsOutputStream = new JSOutputStream(false);
            System.setOut(new java.io.PrintStream(jsOutputStream));
            return jsOutputStream.getOutput().equals(output.toString());
        }
    }

}
