import java.io.*;
public class JSOutputStream extends OutputStream
{

    private boolean error;

    public JSOutputStream(boolean error){
        this.error = error;
    };

    public void write(int b)
    {
        jsWrite(b, error);
    }

    private native void jsWrite(int b, boolean error);
}
