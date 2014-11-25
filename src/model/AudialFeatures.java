package model;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import model.Objects.StreamConsumer;
import model.Objects.WavFile;
import model.Objects.WavFileException;

public class AudialFeatures
{
	private String segmentPath;
	private String directoryMain;
	private String praatPath;
	
	public void setFile(String resultPath)
	{
		this.directoryMain = resultPath;
	}
	
	public AudialFeatures(String audialSegment, String praatResult)
	{
		this.segmentPath = audialSegment;
		this.praatPath = praatResult;
	}
	
	/**
	 * Create Praat Scripts for querying Audial energy and Power for all generate .wav files
	 */
	public void producePraatScripts()
	{
		StringBuilder script = new StringBuilder();
		File audialSeg = new File(segmentPath);
		int fileCount = audialSeg.listFiles().length;
		System.out.println("FILES FOLDER: "+audialSeg.toString());
		System.out.println("FILE COUNT: "+fileCount);
		System.out.println("SEGMENT PATH: "+segmentPath);
		System.out.println("PRAAT PATH: "+praatPath);
		System.out.println("TEST ARGS: "+ segmentPath.concat("\\"+1+".wav"));
		
		File praatFile = new File(praatPath.concat("\\Features.praat"));
		File energyFile = new File (praatPath.concat("\\AudialEnergy.txt"));
		File powerFile = new File (praatPath.concat("\\AudialPower.txt"));
		File paceFile = new File(praatPath.concat("\\AudialPace.txt"));
		for(int i = 1; i <= fileCount; i++)
		{			
//			script = script.append("Read from file: "+"\""+segmentPath.concat("\\"+i+".wav")+"\"" + "\r\n");			
//			script = script.append("energy$ = Get energy: 0, 0" + "\r\n");
//			script = script.append("power$ = Get power: 0, 0" + "\r\n");			
//			script = script.append("writeInfoLine: "+ "\"" +"Audio Energy= "+"\""+", energy$" + "\r\n");
//			script = script.append("writeInfoLine: "+ "\""+"Audio Power= "+"\""+",power$" + "\r\n");			
//			script = script.append("appendFileLine: "+"\""+ energyFile.getAbsolutePath() +"\""+", " + "\"Shot " + i + " \"" + "+ energy$"+ "\r\n");			
//			script = script.append("appendFileLine: "+"\""+ powerFile.getAbsolutePath() +"\""+", " + "\"Shot " + i + " \"" + "+ power$"+ "\r\n");
			findPeak(i, paceFile, segmentPath);
		}
		
		FileWriter praatWriter = null;
    	try 
		{
			praatWriter = new FileWriter(praatFile.getAbsoluteFile());			
			
			BufferedWriter praatBuffWrite = new BufferedWriter(praatWriter);
	    	
	    	praatBuffWrite.write(script.toString());
			praatBuffWrite.close();
		} 
    	catch (IOException e) 
		{
			e.printStackTrace();
		};
							
		executePraatScript(praatFile.getAbsolutePath());
	}
	/**
	 * Finds the peak of an audio file
	 * @param shotNumber
	 * @param paceFile
	 * @param segmentPath2 
	 */
	private void findPeak(int shotNumber, File paceFile, String segmentPath2) {
		try 
		{
			WavFile wavFile = WavFile.openWavFile(new File(segmentPath2.concat("\\"+shotNumber+".wav")));
			ArrayList<Double> distance = new ArrayList<Double>();
			ArrayList<Short> maxVals = new ArrayList<Short>();
			Path path = Paths.get((segmentPath2.concat("\\"+shotNumber+".wav")));
			StringBuilder peakSB = new StringBuilder();
			File peakPath = new File((praatPath.concat("\\Peaks.txt")));
			StringBuilder peaks = new StringBuilder();
			
			
			double samplesPerPixel = wavFile.getSampleRate()/576; // 576 is from visbounds.width
			byte[] data = Files.readAllBytes(path);
			wavFile.display();
			int maxSampleRate = (int)(samplesPerPixel*576);
			maxSampleRate = (int) Math.min(maxSampleRate, wavFile.getSampleRate());
			int index = 0;
			int previousX = 0;
			int previousY = 0;
			int i = 0;
			
			System.out.println("SHOT: " + shotNumber);
			System.out.println("SAMPLE RATE: " + wavFile.getSampleRate());
			System.out.println("SAMPLES PER PIXEL: " + samplesPerPixel);
			System.out.println("Max Sample Rate: " + maxSampleRate);
			
			peakSB = peakSB.append("SHOT: " + shotNumber + "\r\n");
			while(index < maxSampleRate)
			{
				short maxVal = -32767;
				short minVal = 32767;
				
				for(int x = 0; x < samplesPerPixel; x++)
				{
					System.out.println("DATA SIZE: " + data.length +", X+INDEX: " + x +" "+ index +", []: " +(x+index));
					// IDK IF THIS IS CORRECT DATA ARRAY
					maxVal = (short) Math.max(maxVal, data[x+index]);
					minVal = (short) Math.max(minVal, data[x+index]);
				}
				
				peaks = peaks.append(maxVal + "\r\n");
				maxVals.add(maxVal);
				int scaledMinVal = (int)(((minVal + 32768) * 376) / 65536);
				int scaledMaxVal = (int)(((maxVal + 32768) * 376) / 65536);
				
				if(samplesPerPixel > 0.0000000001)
				{
					if(scaledMinVal == scaledMaxVal)
					{
						if(previousY != 0)
						{
							double d = Math.abs(previousX - i) + Math.abs(previousY - maxVal);
							distance.add(d);
						}
					}
					else
					{
						double d = Math.abs(i - i) + Math.abs(minVal - maxVal);
						distance.add(d);
					}
				} else return;
				
				previousX = i;
				previousY = scaledMaxVal;
				i++;
				index = (int)(i * samplesPerPixel);
			}
			
			int numPeaks = 0;
			
            int s1 = 0;
            int s2 = 0;
            int s3 = 0;
            for (int k = 0; k < maxVals.size(); k++)
            {
                s1 = maxVals.get(k);
                if (k + 1 < maxVals.size())
                {
                    s2 = maxVals.get(k+1);
                }
                if (k + 2 < maxVals.size())
                {
                    s3 = maxVals.get(k+2);
                }
                if (k + 2 > maxVals.size()) 
                {
                	break;
                }

                if (s1 < s2 && s2 > s3)
                {
                	peakSB = peakSB.append(s2 + "\r\n");
                    numPeaks++;
                }
                else peakSB = peakSB.append(0 + "\r\n");
            }
            
            peakSB = peakSB.append("NUMBER OF PEAKS: " + numPeaks + "TOTAL: " + maxVals.size() + "\r\n");
            
            FileWriter peakWriter = null;
            FileWriter peaksWriter = null;
        	try 
    		{
        		peakWriter = new FileWriter(paceFile.getAbsoluteFile());			
    			
    			BufferedWriter peakBW = new BufferedWriter(peakWriter);
    	    	
    			peakBW.write(peakSB.toString());
    			peakBW.close();
    			
    			peaksWriter = new FileWriter(peakPath.getAbsoluteFile());			
    			
    			BufferedWriter peakBBBW = new BufferedWriter(peaksWriter);
    	    	
    			peakBBBW.write(peaks.toString());
    			peakBBBW.close();
    		} 
        	catch (IOException e) 
    		{
    			e.printStackTrace();
    		};
		} 
		catch (IOException | WavFileException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Execute a command line script for praatcon
	 * @param filepath
	 */
	public void executePraatScript(String filepath)
	{			
		String[] praatScript = new String[] {"praatcon", filepath.replace("\\", "\\\\")};
		  
		try 
		{	
			Runtime runTime = Runtime.getRuntime();
			Process extractionProcess = runTime.exec(praatScript);									
			
			StreamConsumer errorGobbler = new StreamConsumer(extractionProcess.getErrorStream(), "ERROR");            
			StreamConsumer outputGobbler = new StreamConsumer(extractionProcess.getInputStream(), "OUTPUT");
			
	        errorGobbler.start();
	        outputGobbler.start();
	        int exitVal = extractionProcess.waitFor();
            System.out.println("ExitValue: " + exitVal);   
            if(exitVal == 0)
            {
            	System.out.println("Praat Execution Successful");
            }
      
		} catch (Throwable e) 
		{			
			e.printStackTrace();
		}
	}
}
