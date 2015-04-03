package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import model.Objects.StreamConsumer;
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
			try
			{
				shotList.get(i).setAudioEnergyValue(Double.parseDouble(audioEnergyStrings.get(i)));
				shotList.get(i).setAudioPowerValue(Double.parseDouble(audioPowerStrings.get(i)));
			}catch(IndexOutOfBoundsException e){
				shotList.get(i).setAudioEnergyValue(Double.parseDouble("0"));
				shotList.get(i).setAudioPowerValue(Double.parseDouble("0"));
			}
			shotList.get(i).setAudioPaceValue(Double.parseDouble(audioPaceStrings.get(i)));
			
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
