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
		double last = 0;
		int wavCount = 1;
		String filePath = "";
		String outPath = "";
		String fileRead = "";
		
		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.indexOf("win") >= 0){
			filePath = "\""+this.audioPath.concat("\\output.mp3") + "\"";
			outPath = "\""+this.audioPath.concat("\\Segments");
			fileRead = this.directoryMain.concat("\\Visual Data\\ShotRange.txt");
		}
		else if (OS.indexOf("mac") >= 0) {
			filePath = "\""+this.audioPath.concat("/output.mp3") + "\"";
			outPath = "\""+this.audioPath.concat("/Segments");
			fileRead = this.directoryMain.concat("/Visual Data/ShotRange.txt");
		}
		
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
//			int startingFrame = Integer.parseInt(lineArr[4]);
			int lastFrame = Integer.parseInt(lineArr[6]);
			int numberOfFramesInShot = lastFrame;
			last = numberOfFramesInShot/16.0;			
			lastSecond = (int) Math.round((last));
			int length =  lastSecond - currentSecond;
			
			if (length == 0)
			{
				currentSecond --;
				length = 1;
			}
			String ffmpegCmd = "ffmpeg -ss "+currentSecond+" -t "+length+"  -i "+ filePath + " "+ outPath+ "\\"+wavCount+".mp3\"";

			if (OS.indexOf("mac") >= 0) {
				System.out.println("OS: Mac");
				ffmpegCmd = "/usr/local/Cellar/ffmpeg/2.4.2/bin/ffmpeg -ss "+currentSecond+" -t "+length+"  -i "+ filePath + " "+ outPath+ "/" +wavCount+".mp3\"";
//				System.out.println(ffmpegCmd);
			}
			
			
			
			
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
			currentSecond = lastSecond;
			wavCount++;
			
		}
	}

}
