import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestCaseLauncher 
{
    public static void main(String[] args)
    {
        int toReturn =new CodeTester(new LabLauncher()).test(args[0])?0:1;
        if(toReturn==1)
        {
            System.err.println("Test Failed.");
        }
        System.exit(toReturn);    
    }

}
