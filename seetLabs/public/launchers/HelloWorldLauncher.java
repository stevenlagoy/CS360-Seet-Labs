import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;


public class HelloWorldLauncher 
{
	public static void main(String[] args)
	{
        
		LabLauncher launcher = new LabLauncher();
        launcher.getUserMethod("main", Void.TYPE, args.getClass());
        launcher.launchMethod(new Object[]{args});
        // other launchers should exit 0 if the user's code was successful, and 1 otherwise.
        
        int toReturn = launcher.getJSOutputStream().getOutput().equals("Hello, World!\n")?0:1;

        System.exit(toReturn);
	}
}