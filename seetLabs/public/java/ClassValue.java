
public class ClassValue
{

    private Class<?> classclass;
    private Object instance;

    /**
     * 
     * @param input must be of the form "ClassName "Value""
     */
    public ClassValue(String input)
    {
        String[] values = StringOperations.splitByUnquotedString(input, " ");
       

        try
        {
            classclass = Class.forName(values[0]);   
            instance = setValue(values[1].substring(1, values[1].length()-1));
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

    }


    private Object setValue(String input)
    {
        if(classclass.equals(Integer.class))
        {
            return Integer.parseInt(input);
        }
        if(classclass.equals(Double.class))
        {
            return Double.parseDouble(input);
        }
        if(classclass.equals(String.class))
        {
            return input;
        }
        System.err.println("SeetLabs:Code: Could not parse type "+classclass.getName());
        return null;
    }

    public Class<?> getClassClass()
    {

        if((classclass.equals(Integer.class)))
        {
            return int.class;
        }
        if(classclass.equals(Double.class))
        {
            return double.class;
        }

        return classclass;
    }

    public Object getValue()
    {
        return instance;
    }

}
