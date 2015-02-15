package model.Tempo;

public interface SampleProcessor 
{

    void process(long[] sample);

    public void init(int freq, int channels);
    
}
