package model.Objects;

import java.io.*;

public class StreamConsumer extends Thread
{
    InputStream is;
    String type;
    
    public StreamConsumer(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                System.out.println(">" + line);    
            } catch (IOException ioe)
            {
                ioe.printStackTrace();  
            }
    }
}
