package model.Tempo;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileSampleProcessor implements SampleProcessor {

    private Logger log = Logger.getLogger(FileSampleProcessor.class.getName());

    private BufferedWriter out;

    public FileSampleProcessor() throws FileNotFoundException, IOException {
        File output = new File("c:\\test.txt");
        out = new BufferedWriter(new FileWriter(output));
    }

    public void process(long[] sample) {
        try {
            out.append(""+sample[0]+"\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, "error writing to file: ", e);
        }
    }

    public void init(int freq, int channels) {
    }

    public void close() throws IOException {
        out.close();
    }

}
