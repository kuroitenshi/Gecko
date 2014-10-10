package model;

import java.io.File;

import model.Objects.StreamConsumer;
//Tentative, instantiate to GeckoController
public class AudioExtraction {
	
	private String audioPath;
	private File directoryMain;
	private String shotRangePath;
	
	public AudioExtraction(String resultsPath)
	{
		this.audioPath = resultsPath;
	}
	
	public void setFile(File movieFileChosen)
	{
		this.directoryMain = movieFileChosen;
	}
	
	public void extractAudio()
	{
		//Extracting Audio
		String filePath = "\"" + directoryMain  + "\"";	
		String dirPath = "\"" + this.audioPath.concat("\\Audial Data");
		String ffmpegCmd = "ffmpeg -i "+ filePath + " -ab 160k -ac 2 -ar 44100 -vn " +  dirPath+ "\\"+" output.wav\"";

		System.out.println(ffmpegCmd);
		
		try 
		{
		
			// Fix using Threads
			Runtime runTime = Runtime.getRuntime();
			Process extractionProcess = runTime.exec(ffmpegCmd);									
			
			StreamConsumer errorGobbler = new StreamConsumer(extractionProcess.getErrorStream(), "ERROR");            
			StreamConsumer outputGobbler = new StreamConsumer(extractionProcess.getInputStream(), "OUTPUT");
			
	        errorGobbler.start();
	        outputGobbler.start();
	        int exitVal = extractionProcess.waitFor();
            System.out.println("ExitValue: " + exitVal);   
		} catch (Throwable e) 
		{			
			e.printStackTrace();
		}
		
		
		System.out.println("Audio Completed");
		
	}
	
	
	
	

}
