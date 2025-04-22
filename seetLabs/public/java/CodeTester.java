import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;



public class CodeTester 
{
    
    public boolean test(String fileName) 
    {
        
        return runAllTests(readTestCasesFile(Paths.get("/app/test-cases/"+fileName)));

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
            String[] parts = StringOperations.splitByUnquotedString(testLine, ":");


            String testName = parts[0];
            String methodName = parts[1];
            String[] rawInputs = StringOperations.splitByUnquotedString(parts[2], ",");
            String rawReturn = parts[3];


            ClassValue returnVal = new ClassValue(rawReturn);
            Class<?> returnType = returnVal.getClassClass();
            Object returnExpect = returnVal.getValue();

            ArrayList<Class<?>> paramTypes = new ArrayList<Class<?>>();
            ArrayList<Object> params = new ArrayList<Object>();
            for(String input : rawInputs)
            {
                ClassValue cv = new ClassValue(input);
                params.add(cv.getValue());
                paramTypes.add(cv.getClassClass());
            }
        
            
            boolean result = runTest(testName, methodName, returnType, paramTypes, params, returnExpect);

            if (!result) return false;
        }
        return true;
    }


  
  

    public static boolean runTest(String testName, String methodName, Class<?> returnType, List<Class<?>> args, List<Object> input, Object output) 
    {
        LabLauncher launcher = new LabLauncher();


        // using a user-defined method
        launcher.getUserMethod(methodName, returnType, args.toArray(new Class[0]));
        Object returned = launcher.launchMethod(input);

        Boolean toReturn = returned.equals(output);
        if(toReturn)
        {
            System.out.println(testName+" Passed.");
        }
        else
        {
            System.err.println(testName+" Failed. Expected "+output+", Got "+returned);
        }

      
        return toReturn;
    }

}
