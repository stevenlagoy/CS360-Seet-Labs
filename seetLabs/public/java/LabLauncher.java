import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

public class LabLauncher
{
	public static void main(String[] args)
	{
        System.setOut(new PrintStream(new JSOutputStream(false)));
		System.setErr(new PrintStream(new JSOutputStream(true)));
		Lab.main(args);
	}
}


