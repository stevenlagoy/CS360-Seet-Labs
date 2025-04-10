import java.io.*;
//package com.seet.labs;
public class JSOutputStream extends OutputStream
{
    private StringBuilder output;
    private boolean error;

    public JSOutputStream(boolean error){
        this.error = error;
        output = new StringBuilder();
    };

    public void write(int b)
    {
        output = output.append((char)b);
        jsWrite(b, error);
    }

    public String getOutput()
    {
        return output.toString();
    }

    public void resetOutput()
    {
        output = new StringBuilder();
    }

    private native void jsWrite(int b, boolean error);
}
