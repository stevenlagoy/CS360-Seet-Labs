import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

public class LabLauncher
{
	public static void main(String[] args)
	{
        System.setOut(new PrintStream(new JSOutputStream()));
		System.setErr(new PrintStream(new JSOutputStream()));
		Lab.main(args);
	}
}


