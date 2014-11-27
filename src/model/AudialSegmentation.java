package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import model.Objects.StreamConsumer;

public class AudialSegmentation {
	private String audioPath;
	private String directoryMain;
	

	public AudialSegmentation(String audialDataPath)
	{
		this.audioPath = audialDataPath;
	}

	
	public void setFile(String resultPath)
	{
		this.directoryMain = resultPath;
	}
	
	public void segmentAudio() throws IOException
	{
		BufferedReader reader = null;
		String perLine = "";
		String[] lineArr = new String[7];
		int currentSecond = 0;
		int lastSecond = 0;
		int wavCount = 1;
		String filePath = "\""+this.audioPath.concat("\\output.wav") + "\"";
		String outPath = "\""+this.audioPath.concat("\\Segments");
		String fileRead = this.directoryMain.concat("\\Visual Data\\ShotRange.txt");
		
		try 
		{
			reader = new BufferedReader(new FileReader(fileRead));
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		
		while((perLine = reader.readLine()) != null)
		{
			lineArr = perLine.split(" ");
			int startingFrame = Integer.parseInt(lineArr[4]);
			int lastFrame = Integer.parseInt(lineArr[6]);
			int numberOfFramesInShot = (lastFrame - startingFrame) + 1;
			lastSecond = (numberOfFramesInShot/16);
			
			String ffmpegCmd = "ffmpeg -ss "+currentSecond+" -t "+lastSecond+"  -i "+ filePath + " "+ outPath+ "\\"+wavCount+".wav\"";
			System.out.println(ffmpegCmd);
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
			
			currentSecond = (lastSecond+1);
			wavCount++;
			
		}
	}

}
