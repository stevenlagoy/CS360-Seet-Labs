import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

//package com.seet.labs;

public class LabLauncher
{
	private final static String className = "Lab";

	public LabLauncher()
	{
		new Thread(()->{new LabLauncher().callJS();}).start();
        System.setOut(new PrintStream(new JSOutputStream(false), true));
		System.setErr(new PrintStream(new JSOutputStream(true), true));
	}



	public Method getUserMethod(String methodName, Class returnType, Class<?>... parameterTypes)
	{
		Method toReturn;
		try
		{
			Class<?> lab = Class.forName(className);
			toReturn = lab.getDeclaredMethod(methodName, formalParameters);


		}
		catch(ClassNotFoundException e)
		{
			System.err.println("Couldn't find the '"+className+"' class. Maybe you accidentaly deleted or renamed it?");
			System.err.println("Reset the code editor if you're not sure how to fix this.");
			System.exit(1);
		}
		catch(NoSuchMethodException e)
		{
			System.err.println("Couldn't find the right '"+methodName+"' method. If this method exists in your code, make sure it has the right parameters.");
			System.err.println("Reset the code editor if you're not sure how to fix this.");
			System.exit(1);
		}

		if(!returnType.equals(toReturn.getReturnType()))
		{
			System.err.println("'"+methodName+"' returns '"+toReturn.getReturnType().getName()+"', but we expected '"+returnType.getName()+"'.");
			System.exit(1);
		}

		if(!Modifier.isStatic(toReturn.getModifiers()))
		{
			System.err.println("'"+methodName+"' should be static.");
			System.exit(1);
		}

		toReturn.setAccessible(true);
		return toReturn;
	}



	private native void callJS();
	public void end()
	{
		// exit prematurely
		System.exit(1);
	}
}


