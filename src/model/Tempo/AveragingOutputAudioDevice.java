package model.Tempo;

import java.util.LinkedList;
import java.util.Queue;
import javazoom.jl.decoder.JavaLayerException;


public class AveragingOutputAudioDevice extends BaseOutputAudioDevice {
    private int averageLength = 1024; // number of samples over which the average is calculated
    private Queue<Short> instantBuffer = new LinkedList<Short>();

    public AveragingOutputAudioDevice(SampleProcessor processor) {
        super(processor);
    }

    @Override
    protected void outputImpl(short[] samples, int offs, int len) throws JavaLayerException {
        for(int i=0; i<len; i++)
            instantBuffer.offer(samples[i]);

        while(instantBuffer.size()>averageLength*channels)
        {
            long[] average = new long[channels];
            for(int i=0; i<averageLength; i++)
                for(int c=0; c<channels; c++)
                    average[c] += instantBuffer.poll();

            for(int c=0; c<channels; c++)
                average[c] = average[c]/len;

            if(processor != null)
                processor.process(average);
        }
    }
}
