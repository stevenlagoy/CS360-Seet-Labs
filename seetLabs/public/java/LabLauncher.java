import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

public class LabLauncher
{
	public static void main(String[] args)
	{
		new Thread(()->{new LabLauncher().callJS();}).start();
		
        System.setOut(new PrintStream(new JSOutputStream(false), true));
		System.setErr(new PrintStream(new JSOutputStream(true), true));
		Lab.main(args);
		System.exit(0);
	}

	private native void callJS();



	public void end()
	{
		// exit prematurely
		System.exit(1);
	}
}


