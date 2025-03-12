import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import com.sun.tools.javac.Main;

public class JavaCLauncher
{
	public static void main(String[] args) throws Exception
	{
        System.setOut(new PrintStream(new JSOutputStream()));
		System.setErr(new PrintStream(new JSOutputStream()));
		com.sun.tools.javac.Main.main(args);
	}
}


