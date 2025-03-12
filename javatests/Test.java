import java.io.*;

public class Test
{

    public static void main(String[] args)
    {
        System.setOut(new PrintStream(new JSOutputStream()));
        System.out.println("Hello, world! \nThis is from java!");
    }
}

