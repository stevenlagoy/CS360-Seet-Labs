

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;



public class CodeTester 
{
    
    public static void main(String[] args) 
    {
    
        for (String arg : args) {
            System.out.println(arg);
        }
        String testFileName = args[0];
        runAllTests(readTestCasesFile(FilePaths.TEST_CASE_DEST.resolve(testFileName)));

        // call launcher.getUserMethod("method name", return type, parameter types)
        // then launcher.launchMethod(parameter values)
    }

    public ArrayList<String> readTestCasesFile(Path filepath) 
    {

        ArrayList<String> contents = new ArrayList<String>();

        try 
        {
            File file = filepath.toFile();
            Scanner scanner = new Scanner(file);


            while (scanner.hasNextLine())
            {
                contents.add(scanner.nextLine());
            }

            scanner.close();
        }
        catch (FileNotFoundException e) 
        {
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

    public boolean runAllTests(List<String> tests)
     {
        for (String testLine : tests) 
        {
            String[] parts = splitByUnquotedString(testLine, ":");
            String testName = parts[0], output = parts[2], input[], methodName = "";

            if (parts[1].contains("("))
            { 
                // calling a method
                methodName = parts[1].split("(")[0];
                input = parts[1].split("(")[1].replace(")","").split(",");
            }
            else
            {
                input = parts[1].split(",");
            }

            List<Object> parameters = Arrays.asList((Object[]) input); // Widening to object

            Class<?> returnType = output.getClass();
            
            boolean result = runTest(testName, methodName, returnType, parameters, parameters, output);

            if (!result) return false;
        }
        return true;
    }



    private String[] splitByUnquotedString(String string, String separator) {
        List<String> parts = new ArrayList<>();
        int lastSplitIndex = 0;
        
        for (int i = 0; i <= string.length() - separator.length(); i++) {
            // Check if this position contains the separator
            if (string.startsWith(separator, i)) {
                // Verify the separator is not in quotes
                if (!isInString(string, i)) {
                    // Add the part before this separator
                    parts.add(string.substring(lastSplitIndex, i).trim());
                    lastSplitIndex = i + separator.length();
                    i += separator.length() - 1; // Skip the rest of the separator
                }
            }
        }
        
        // Add the remaining part after the last separator
        if (lastSplitIndex <= string.length()) {
            parts.add(string.substring(lastSplitIndex).trim());
        }
        
        return parts.toArray(new String[0]);
    }

    // returns whether a specific character in a string is in a string delimited by "".
    private boolean isInString(String line, int position) 
    {
        boolean inString = false;
        for (int i = 0; i < position; i++) 
        {
            char c = line.charAt(i);
            boolean charIsEscaped = i!=0 && line.charAt(i-1) == '\\';

            if (c == '"' && !charIsEscaped ) {
                inString = !inString;
            }
        }

        return inString;
    }


    public static boolean runTest(String testName, String methodName, Class<?> returnType, List<Class<?>> args, List<Object> input, Object output) {
        LabLauncher launcher = new LabLauncher();

        if (methodName.equals("")) 
        {
             // using console output
            String[] strArr = new String[0];
            launcher.getUserMethod("main", Void.TYPE, strArr.getClass());
            launcher.launchMethod(input);
            return launcher.getJSOutputStream().getOutput().equals(output.toString());
        }

        // using a user-defined method
        launcher.getUserMethod(methodName, returnType, args);
        Object returned = launcher.launchMethod(input);

        return returned.equals(output);

      
        
    }

}
