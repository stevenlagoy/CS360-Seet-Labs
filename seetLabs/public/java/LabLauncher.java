import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.*;

//package com.seet.labs;

public class LabLauncher
{
	private final static String className = "Lab";
	private Method userMethod = null;

	public LabLauncher()
	{
		new Thread(()->{this.callJS();}).start();
        System.setOut(new PrintStream(new JSOutputStream(false), true));
		System.setErr(new PrintStream(new JSOutputStream(true), true));
	}



	public void getUserMethod(String methodName, Class<?> returnType, Class<?>... parameterTypes)
	{
		
		try
		{
			Class<?> lab = Class.forName(className);
			userMethod = lab.getDeclaredMethod(methodName, parameterTypes);


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

		if(!returnType.equals(userMethod.getReturnType()))
		{
			System.err.println("'"+methodName+"' returns '"+userMethod.getReturnType().getName()+"', but we expected '"+userMethod.getName()+"'.");
			System.exit(1);
		}

		if(!Modifier.isStatic(userMethod.getModifiers()))
		{
			System.err.println("'"+methodName+"' should be static.");
			System.exit(1);
		}

		
	}


	public Object launchMethod(Object... arguments)
	{
		
		try
        {
            userMethod.invoke(null, arguments);
        }
        catch(InvocationTargetException e)
        {
			e.getCause().printStackTrace();
			System.exit(1);
        }
        catch(IllegalAccessException e)
        {
            System.err.println("Couldn't access '"+userMethod.getName()+"'.");
			System.err.println("Your method should be declared as public, i.e. public static "+userMethod.getName()+"( ... arguments ... )");
			System.exit(1);
        }

		return null;
	
	}



	public native void callJS();
	public void end()
	{
		// exit prematurely
		System.exit(1);
	}
}


