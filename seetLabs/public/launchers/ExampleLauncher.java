import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.io.BufferedReader;
import java.io.FileReader;


public class ExampleLauncher 
{
	public static void main(String[] args)
	{
        
		LabLauncher launcher = new LabLauncher();

        int[] arr = {1, 2, 3, 4};
        launcher.getUserMethod("addArray", int.class, arr.getClass());
        Object check = launcher.launchMethod(new Object[]{arr});

        int toReturn = 0;
        if((int)check!=10)
        {
            toReturn = 1;
            System.err.println("We Expected 10, but you returned "+(int)check+".");
        }
        



        // read out the test cases file.
        String path = args[0];
        System.out.println(path);

        BufferedReader br = null;;
        try {
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();

            while (line != null) {
                System.out.println(line);
              line = br.readLine();
            }
            
        } 
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            //if(br != null) br.close();
        }



        // other launchers should exit 0 if the user's code was successful, and 1 otherwise.
        System.exit(toReturn);
	}
}