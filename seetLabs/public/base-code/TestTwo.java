public class Lab
{
	public static void main(String[] args)
	{
        try
        {
            for(int i = 0; i<10; i++)
            {
                System.out.println("Hello, world!");
                Thread.sleep(1000);
            }
            throw new Exception("That's the end!");   
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
     
    }
}