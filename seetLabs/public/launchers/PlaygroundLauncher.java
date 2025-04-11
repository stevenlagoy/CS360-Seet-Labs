import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;


public class PlaygroundLauncher 
{
	public static void main(String[] args)
	{
       
		LabLauncher launcher = new LabLauncher();
        
        launcher.getUserMethod("main", Void.TYPE, args.getClass());
        launcher.launchMethod(new Object[]{args});
        // other launchers should exit 0 if the user's code was successful, and 1 otherwise.
        System.exit(0);
	}
}