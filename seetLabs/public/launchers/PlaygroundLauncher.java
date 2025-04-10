import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
//package com.seet.labs;

public class PlaygroundLauncher 
{
	public static void main(String[] args)
	{
		LabLauncher launcher = new LabLauncher();
        Method main = launcher.getUserMethod("main", String[].class);
        String[] args = {};
        main.invoke(null, args);
        System.exit(0);
	}
}