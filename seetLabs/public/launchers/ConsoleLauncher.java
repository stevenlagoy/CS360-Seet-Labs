import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ConsoleLauncher
{
    public static void main(String[] args)
    {
        LabLauncher launcher = new LabLauncher();
        launcher.getUserMethod("main", Void.TYPE, args.getClass());
        String[] fakeArg = new String[0];

        launcher.launchMethod(new Object[]{fakeArg});

        // other launchers should exit 0 if the user's code was successful, and 1 otherwise.
        
      

        String testAgainst ="";
        try(Scanner sc = new Scanner(new File(args[0])))
        {
            while(sc.hasNext()) 
            {
                testAgainst+=sc.nextLine()+")";
            }     
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }


        int toReturn = launcher.getJSOutputStream().getOutput().trim().equals(testAgainst.trim())?0:1;
        if(toReturn==1)
        {
            System.err.println("Test Failed. Expected Output: \n"+testAgainst+"");
        }
        System.exit(toReturn);    
    }
}
