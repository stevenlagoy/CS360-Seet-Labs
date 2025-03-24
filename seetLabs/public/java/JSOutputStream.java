import java.io.*;
public class JSOutputStream extends OutputStream
{

    public JSOutputStream(){};

    public void write(int b)
    {
        jsWrite(b);
    }

    private native void jsWrite(int b);
}
