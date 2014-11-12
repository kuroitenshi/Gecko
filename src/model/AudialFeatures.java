package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.Objects.StreamConsumer;

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
		for(int i = 1; i <= fileCount; i++)
		{			
			script = script.append("Read from file: "+"\""+segmentPath.concat("\\"+i+".wav")+"\"" + "\r\n");			
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
