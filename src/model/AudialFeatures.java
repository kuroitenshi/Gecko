package model;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import model.Objects.StreamConsumer;
import model.Objects.WavFile;
import model.Objects.WavFileException;
import model.Tempo.*;

public class AudialFeatures
{
	private String segmentPath;
	private String directoryMain;
	private String praatPath;
	private String audioEnergyPath;
	private String audioPowerPath;
	private String audioPacePath;
	
	
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
	 * @throws UnsupportedAudioFileException 
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
		System.out.println("TEST ARGS: "+ segmentPath.concat("\\"+1+".mp3"));
		
		File praatFile = new File(praatPath.concat("\\Features.praat"));
		File energyFile = new File (praatPath.concat("\\AudialEnergy.txt"));
		setAudioEnergyPath(energyFile.getAbsolutePath());
		File powerFile = new File (praatPath.concat("\\AudialPower.txt"));
		setAudioPowerPath(powerFile.getAbsolutePath());
		
		
		String OS = System.getProperty("os.name").toLowerCase();
					
		 if (OS.indexOf("mac") >= 0)
		 {
			System.out.println("OS: Mac");
			praatFile = new File(praatPath.concat("/Features.praat"));
			energyFile = new File (praatPath.concat("/AudialEnergy.txt"));
			setAudioEnergyPath(energyFile.getAbsolutePath());
			powerFile = new File (praatPath.concat("/AudialPower.txt"));
			setAudioPowerPath(powerFile.getAbsolutePath());
			
		 }
		
		for(int i = 1; i <= fileCount; i++)
		{			
			script = script.append("Read from file: "+"\""+segmentPath.concat("\\"+i+".mp3")+"\"" + "\r\n");			
			script = script.append("energy$ = Get energy: 0, 0" + "\r\n");
			script = script.append("power$ = Get power: 0, 0" + "\r\n");			
			script = script.append("writeInfoLine: "+ "\"" +"Audio Energy= "+"\""+", energy$" + "\r\n");
			script = script.append("writeInfoLine: "+ "\""+"Audio Power= "+"\""+",power$" + "\r\n");			
			script = script.append("appendFileLine: "+"\""+ energyFile.getAbsolutePath() +"\""+", " + "\"Shot " + i + " \"" + "+ energy$"+ "\r\n");			
			script = script.append("appendFileLine: "+"\""+ powerFile.getAbsolutePath() +"\""+", " + "\"Shot " + i + " \"" + "+ power$"+ "\r\n");
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
	/*Set audio features for shots */
	public void setAudioFeatures(ArrayList<Shot> shotList) throws IOException
	{
		String audioPowerValue;
		ArrayList<String> audioPowerStrings = new ArrayList<String>();
		String audioEnergyValue;
		ArrayList<String> audioEnergyStrings = new ArrayList<String>();
		String audioPaceValue;
		try {
			audioPaceValue = calculateBPM();
		} catch (JavaLayerException e) 
		{		
			e.printStackTrace();
		}		
		ArrayList<String> audioPaceStrings = new ArrayList<String>();
		
		FileReader audioPowerReader = new FileReader(getAudioPowerPath());
		BufferedReader audioPowerBuffReader = new BufferedReader(audioPowerReader);
		FileReader audioEnergyReader = new FileReader(getAudioEnergyPath());
		BufferedReader audioEnergyBuffReader = new BufferedReader(audioEnergyReader);
		FileReader audioPaceReader = new FileReader(getAudioPacePath());
		BufferedReader audioPaceBuffReader = new BufferedReader(audioPaceReader);
		
		
		
		while((audioPowerValue = audioPowerBuffReader.readLine()) != null)
		{			
			audioPowerStrings.add(audioPowerValue.split(" ")[2]);
		}
		
		while((audioEnergyValue = audioEnergyBuffReader.readLine()) != null)
		{		
			audioEnergyStrings.add(audioEnergyValue.split(" ")[2]);
		}
		while((audioPaceValue = audioPaceBuffReader.readLine()) != null)
		{
			audioPaceStrings.add(audioPaceValue);
		}	
		for(int i=0; i < shotList.size(); i++)
		{
			shotList.get(i).setAudioEnergyValue(Double.parseDouble(audioEnergyStrings.get(i)));
			shotList.get(i).setAudioPowerValue(Double.parseDouble(audioPowerStrings.get(i)));
			shotList.get(i).setAudioPaceValue(Double.parseDouble(audioPaceStrings.get(i)));
		}
		
	
	}
	
	
	/**
	 * Finds the peak of an audio file
	 * @param shotNumber
	 * @param paceFile
	 * @param segmentPath2 
	 * @throws UnsupportedAudioFileException 
	 */
	private void findPeak(int shotNumber, File paceFile, String segmentPath2) 
	{
		try 
		{
			WavFile wavFile = WavFile.openWavFile(new File(segmentPath2.concat("\\"+shotNumber+".mp3")));
			ArrayList<Double> distance = new ArrayList<Double>();
			ArrayList<Short> maxVals = new ArrayList<Short>();
			Path path = Paths.get((segmentPath2.concat("\\"+shotNumber+".mp3")));
			StringBuilder peakSB = new StringBuilder();
			File peakPath = new File((praatPath.concat("\\Peaks.txt")));
			StringBuilder peaks = new StringBuilder();
			
			String OS = System.getProperty("os.name").toLowerCase();

			if (OS.indexOf("mac") >= 0) 
			{
				wavFile = WavFile.openWavFile(new File(segmentPath2.concat("/"+shotNumber+".mp3")));
				distance = new ArrayList<Double>();
				maxVals = new ArrayList<Short>();
				path = Paths.get((segmentPath2.concat("/"+shotNumber+".wav")));
				peakSB = new StringBuilder();
				peakPath = new File((praatPath.concat("/Peaks.txt")));
				peaks = new StringBuilder();
			}
			
			
			double samplesPerPixel = wavFile.getSampleRate()/576; // 576 is from visbounds.width
			/*Reading Unsigned Int from .wav files*/			
			InputStream fileInputStream = new FileInputStream(new File(path.toString()));
			DataInputStream dataStream = new DataInputStream(fileInputStream);
			int streamLength = dataStream.available();			
			byte[] byteBuffer = new byte[streamLength];					
			dataStream.readFully(byteBuffer);
			ArrayList<Short> waveData = new ArrayList<Short>();
			
			/*USING DATA INPUT STREAM*/
			for(int i=0; i < byteBuffer.length; i++)
			{
				/*
				long value = 0;
				long unsigned = 0;
				long value3 = 0;
				long value4 = 0;
				long value5 = 0;
				
				
				value = (value << 8) + (byteBuffer[i] & 0xff);
				unsigned = (unsigned << 8) + (byteBuffer[i] & 0xFFFFFFFFL);				
				value3 = (byteBuffer[i] & 0xFFFFFFFFL);
				value4 = (value4 << 4) + (byteBuffer[i] & 0xFFFFFFFFL);
				value5 = (byteBuffer[i] & 0xff);

				//System.out.println("RAW BYTE " + byteBuffer[i]);
				//System.out.println("NUMERIC EQUIVALENT " + value); //Normal Reading for WAVES
				//System.out.println("GOAL UNSIGNED LONG " + unsigned);
				//System.out.println("RAW BYTE CONVERSION UNSIGNED LONG " + value3);
				//System.out.println("BYTE 4 SHIFTS " + value4);
				//System.out.println("BYTE NO SHIFTS  " + value5);
				
				*/
				
				short dataVal = (short) (byteBuffer[i]<<8); 			
				waveData.add(dataVal);
			}	
			dataStream.close();
		
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
				System.out.println("SHOT: " + shotNumber);
				for(int x = 0; x < samplesPerPixel; x++)
				{
					// CORRECT THIS
					maxVal = (short) Math.max(maxVal, waveData.get(x+index));
					minVal = (short) Math.max(minVal, waveData.get(x+index));
										
				}
				System.out.println("PEAK = " + maxVal);
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
		String[] praatScript = null; 
		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.indexOf("win") >= 0){
			praatScript = new String[] {"praatcon", filepath.replace("\\", "\\\\")};
		}
		else if (OS.indexOf("mac") >= 0) {
			praatScript = new String[] {"/Users/joshua/Documents/Praat.app/Contents/MacOS/Praat", filepath.replace("/", "//")};
		}  
		
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
	
	/*For Audio Pace*/
	public String calculateBPM() throws FileNotFoundException, JavaLayerException
	{
		
		 Logger log = Logger.getLogger("Tempo");
		
		 File audialSeg = new File(segmentPath);
		 int fileCount = audialSeg.listFiles().length;		 	
		 StringBuilder paceResults = new StringBuilder();
		 
		 for(int i=1; i <= fileCount; i++)
		 {
		 
			 BPM2SampleProcessor processor = new BPM2SampleProcessor();
		     processor.setSampleSize(1024);
		     EnergyOutputAudioDevice output = new EnergyOutputAudioDevice(processor);
		     output.setAverageLength(1024);
		        
		     Player player = new Player(new FileInputStream(segmentPath.concat("\\"+i+".mp3")), output);
		     System.out.println(segmentPath.concat("\\"+i+".mp3"));
		     player.play();		    
		     paceResults = paceResults.append(processor.getBPM() + System.lineSeparator());		     
		 }
		 
		 File paceFile = new File (praatPath.concat("\\AudialPace.txt"));
		 setAudioPacePath(paceFile.getAbsolutePath());
			
		 FileWriter paceWriter = null;
	    	try 
			{
	    		paceWriter = new FileWriter(paceFile.getAbsoluteFile());			
				
				BufferedWriter paceBuffWrite = new BufferedWriter(paceWriter);
		    	
				paceBuffWrite.write(paceResults.toString());
				paceBuffWrite.close();
			} 
	    	catch (IOException e) 
			{
				e.printStackTrace();
			};
				
	     
		 return paceResults.toString();
	}
	
	public String getAudioEnergyPath()
	{
		return audioEnergyPath;
	}

	public void setAudioEnergyPath(String audioEnergyPath) 
	{
		this.audioEnergyPath = audioEnergyPath;
	}

	public String getAudioPowerPath() 
	{
		return audioPowerPath;
	}

	public void setAudioPowerPath(String audioPowerPath)
	{
		this.audioPowerPath = audioPowerPath;
	}

	public String getAudioPacePath()
	{
		return audioPacePath;
	}

	public void setAudioPacePath(String audioPacePath) 
	{
		this.audioPacePath = audioPacePath;
	}
}
