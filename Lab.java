public class Lab {
	public static void main(String[] args) {
		// Write your code below
		for(int i = 0; i<=100; i++)
    {
      System.out.println(fizzBuzz(i));
    }
	}

    private static String fizzBuzz(int num)
    {
      String out = "";
      if(num%5==0) out+="Fizz";
      if(num%3==0) out+="Buzz";

      if(out.length() == 0)
      {
        out +=num;
      }
      return out;
    }
}
