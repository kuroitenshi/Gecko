package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import model.Objects.StreamConsumer;

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
		
		String ffmpegCmd = "";
		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.indexOf("win") >= 0){
			System.out.println("OS: Windows");
			String dirPath = "\"" + this.audioPath.concat("\\Audial Data");
			ffmpegCmd = "ffmpeg -i "+ filePath + " -ab 160k -ac 2 -ar 44100 -vn " +  dirPath+ "\\"+"output.mp3\"";
		}
		else if (OS.indexOf("mac") >= 0) {
			System.out.println("OS: Mac");
			String dirPath = "\"" + this.audioPath.concat("/Audial Data");
			filePath = filePath.replace("\"", "'");
			dirPath = dirPath.replace("\"", "'");
			ffmpegCmd = "/usr/local/Cellar/ffmpeg/2.4.2/bin/ffmpeg -i " + filePath + " -ab 160k -ac 2 -ar 44100 -vn " + dirPath + "/output.mp3'";
		}
		
		
		System.out.println("1st com: "+ ffmpegCmd);
		
		try 
		{

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
		
		
		
		
	}
		
	

}
